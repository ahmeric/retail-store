package com.ahmeric.store.integration;

import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.model.response.UserListResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerIntegrationTest {

  @Container
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
  private static MongoClient mongoClient;
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  private MongoDatabase database;
  private MongoCollection<Document> collection;

  @DynamicPropertySource
  static void mongodbProperties(DynamicPropertyRegistry registry) {
    String uri = mongoDBContainer.getReplicaSetUrl();
    registry.add("spring.data.mongodb.uri", () -> uri);
    mongoClient = MongoClients.create(uri);
  }

  @BeforeEach
  public void setup() {
    database = mongoClient.getDatabase("test");
    collection = database.getCollection("users");

    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @AfterEach
  public void cleanup() {
    // Clean up the users collection after each test
    collection.drop();
  }

  @Test
  void shouldReturn200_whenGetAllUsersRequestIsGivenWithValidToken() {

    var token = getEmployeeUserToken();

    // Request headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);

    var request = new HttpEntity(headers);

    // Make the GET request
    ResponseEntity<UserListResponse> response = restTemplate.exchange(
        createURLWithPort("/api/v1/users"),
        HttpMethod.GET,
        request,
        UserListResponse.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void shouldReturn403_whenGetAllUsersRequestIsGivenWithInvalidToken() {
    // Request headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer invalid_token");

    var request = new HttpEntity(headers);

    // Make the GET request
    ResponseEntity<Void> response = restTemplate.exchange(
        createURLWithPort("/api/v1/users"),
        HttpMethod.GET,
        request,
        Void.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void shouldReturn403_whenGetAllUsersRequestIsCalledByNonEmployeeUser() {
    // Request headers
    var token = getCustomerUserToken();

    // Request headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);

    var request = new HttpEntity(headers);

    // Make the GET request
    ResponseEntity<Void> response = restTemplate.exchange(
        createURLWithPort("/api/v1/users"),
        HttpMethod.GET,
        request,
        Void.class);

    Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }

  private String getEmployeeUserToken() {

    Document document = new Document("_id", new ObjectId())
        .append("userName", "employeeUser")
        .append("userType", "EMPLOYEE")
        .append("registrationDate", Date.from(Instant.parse("2021-01-01T00:00:00Z")))
        .append("password", "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov.");
    collection.insertOne(document);

    // Generate a valid token
    Map<String, String> authRequest = new HashMap<>();
    authRequest.put("userName", "employeeUser");
    authRequest.put("password", "password");

    ResponseEntity<AuthenticationResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), authRequest,
            AuthenticationResponse.class);

    return "Bearer " + Objects.requireNonNull(responseToken.getBody()).getToken();
  }

  private String getCustomerUserToken() {

    Document document = new Document("_id", new ObjectId())
        .append("userName", "employeeUser")
        .append("userType", "CUSTOMER")
        .append("registrationDate", Date.from(Instant.parse("2021-01-01T00:00:00Z")))
        .append("password", "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov.");
    collection.insertOne(document);

    // Generate a valid token
    Map<String, String> authRequest = new HashMap<>();
    authRequest.put("userName", "employeeUser");
    authRequest.put("password", "password");

    ResponseEntity<AuthenticationResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), authRequest,
            AuthenticationResponse.class);

    return "Bearer " + Objects.requireNonNull(responseToken.getBody()).getToken();
  }
}

