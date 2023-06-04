package com.ahmeric.store.model.response;

import com.ahmeric.store.entity.ProductType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for a product. Contains details about the product including id, name, price, and
 * type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

  private String id;
  private String name;
  private BigDecimal price;
  private ProductType type;
}
