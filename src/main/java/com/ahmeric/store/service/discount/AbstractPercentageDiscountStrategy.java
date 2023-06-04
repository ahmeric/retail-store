package com.ahmeric.store.service.discount;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import java.math.BigDecimal;

/**
 * Percentage based discount strategy.
 */
public abstract class AbstractPercentageDiscountStrategy implements DiscountStrategy {

  /**
   * Returns the discount description.
   *
   * @param discount     Discount amount.
   * @param percentage   Percentage of the discount.
   * @param discountName Name of the discount.
   * @return Description of the discount.
   */
  protected String getDiscountDescription(BigDecimal discount,
      BigDecimal percentage, String discountName) {
    return String.format("%s - with percentage %s amount: %s",
        discountName,
        percentage.toPlainString(),
        discount.toPlainString());
  }

  /**
   * Calculates the total amount that is eligible for discount.
   *
   * @param billDto The bill to calculate the discountable amount from.
   * @return The discountable amount.
   */
  protected BigDecimal getDiscountableTotalAmount(BillDto billDto) {
    return billDto.getProducts().stream()
        .filter(product -> product.getType() != ProductType.GROCERY)
        .map(ProductDto::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Returns the order of the discount strategy.
   *
   * @return Order of the discount strategy.
   */
  @Override
  public int getOrder() {
    return 1;
  }
}
