package com.ahmeric.store.integration;

import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.model.response.BillListResponse;
import com.ahmeric.store.model.response.BillResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BillControllerIntegrationTest {

  @Container
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
  private static MongoClient mongoClient;
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  private MongoDatabase database;
  private MongoCollection<Document> userCollection;
  private MongoCollection<Document> productCollection;
  private MongoCollection<Document> billCollection;

  private String token;

  @DynamicPropertySource
  static void mongodbProperties(DynamicPropertyRegistry registry) {
    String uri = mongoDBContainer.getReplicaSetUrl();
    registry.add("spring.data.mongodb.uri", () -> uri);
    mongoClient = MongoClients.create(uri);
  }

  @BeforeEach
  public void setup() {
    database = mongoClient.getDatabase("test");
    userCollection = database.getCollection("users");
    productCollection = database.getCollection("products");
    billCollection = database.getCollection("bills");

    Document user = new Document("_id", new ObjectId())
        .append("userName", "customerUser")
        .append("userType", "CUSTOMER")
        .append("registrationDate", Date.from(Instant.parse("2023-01-01T00:00:00Z")))
        .append("password", "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov.");
    userCollection.insertOne(user);

    Document product1 = new Document("_id", "product1")
        .append("name", "Product 1")
        .append("price", new BigDecimal("100.00"))
        .append("type", "CLOTHING");
    productCollection.insertOne(product1);

    Document product2 = new Document("_id", "product2")
        .append("name", "Product 2")
        .append("price", new BigDecimal("200.00"))
        .append("type", "ELECTRONICS");
    productCollection.insertOne(product2);

    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    Map<String, String> authRequest = new HashMap<>();
    authRequest.put("userName", "customerUser");
    authRequest.put("password", "password");

    ResponseEntity<AuthenticationResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), authRequest,
            AuthenticationResponse.class);

    this.token = Objects.requireNonNull(responseToken.getBody()).getToken();
  }

  @AfterEach
  public void cleanup() {
    userCollection.drop();
    productCollection.drop();
    billCollection.drop();
  }

  @Test
  void shouldReturn201_whenValidBillRequestIsGiven() {
    List<String> productIds = List.of("product1", "product2");
    ResponseEntity<BillResponse> response = createBill(productIds);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
    Assertions.assertEquals(UserType.CUSTOMER, response.getBody().getUserType());
    Assertions.assertEquals(2, response.getBody().getProducts().size());
    Assertions.assertEquals(new BigDecimal("300.00"), response.getBody().getTotalAmount());
    Assertions.assertNotNull(response.getBody().getDiscount());
    Assertions.assertNotNull(response.getBody().getNetAmount());
  }

  @Test
  void shouldReturn400_whenInvalidBillRequestIsGiven() {
    ResponseEntity<BillResponse> response = createBill(Collections.emptyList());

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturn200_whenGetAllBills() {
    List<String> productIds = List.of("product1", "product2");
    createBill(productIds);
    ResponseEntity<BillListResponse> response = getAllBills();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertFalse(response.getBody().getBills().isEmpty());
    Assertions.assertEquals(1, response.getBody().getBills().size());
  }

  @Test
  void shouldReturn200_whenGetBillById() {
    List<String> productIds = List.of("product1", "product2");
    ResponseEntity<BillResponse> createResponse = createBill(productIds);
    ResponseEntity<BillResponse> response = getBillById(
        Objects.requireNonNull(createResponse.getBody()).getId());

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(createResponse.getBody().getProducts().size(),
        Objects.requireNonNull(response.getBody()).getProducts().size());
    Assertions.assertEquals(UserType.CUSTOMER, response.getBody().getUserType());
    Assertions.assertEquals(2, response.getBody().getProducts().size());
  }

  @Test
  void shouldReturn404_whenGetBillByIdNotFound() {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    ResponseEntity<String> response = restTemplate
        .exchange(createURLWithPort("/api/v1/bills/non-existent-id"),
            HttpMethod.GET, entity, String.class);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  private ResponseEntity<BillResponse> createBill(List<String> productIds) {
    HttpEntity<Map<String, Object>> entity = buildRequestEntity(productIds);
    return restTemplate.postForEntity(createURLWithPort("/api/v1/bills"), entity,
        BillResponse.class);
  }

  private ResponseEntity<BillResponse> getBillById(String id) {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    return restTemplate.exchange(createURLWithPort("/api/v1/bills/" + id), HttpMethod.GET, entity,
        BillResponse.class);
  }

  private ResponseEntity<BillListResponse> getAllBills() {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    return restTemplate.exchange(createURLWithPort("/api/v1/bills"), HttpMethod.GET, entity,
        BillListResponse.class);
  }

  private HttpEntity<Map<String, Object>> buildRequestEntity(List<String> productIds) {
    Map<String, Object> billRequest = new HashMap<>();
    billRequest.put("productIdLists", productIds);
    return new HttpEntity<>(billRequest, getHeadersWithToken());
  }

  private HttpHeaders getHeadersWithToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + token);
    return headers;
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}

