package com.ahmeric.store.service.discount;

import com.ahmeric.store.entity.ProductType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeDiscountStrategyTest extends AbstractDiscountStrategyTestBase {

  private EmployeeDiscountStrategy employeeDiscountStrategy;

  @BeforeEach
  void setUp() {
    employeeDiscountStrategy = new EmployeeDiscountStrategy();
  }

  @Override
  protected DiscountStrategy getDiscountStrategy() {
    return employeeDiscountStrategy;
  }

  @Override
  protected BigDecimal getDiscountPercentage() {
    return BigDecimal.valueOf(0.3);
  }

  @ParameterizedTest
  @CsvSource({
      "100, 30.0, ELECTRONICS",
      "100, 0.0, GROCERY",
      "200, 60.0, CLOTHING",
      "300, 90.0, ELECTRONICS",
      "400, 120.0, CLOTHING",
      "500, 0.0, GROCERY"
  })
  void givenBill_whenCalculateDiscount_thenReturnCorrectDiscount(
      BigDecimal productPrice, BigDecimal expectedDiscount, ProductType type) {
    singleProductTest(productPrice, expectedDiscount, type);
  }

  @ParameterizedTest
  @CsvSource({
      "100, 15.0, ELECTRONICS, GROCERY",
      "200, 60.0, CLOTHING, ELECTRONICS",
      "300, 90.0, ELECTRONICS, CLOTHING",
      "400, 60.0, GROCERY, CLOTHING",
      "500, 75.0, GROCERY, ELECTRONICS"
  })
  void givenBillWithMultipleProducts_whenCalculateDiscount_thenReturnCorrectDiscount(
      int totalProductCount, BigDecimal expectedDiscount, ProductType type1, ProductType type2) {
    multipleProductsTest(totalProductCount, expectedDiscount, type1, type2);
  }

}

