package com.ahmeric.store.integration;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.model.response.ProductListResponse;
import com.ahmeric.store.model.response.ProductResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
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
class ProductControllerIntegrationTest {

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

    Document document = new Document("_id", new ObjectId())
        .append("userName", "employeeUser")
        .append("userType", "EMPLOYEE")
        .append("registrationDate", Date.from(Instant.parse("2021-01-01T00:00:00Z")))
        .append("password", "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov.");
    userCollection.insertOne(document);

    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    Map<String, String> request = new HashMap<>();
    request.put("userName", "employeeUser");
    request.put("password", "password");

    ResponseEntity<AuthenticationResponse> responseToken = restTemplate
        .postForEntity(createURLWithPort("/api/v1/auth/authenticate"), request,
            AuthenticationResponse.class);

    this.token = Objects.requireNonNull(responseToken.getBody()).getToken();
  }

  @AfterEach
  public void cleanup() {
    userCollection.drop();
    if (productCollection != null) {
      productCollection.drop();
    }
  }

  @Test
  void shouldReturn201_whenValidProductRequestIsGiven() {
    ResponseEntity<ProductResponse> response = createProduct("Product 2", new BigDecimal("200"),
        "CLOTHING");
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
    Assertions.assertEquals("Product 2", response.getBody().getName());
    Assertions.assertEquals(new BigDecimal("200"), response.getBody().getPrice());
    Assertions.assertEquals(ProductType.CLOTHING, response.getBody().getType());
  }

  @Test
  void shouldReturn400_whenInvalidProductRequestIsGiven() {
    ResponseEntity<ProductResponse> response = createProduct(null, null, null);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturn200_whenGetAllProducts() {
    insertTestDataProduct();
    ResponseEntity<ProductListResponse> response = getAllProducts();
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertFalse(response.getBody().getProducts().isEmpty());
    Assertions.assertEquals(2, response.getBody().getProducts().size());
  }

  @Test
  void shouldReturn200_whenGetProductById() {
    insertTestDataProduct();
    ResponseEntity<ProductResponse> response = getProductById("product1");
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("product1", Objects.requireNonNull(response.getBody()).getId());
    Assertions.assertEquals("Product 1", response.getBody().getName());
  }

  private void insertTestDataProduct() {
    productCollection = database.getCollection("products");

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
  }

  @Test
  void shouldReturn404_whenGetProductByIdNotFound() {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    ResponseEntity<String> response = restTemplate
        .exchange(createURLWithPort("/api/v1/products/non-existent-id"),
            HttpMethod.GET, entity, String.class);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldReturn200_whenUpdateProduct() {
    insertTestDataProduct();
    Map<String, Object> productRequest = new HashMap<>();
    productRequest.put("name", "Updated Product 1");
    productRequest.put("price", new BigDecimal("101"));
    productRequest.put("type", "GROCERY");

    ResponseEntity<ProductResponse> response = updateProduct("product1", productRequest);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("product1", Objects.requireNonNull(response.getBody()).getId());
    Assertions.assertEquals("Updated Product 1", response.getBody().getName());
    Assertions.assertEquals(new BigDecimal("101"), response.getBody().getPrice());
  }


  private ResponseEntity<ProductResponse> createProduct(String name, BigDecimal price,
      String type) {
    HttpEntity<Map<String, Object>> entity = buildRequestEntity(name, price, type);
    return restTemplate.postForEntity(createURLWithPort("/api/v1/products"), entity,
        ProductResponse.class);
  }

  private ResponseEntity<ProductResponse> getProductById(String id) {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    return restTemplate.exchange(createURLWithPort("/api/v1/products/" + id), HttpMethod.GET,
        entity, ProductResponse.class);
  }

  private ResponseEntity<ProductListResponse> getAllProducts() {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getHeadersWithToken());
    return restTemplate.exchange(createURLWithPort("/api/v1/products"), HttpMethod.GET, entity,
        ProductListResponse.class);
  }


  private ResponseEntity<ProductResponse> updateProduct(String id,
      Map<String, Object> productRequest) {
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(productRequest,
        getHeadersWithToken());
    return restTemplate.exchange(createURLWithPort("/api/v1/products/" + id), HttpMethod.PUT,
        entity, ProductResponse.class);
  }

  private HttpEntity<Map<String, Object>> buildRequestEntity(String name, BigDecimal price,
      String type) {
    Map<String, Object> productRequest = new HashMap<>();
    productRequest.put("name", name);
    productRequest.put("price", price);
    productRequest.put("type", type);
    return new HttpEntity<>(productRequest, getHeadersWithToken());
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

