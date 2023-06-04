package com.ahmeric.store.model.response;

import com.ahmeric.store.entity.UserType;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for a bill. Contains details about the bill including id, user, products, amount,
 * discount and net amount.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

  private String id;
  private String userId;
  private UserType userType;
  private List<ProductResponse> products;
  private BigDecimal totalAmount;
  private BigDecimal discount;
  private BigDecimal netAmount;
  private List<String> appliedDiscounts;
}
