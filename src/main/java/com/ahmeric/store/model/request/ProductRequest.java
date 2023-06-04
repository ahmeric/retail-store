package com.ahmeric.store.model.request;

import com.ahmeric.store.entity.ProductType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for a product in the store application. Contains information about the product's
 * name, price, and type.
 */
@Data
@Builder
public class ProductRequest {

  @NotNull(message = "{api.validation.not.null.product.name}")
  private String name;
  @NotNull(message = "{api.validation.not.null.product.price}")
  private BigDecimal price;
  @NotNull(message = "{api.validation.not.null.product.type}")
  private ProductType type;
}
