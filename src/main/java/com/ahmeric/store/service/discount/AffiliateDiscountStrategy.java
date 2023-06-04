package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

/**
 * Discount strategy for affiliates.
 */
@NoArgsConstructor
public class AffiliateDiscountStrategy extends AbstractPercentageDiscountStrategy {

  private static final BigDecimal TEN_PERCENT = BigDecimal.valueOf(0.1);

  /**
   * Calculate discount for the affiliate user.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return DiscountResult object containing discount amount and its description.
   */
  @Override
  public DiscountResult calculateDiscount(BillDto billDto) {
    var discount = getDiscountableTotalAmount(billDto).multiply(TEN_PERCENT);
    return new DiscountResult(discount, getDiscountDescription(discount,
        TEN_PERCENT, getClass().getSimpleName()));
  }

}
