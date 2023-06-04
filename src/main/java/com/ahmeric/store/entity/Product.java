package com.ahmeric.store.entity;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a Product in the system. It is used to model a Product that has an id,
 * name, price, and type.
 */
@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

  @Id
  private String id;
  @NotNull
  private String name;
  @NotNull
  private BigDecimal price;
  @NotNull
  private ProductType type;

}


