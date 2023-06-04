package com.ahmeric.store.model.dto;

import com.ahmeric.store.entity.ProductType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for a product in the store application. Contains information about the
 * product's name, price, and type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

  private String id;
  private String name;
  private BigDecimal price;
  private ProductType type;
}
