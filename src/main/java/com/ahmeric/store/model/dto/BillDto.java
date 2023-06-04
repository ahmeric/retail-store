package com.ahmeric.store.model.dto;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

/**
 * Data Transfer Object (DTO) for a bill in the store application. Contains information about the
 * bill's total amount, discounts applied, and the net amount after discounts.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BillDto {

  private String id;
  private UserDto user;
  private List<ProductDto> products;
  private BigDecimal totalAmount;
  private BigDecimal discount;
  private BigDecimal netAmount;
  private List<String> appliedDiscounts;
  /**
   * Transient list of product IDs, used for operational convenience but not persisted.
   */
  @Transient
  private List<String> productIdList;

  public void addDiscount(BigDecimal discount) {
    this.discount = getDiscount().add(discount);
  }

  public BigDecimal getNetAmount() {
    return getTotalAmount().subtract(getDiscount());
  }

  /**
   * Returns a list of the discounts applied. If the list is null, it initializes a new ArrayList.
   *
   * @return List of applied discounts.
   */
  public List<String> getAppliedDiscounts() {
    if (this.appliedDiscounts == null) {
      this.appliedDiscounts = new ArrayList<>();
    }
    return this.appliedDiscounts;
  }

  /**
   * Returns the discount applied. If the discount is null, it initializes it to ZERO.
   *
   * @return Discount value.
   */
  public BigDecimal getDiscount() {
    if (this.discount == null) {
      this.discount = BigDecimal.ZERO;
    }
    return this.discount;
  }
}
