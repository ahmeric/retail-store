package com.ahmeric.store.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.Product;
import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.repository.ProductRepository;
import com.ahmeric.store.utils.Mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private Mapper modelMapper;

  private Product product1;
  private ProductDto productDto1;

  @BeforeEach
  void setUp() {
    product1 = Product.builder().id("1").name("product1").price(BigDecimal.valueOf(100))
        .type(ProductType.ELECTRONICS).build();
    productDto1 = ProductDto.builder().id("1").name("product1").price(BigDecimal.valueOf(100))
        .type(ProductType.ELECTRONICS).build();
  }

  @Test
  void testCreateProduct_GivenValidProductDTO_ShouldReturnProductDTO() {
    when(modelMapper.map(productDto1, Product.class)).thenReturn(product1);
    when(productRepository.save(product1)).thenReturn(product1);
    when(modelMapper.map(product1, ProductDto.class)).thenReturn(productDto1);

    ProductDto result = productService.createProduct(productDto1);

    assertEquals("1", result.getId());
    assertEquals("product1", result.getName());
    assertEquals(BigDecimal.valueOf(100), result.getPrice());
    verify(productRepository, times(1)).save(product1);
  }

  @Test
  void testGetAllProducts_WhenProductsExist_ShouldReturnListOfProductDTOs() {
    when(productRepository.findAll()).thenReturn(List.of(product1));
    when(modelMapper.map(List.of(product1), Mapper.PRODUCT_DTO_LIST_TYPE))
        .thenReturn(List.of(productDto1));

    List<ProductDto> result = productService.getAllProducts();

    assertEquals(1, result.size());
    assertEquals("1", result.get(0).getId());
    assertEquals("product1", result.get(0).getName());
    assertEquals(BigDecimal.valueOf(100), result.get(0).getPrice());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  void testGetProductById_GivenValidId_ShouldReturnProductDTO() {
    when(productRepository.findById("1")).thenReturn(Optional.of(product1));
    when(modelMapper.map(product1, ProductDto.class)).thenReturn(productDto1);

    ProductDto result = productService.getProductById("1");

    assertEquals("1", result.getId());
    assertEquals("product1", result.getName());
    assertEquals(BigDecimal.valueOf(100), result.getPrice());
    verify(productRepository, times(1)).findById("1");
  }

  @Test
  void testGetProductById_GivenInvalidId_ShouldThrowRetailStoreException() {
    when(productRepository.findById("unknown")).thenReturn(Optional.empty());

    assertThrows(RetailStoreException.class, () -> productService.getProductById("unknown"));
    verify(productRepository, times(1)).findById("unknown");
  }

  @Test
  void testUpdateProduct_GivenValidProductDTO_ShouldReturnUpdatedProductDTO() {
    when(productRepository.findById("1")).thenReturn(Optional.of(product1));
    when(productRepository.save(product1)).thenReturn(product1);
    when(modelMapper.map(product1, ProductDto.class)).thenReturn(productDto1);

    ProductDto result = productService.updateProduct(productDto1);

    assertEquals("1", result.getId());
    assertEquals("product1", result.getName());
    assertEquals(BigDecimal.valueOf(100), result.getPrice());
    verify(productRepository, times(1)).save(product1);
  }

  @Test
  void testUpdateProduct_GivenInvalidProductDTO_ShouldThrowRetailStoreException() {
    when(productRepository.findById("unknown")).thenReturn(Optional.empty());
    var productDto = ProductDto.builder().id("unknown").build();
    assertThrows(RetailStoreException.class,
        () -> productService.updateProduct(productDto));
    verify(productRepository, times(1)).findById("unknown");
  }
}
