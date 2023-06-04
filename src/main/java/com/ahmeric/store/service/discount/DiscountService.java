package com.ahmeric.store.service.discount;

import com.ahmeric.store.model.dto.BillDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class to handle discount related operations.
 */
@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountStrategyFactory discountStrategyFactory;

  /**
   * Applies applicable discounts to the bill.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return Updated BillDto object after applying the discounts.
   */
  public BillDto applyDiscount(BillDto billDto) {

    List<DiscountStrategy> applicableDiscounts
        = discountStrategyFactory.getApplicableDiscounts(billDto);

    applicableDiscounts.stream()
        .map(discountStrategy -> discountStrategy.calculateDiscount(billDto))
        .forEach(discountResult -> {
          if (discountResult.discountAmount().compareTo(BigDecimal.ZERO) > 0) {
            billDto.addDiscount(discountResult.discountAmount());
            billDto.getAppliedDiscounts().add(discountResult.discountDescription());
          }
        });

    return billDto;
  }

}
