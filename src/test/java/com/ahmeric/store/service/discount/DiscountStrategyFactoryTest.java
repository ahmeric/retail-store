package com.ahmeric.store.service.discount;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.UserDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiscountStrategyFactoryTest {

  private final DiscountStrategyFactory discountStrategyFactory = new DiscountStrategyFactory();

  private static Stream<Arguments> provideParametersForDiscountStrategies() {
    return Stream.of(
        Arguments.of(UserType.EMPLOYEE, LocalDate.now(), BigDecimal.valueOf(200),
            List.of(EmployeeDiscountStrategy.class, FixedAmountDiscountStrategy.class)),
        Arguments.of(UserType.AFFILIATE, LocalDate.now(), BigDecimal.valueOf(200),
            List.of(AffiliateDiscountStrategy.class, FixedAmountDiscountStrategy.class)),
        Arguments.of(UserType.CUSTOMER, LocalDate.now().minusYears(3), BigDecimal.valueOf(200),
            List.of(LoyalCustomerDiscountStrategy.class, FixedAmountDiscountStrategy.class)),
        Arguments.of(UserType.CUSTOMER, LocalDate.now().minusYears(1), BigDecimal.valueOf(200),
            List.of(FixedAmountDiscountStrategy.class)),
        Arguments.of(UserType.CUSTOMER, LocalDate.now().minusYears(1), BigDecimal.valueOf(50),
            List.of())
    );
  }

  @ParameterizedTest
  @MethodSource("provideParametersForDiscountStrategies")
  void shouldReturnCorrectDiscountStrategies(UserType userType, LocalDate registrationDate,
      BigDecimal netAmount, List<Class<?>> expectedStrategyClasses) {
    UserDto user = new UserDto();
    user.setUserType(userType);
    user.setRegistrationDate(registrationDate);
    BillDto billDto = new BillDto();
    billDto.setUser(user);
    billDto.setTotalAmount(netAmount);

    List<DiscountStrategy> discountStrategies = discountStrategyFactory.getApplicableDiscounts(
        billDto);
    assertEquals(expectedStrategyClasses.size(), discountStrategies.size());
    for (int i = 0; i < discountStrategies.size(); i++) {
      assertEquals(expectedStrategyClasses.get(i), discountStrategies.get(i).getClass());
    }
  }
}

