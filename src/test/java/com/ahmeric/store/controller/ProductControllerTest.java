package com.ahmeric.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.model.request.ProductRequest;
import com.ahmeric.store.model.response.ProductListResponse;
import com.ahmeric.store.model.response.ProductResponse;
import com.ahmeric.store.service.ProductService;
import com.ahmeric.store.utils.Mapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  @Mock
  private ProductService productService;

  @Mock
  private Mapper modelMapper;

  @InjectMocks
  private ProductController controller;

  private ProductRequest productRequest;
  private ProductResponse productResponse;
  private ProductDto productDto;

  @BeforeEach
  void setUp() {
    productRequest = ProductRequest.builder().name("product 1").price(BigDecimal.TEN)
        .type(ProductType.ELECTRONICS).build();
    productResponse = ProductResponse.builder().name("product 1").price(BigDecimal.TEN)
        .type(ProductType.ELECTRONICS).build();
    productDto = ProductDto.builder().name("product 1").price(BigDecimal.TEN)
        .type(ProductType.ELECTRONICS).build();
  }

  @Test
  void givenProductRequest_whenCreateProduct_thenInvokeServiceAndReturnProductResponse() {
    when(modelMapper.map(productRequest, ProductDto.class)).thenReturn(productDto);
    when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);
    when(modelMapper.map(productDto, ProductResponse.class)).thenReturn(productResponse);

    ResponseEntity<ProductResponse> response = controller.createProduct(productRequest);

    verify(productService, times(1)).createProduct(any(ProductDto.class));
    verify(modelMapper, times(1)).map(productDto, ProductResponse.class);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(productResponse, response.getBody());
  }

  @Test
  void whenGetAllProducts_thenInvokeServiceAndReturnProductListResponse() {
    List<ProductDto> productDtoList = List.of(productDto);
    when(productService.getAllProducts()).thenReturn(productDtoList);
    when(modelMapper.map(productDtoList, Mapper.PRODUCT_RESPONSE_LIST_TYPE)).thenReturn(
        List.of(productResponse));

    ResponseEntity<ProductListResponse> response = controller.getAllProducts();

    verify(productService, times(1)).getAllProducts();
    verify(modelMapper, times(1)).map(productDtoList, Mapper.PRODUCT_RESPONSE_LIST_TYPE);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(List.of(productResponse), response.getBody().getProducts());
  }

  @Test
  void givenId_whenGetProductById_thenInvokeServiceAndReturnProductResponse() {
    String id = "id";
    when(productService.getProductById(id)).thenReturn(productDto);
    when(modelMapper.map(productDto, ProductResponse.class)).thenReturn(productResponse);

    ResponseEntity<ProductResponse> response = controller.getProductById(id);

    verify(productService, times(1)).getProductById(id);
    verify(modelMapper, times(1)).map(productDto, ProductResponse.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(productResponse, response.getBody());
  }

  @Test
  void givenProductRequestAndId_whenUpdateProduct_thenInvokeServiceAndReturnProductResponse() {
    String id = "id";
    when(modelMapper.map(productRequest, ProductDto.class)).thenReturn(productDto);
    when(productService.updateProduct(any(ProductDto.class))).thenReturn(productDto);
    when(modelMapper.map(productDto, ProductResponse.class)).thenReturn(productResponse);

    ResponseEntity<ProductResponse> response = controller.updateProduct(id, productRequest);

    verify(productService, times(1)).updateProduct(any(ProductDto.class));
    verify(modelMapper, times(1)).map(productDto, ProductResponse.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(productResponse, response.getBody());
  }

}

