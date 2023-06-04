package com.ahmeric.store.service.discount;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

  @Mock
  private DiscountStrategyFactory discountStrategyFactory;

  @InjectMocks
  private DiscountService discountService;

  private DiscountStrategy discountStrategy;
  private DiscountResult discountResult;
  private BillDto billDto;

  @BeforeEach
  public void setUp() {
    discountStrategy = Mockito.mock(DiscountStrategy.class);
    discountResult = new DiscountResult(BigDecimal.TEN, "Test Discount");
    billDto = new BillDto();
  }

  @Test
  void shouldApplyApplicableDiscount_whenDiscountStrategyIsApplicable() {
    Mockito.when(discountStrategyFactory.getApplicableDiscounts(billDto))
        .thenReturn(Collections.singletonList(discountStrategy));
    Mockito.when(discountStrategy.calculateDiscount(billDto)).thenReturn(discountResult);

    BillDto resultBillDto = discountService.applyDiscount(billDto);

    assertAll(
        () -> assertEquals(BigDecimal.TEN, resultBillDto.getDiscount()),
        () -> assertEquals(Collections.singletonList("Test Discount"),
            resultBillDto.getAppliedDiscounts())
    );
  }

  @Test
  void shouldNotApplyAnyDiscount_whenNoDiscountStrategyIsApplicable() {
    Mockito.when(discountStrategyFactory.getApplicableDiscounts(billDto))
        .thenReturn(Collections.emptyList());

    BillDto resultBillDto = discountService.applyDiscount(billDto);

    assertAll(
        () -> assertEquals(BigDecimal.ZERO, resultBillDto.getDiscount()),
        () -> assertEquals(Collections.emptyList(), resultBillDto.getAppliedDiscounts())
    );
  }

  @Test
  void shouldNotApplyDiscount_whenNoDiscountAmountIsZero() {
    DiscountStrategy discountStrategy2 = Mockito.mock(DiscountStrategy.class);
    DiscountResult discountResult2 = new DiscountResult(BigDecimal.ZERO, "Test Discount 2");

    Mockito.when(discountStrategyFactory.getApplicableDiscounts(billDto))
        .thenReturn(List.of(discountStrategy, discountStrategy2));
    Mockito.when(discountStrategy.calculateDiscount(billDto)).thenReturn(discountResult);
    Mockito.when(discountStrategy2.calculateDiscount(billDto)).thenReturn(discountResult2);

    BillDto resultBillDto = discountService.applyDiscount(billDto);

    assertAll(
        () -> assertEquals(BigDecimal.valueOf(10), resultBillDto.getDiscount()),
        () -> assertEquals(List.of("Test Discount"),
            resultBillDto.getAppliedDiscounts())
    );
  }

  @Test
  void shouldApplyMultipleDiscounts_whenMultipleDiscountStrategiesAreApplicable() {
    DiscountStrategy discountStrategy2 = Mockito.mock(DiscountStrategy.class);
    DiscountResult discountResult2 = new DiscountResult(BigDecimal.valueOf(20), "Test Discount 2");

    Mockito.when(discountStrategyFactory.getApplicableDiscounts(billDto))
        .thenReturn(List.of(discountStrategy, discountStrategy2));
    Mockito.when(discountStrategy.calculateDiscount(billDto)).thenReturn(discountResult);
    Mockito.when(discountStrategy2.calculateDiscount(billDto)).thenReturn(discountResult2);

    BillDto resultBillDto = discountService.applyDiscount(billDto);

    assertAll(
        () -> assertEquals(BigDecimal.valueOf(30), resultBillDto.getDiscount()),
        () -> assertEquals(List.of("Test Discount", "Test Discount 2"),
            resultBillDto.getAppliedDiscounts())
    );
  }
}

