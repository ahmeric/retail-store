package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;

/**
 * Fixed amount discount strategy.
 */
public class FixedAmountDiscountStrategy implements DiscountStrategy {

  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
  private static final BigDecimal FIVE = BigDecimal.valueOf(5);

  /**
   * Calculate discount based on the fixed amount strategy.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return DiscountResult object containing discount amount and its description.
   */
  @Override
  public DiscountResult calculateDiscount(BillDto billDto) {
    var discount = billDto.getNetAmount().divideToIntegralValue(HUNDRED).multiply(FIVE);
    return new DiscountResult(discount, getDescription(discount));
  }

  /**
   * Returns the order of the discount strategy.
   *
   * @return Order of the discount strategy.
   */
  @Override
  public int getOrder() {
    return 2;
  }

  /**
   * Gets description of the discount.
   *
   * @param discount Discount amount.
   * @return Description of the discount.
   */
  private String getDescription(BigDecimal discount) {
    return String.format("%s -  amount: %s",
        getClass().getSimpleName(),
        discount.toPlainString());
  }

}
