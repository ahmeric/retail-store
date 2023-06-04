package com.ahmeric.store.service.discount;

import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.exception.ErrorRegistry;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.BillDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * Factory class for getting applicable discounts.
 */
@NoArgsConstructor
@Component
public class DiscountStrategyFactory {

  /**
   * Returns applicable discounts for the bill based on the user type and bill amount.
   *
   * @param billDto Bill details for which discount is to be calculated.
   * @return List of applicable DiscountStrategy objects.
   */
  public List<DiscountStrategy> getApplicableDiscounts(BillDto billDto) {
    List<DiscountStrategy> applicableDiscounts = new ArrayList<>();
    Set<Integer> orders = new HashSet<>();
    UserType userType = billDto.getUser().getUserType();
    LocalDate registrationDate = billDto.getUser().getRegistrationDate();

    // A user can get only one of the percentage based discounts on a bill.
    if (userType == UserType.EMPLOYEE) {
      addStrategy(applicableDiscounts, orders, new EmployeeDiscountStrategy());
    } else if (userType == UserType.AFFILIATE) {
      addStrategy(applicableDiscounts, orders, new AffiliateDiscountStrategy());
    } else if (userType == UserType.CUSTOMER
        && ChronoUnit.YEARS.between(registrationDate, LocalDate.now()) > 2) {
      addStrategy(applicableDiscounts, orders, new LoyalCustomerDiscountStrategy());
    }

    // Add fixed amount discount if applicable initially
    if (billDto.getNetAmount().compareTo(BigDecimal.valueOf(100)) >= 0) {
      applicableDiscounts.add(new FixedAmountDiscountStrategy());
    }

    applicableDiscounts.sort(Comparator.comparingInt(Ordered::getOrder));
    return applicableDiscounts;
  }

  /**
   * Adds a discount strategy to the list of applicable discounts if it does not exist already.
   *
   * @param applicableDiscounts List of applicable discounts.
   * @param orders              Set of existing discount orders.
   * @param strategy            Discount strategy to be added.
   */
  private void addStrategy(List<DiscountStrategy> applicableDiscounts,
      Set<Integer> orders, DiscountStrategy strategy) {
    if (!orders.add(strategy.getOrder())) {
      throw new RetailStoreException(ErrorRegistry.SAME_ORDER_MULTIPLE_STRATEGIES);
    }
    applicableDiscounts.add(strategy);
  }
}

