package com.ahmeric.store.service.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ahmeric.store.entity.ProductType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoyalCustomerDiscountStrategyTest extends AbstractDiscountStrategyTestBase {

  private LoyalCustomerDiscountStrategy loyalCustomerDiscountStrategy;


  @BeforeEach
  void setUp() {
    loyalCustomerDiscountStrategy = new LoyalCustomerDiscountStrategy();
  }

  @Override
  protected DiscountStrategy getDiscountStrategy() {
    return loyalCustomerDiscountStrategy;
  }

  @Override
  protected BigDecimal getDiscountPercentage() {
    return BigDecimal.valueOf(0.05);
  }

  @ParameterizedTest
  @CsvSource({
      "100, 5.00, ELECTRONICS",
      "100, 0.00, GROCERY",
      "200, 10.00, CLOTHING",
      "300, 15.00, ELECTRONICS",
      "400, 20.00, CLOTHING",
      "500, 0.00, GROCERY"
  })
  void givenBill_whenCalculateDiscount_thenReturnCorrectDiscount(
      BigDecimal productPrice, BigDecimal expectedDiscount, ProductType type) {
    singleProductTest(productPrice, expectedDiscount, type);
  }

  @ParameterizedTest
  @CsvSource({
      "100, 2.50, ELECTRONICS, GROCERY",
      "200, 10.00, CLOTHING, ELECTRONICS",
      "300, 15.00, ELECTRONICS, CLOTHING",
      "400, 10.00, GROCERY, CLOTHING",
      "500, 12.50, GROCERY, ELECTRONICS"
  })
  void givenBillWithMultipleProducts_whenCalculateDiscount_thenReturnCorrectDiscount(
      int totalProductCount, BigDecimal expectedDiscount, ProductType type1, ProductType type2) {
    multipleProductsTest(totalProductCount, expectedDiscount, type1, type2);
  }

  @Test
  void givenStrategy_whenGetOrder_thenCorrectOrder() {
    assertEquals(1, loyalCustomerDiscountStrategy.getOrder());
  }

}

