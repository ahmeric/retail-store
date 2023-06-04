package com.ahmeric.store.service;

import com.ahmeric.store.entity.Product;
import com.ahmeric.store.exception.ErrorRegistry;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.repository.ProductRepository;
import com.ahmeric.store.utils.Mapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for Product operations. Handles product creation, retrieval and update.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final Mapper modelMapper;

  /**
   * Creates a new product.
   *
   * @param productDto DTO of the product to be created.
   * @return Created ProductDto.
   */
  public ProductDto createProduct(ProductDto productDto) {
    var product = modelMapper.map(productDto, Product.class);
    productRepository.save(product);
    return modelMapper.map(product, ProductDto.class);
  }

  /**
   * Returns a list of all products.
   *
   * @return List of ProductDto.
   */
  public List<ProductDto> getAllProducts() {
    return modelMapper.map(productRepository.findAll(), Mapper.PRODUCT_DTO_LIST_TYPE);
  }

  /**
   * Returns a product by its ID. Throws an exception if the product with the provided ID cannot be
   * found.
   *
   * @param id The ID of the product.
   * @return ProductDto for the requested product.
   */
  public ProductDto getProductById(String id) {
    var product = productRepository.findById(id).orElseThrow(() -> new RetailStoreException(
        ErrorRegistry.PRODUCT_NOT_FOUND));
    return modelMapper.map(product, ProductDto.class);
  }

  /**
   * Updates an existing product. Throws an exception if the product to be updated cannot be found.
   *
   * @param productDto DTO of the product to be updated.
   * @return Updated ProductDto.
   */
  public ProductDto updateProduct(ProductDto productDto) {
    var product = productRepository.findById(productDto.getId())
        .orElseThrow(() -> new RetailStoreException(ErrorRegistry.PRODUCT_NOT_FOUND));
    product.setName(productDto.getName());
    product.setPrice(productDto.getPrice());
    product.setType(productDto.getType());
    productRepository.save(product);

    return modelMapper.map(product, ProductDto.class);
  }

}
