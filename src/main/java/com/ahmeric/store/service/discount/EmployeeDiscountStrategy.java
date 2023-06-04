package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

/**
 * Discount strategy for employees.
 */
@NoArgsConstructor
public class EmployeeDiscountStrategy extends AbstractPercentageDiscountStrategy {

  private static final BigDecimal THIRTY_PERCENT = BigDecimal.valueOf(0.3);

  /**
   * Calculate discount for the employee user.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return DiscountResult object containing discount amount and its description.
   */
  @Override
  public DiscountResult calculateDiscount(BillDto billDto) {
    var discount = getDiscountableTotalAmount(billDto).multiply(THIRTY_PERCENT);
    return new DiscountResult(discount, getDiscountDescription(discount,
        THIRTY_PERCENT, getClass().getSimpleName()));
  }

}
