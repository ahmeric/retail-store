package com.ahmeric.store.entity;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a Bill in the system. It is used to model a Bill that contains products and
 * user related information.
 */
@Document(collection = "bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill {

  @Id
  private String id;
  private String userId;
  private UserType userType;
  private List<Product> products;
  private BigDecimal totalAmount;
  private BigDecimal discount;
  private BigDecimal netAmount;
  private List<String> appliedDiscounts;
}
