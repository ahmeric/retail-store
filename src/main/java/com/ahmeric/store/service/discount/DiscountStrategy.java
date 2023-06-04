package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import org.springframework.core.Ordered;

/**
 * Interface for all discount strategies.
 */
public interface DiscountStrategy extends Ordered {

  /**
   * Calculate discount based on the strategy.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return DiscountResult object containing discount amount and its description.
   */
  DiscountResult calculateDiscount(BillDto billDto);
}
