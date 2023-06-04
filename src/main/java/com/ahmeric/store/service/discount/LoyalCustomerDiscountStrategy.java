package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

/**
 * Discount strategy for loyal customers.
 */
@NoArgsConstructor
public class LoyalCustomerDiscountStrategy extends AbstractPercentageDiscountStrategy {

  private static final BigDecimal FIVE_PERCENT = BigDecimal.valueOf(0.05);

  /**
   * Calculate discount for the loyal customer user.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return DiscountResult object containing discount amount and its description.
   */
  @Override
  public DiscountResult calculateDiscount(BillDto billDto) {
    var discount = getDiscountableTotalAmount(billDto).multiply(FIVE_PERCENT);
    return new DiscountResult(discount, getDiscountDescription(discount,
        FIVE_PERCENT, getClass().getSimpleName()));
  }

}

