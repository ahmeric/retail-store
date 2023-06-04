package com.ahmeric.store.controller;

import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.model.request.ProductRequest;
import com.ahmeric.store.model.response.ProductListResponse;
import com.ahmeric.store.model.response.ProductResponse;
import com.ahmeric.store.service.ProductService;
import com.ahmeric.store.utils.Mapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class manages product-related operations. It provides REST endpoints for creating,
 * retrieving all products, retrieving product by id and updating a product.
 */
@RestController
@RequestMapping("/api/v1/products")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class ProductController {

  /**
   * The ProductService to handle product-related operations. The Mapper to convert between various
   * model classes.
   */
  private final ProductService productService;
  private final Mapper modelMapper;

  /**
   * Endpoint for creating a product.
   *
   * @param productRequest The request containing product details for creating a product.
   * @return A response entity containing the created product.
   */
  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(
      @RequestBody @Valid ProductRequest productRequest) {
    var productDto = modelMapper.map(productRequest, ProductDto.class);
    productDto = productService.createProduct(productDto);
    var productResponse = modelMapper.map(productDto, ProductResponse.class);
    return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
  }

  /**
   * Endpoint for retrieving all products.
   *
   * @return A response entity containing a list of all products.
   */
  @GetMapping
  public ResponseEntity<ProductListResponse> getAllProducts() {

    List<ProductResponse> productList = modelMapper.map(productService.getAllProducts(),
        Mapper.PRODUCT_RESPONSE_LIST_TYPE);
    return ResponseEntity.ok(ProductListResponse.builder().products(productList).build());
  }

  /**
   * Endpoint for retrieving a product by id.
   *
   * @param id The id of the product to be retrieved.
   * @return A response entity containing the product with the given id.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
    var productDto = productService.getProductById(id);
    return ResponseEntity.ok(modelMapper.map(productDto, ProductResponse.class));
  }

  /**
   * Endpoint for updating a product.
   *
   * @param id             The id of the product to be updated.
   * @param productRequest The request containing product details for updating the product.
   * @return A response entity containing the updated product.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id,
      @RequestBody @Valid ProductRequest productRequest) {
    ProductDto productDto = modelMapper.map(productRequest, ProductDto.class);
    productDto.setId(id);
    productDto = productService.updateProduct(productDto);
    return ResponseEntity.ok(modelMapper.map(productDto, ProductResponse.class));
  }

}
