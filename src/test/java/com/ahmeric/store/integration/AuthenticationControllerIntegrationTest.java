package com.ahmeric.store.integration;

import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.model.response.ErrorResponse;
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
class AuthenticationControllerIntegrationTest {

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

    Document document = new Document("_id", new ObjectId())
        .append("userName", "employeeUser")
        .append("userType", "EMPLOYEE")
        .append("registrationDate", Date.from(Instant.parse("2021-01-01T00:00:00Z")))
        .append("password", "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov.");
    collection.insertOne(document);

    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  @AfterEach
  public void cleanup() {
    collection.drop();
  }

  @Test
  void shouldReturn200_whenValidUserRequestIsGivenForRegistration() throws Exception {
    Map<String, String> request = new HashMap<>();
    request.put("userName", "testUser");
    request.put("password", "testPassword");
    request.put("userType", "AFFILIATE");

    ResponseEntity<Void> response = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/register"), request, Void.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void shouldReturn400_whenInvalidUserRequestIsGivenForRegistration() throws Exception {
    Map<String, String> request = new HashMap<>();
    request.put("userName", "testUser");
    request.put("password", "testPassword");
    request.put("userType", "WRONG_USERTYPE");

    ResponseEntity<Void> response = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/register"), request, Void.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturn200_whenValidAuthenticationRequestIsGiven() throws Exception {

    Map<String, String> request = new HashMap<>();
    request.put("userName", "employeeUser");
    request.put("password", "password");

    ResponseEntity<AuthenticationResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), request,
            AuthenticationResponse.class);

    String token = Objects.requireNonNull(responseToken.getBody()).getToken();

    // Validate token
    Assertions.assertNotNull(token);
    Assertions.assertFalse(token.isEmpty());
    Assertions.assertEquals(3, token.split("\\.").length);
    Assertions.assertEquals(HttpStatus.OK, responseToken.getStatusCode());
  }

  @Test
  void shouldReturn401_whenInvalidAuthenticationRequestIsGiven() {
    Map<String, String> request = new HashMap<>();
    request.put("userName", "employeeUser");
    request.put("password", "wrongPassword");

    ResponseEntity<ErrorResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), request,
            ErrorResponse.class);

    Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseToken.getStatusCode());
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}

