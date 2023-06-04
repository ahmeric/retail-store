package com.ahmeric.store.service.discount;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FixedAmountDiscountStrategyTest {

  private FixedAmountDiscountStrategy fixedAmountDiscountStrategy;

  @BeforeEach
  public void setup() {
    fixedAmountDiscountStrategy = new FixedAmountDiscountStrategy();
  }

  @ParameterizedTest
  @CsvSource({
      "99, 0",
      "100, 5",
      "150, 5",
      "200, 10",
      "290, 10",
      "300, 15"
  })
  void givenBill_whenCalculateDiscount_thenDiscountIsCorrect(BigDecimal netAmount,
      BigDecimal expectedDiscountAmount) {
    BillDto billDto = createBillWithTotalAmount(netAmount);
    DiscountResult discountResult = fixedAmountDiscountStrategy.calculateDiscount(billDto);

    assertEquals(expectedDiscountAmount, discountResult.discountAmount());
    String expectedDescription = String.format("%s -  amount: %s",
        FixedAmountDiscountStrategy.class.getSimpleName(),
        expectedDiscountAmount.toPlainString());
    assertEquals(expectedDescription, discountResult.discountDescription());
  }

  @Test
  void givenStrategy_whenGetOrder_thenCorrectOrder() {
    assertEquals(2, fixedAmountDiscountStrategy.getOrder());
  }

  private BillDto createBillWithTotalAmount(BigDecimal netAmount) {
    BillDto billDto = new BillDto();
    billDto.setTotalAmount(netAmount);
    return billDto;
  }
}

