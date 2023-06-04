package com.ahmeric.store.service.discount;

import com.ahmeric.store.entity.ProductType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AffiliateDiscountStrategyTest extends AbstractDiscountStrategyTestBase {

  private AffiliateDiscountStrategy affiliateDiscountStrategy;

  @BeforeEach
  public void setup() {
    affiliateDiscountStrategy = new AffiliateDiscountStrategy();
  }

  @Override
  protected DiscountStrategy getDiscountStrategy() {
    return affiliateDiscountStrategy;
  }

  @Override
  protected BigDecimal getDiscountPercentage() {
    return BigDecimal.valueOf(0.1);
  }


  @ParameterizedTest
  @CsvSource({
      "100, 10.0, ELECTRONICS",
      "200, 20.0, CLOTHING",
      "300, 30.0, ELECTRONICS",
      "0, 0.0, ELECTRONICS",
      "100, 0.0, GROCERY"
  })
  void givenBillWithProductPriceAndProductType_whenCalculateDiscount_thenReturnCorrectDiscount(
      BigDecimal productPrice, BigDecimal expectedDiscount, ProductType type) {
    singleProductTest(productPrice, expectedDiscount, type);
  }

  @ParameterizedTest
  @CsvSource({
      "100, 5.0, ELECTRONICS, GROCERY",
      "200, 20.0, CLOTHING, ELECTRONICS",
      "300, 30.0, ELECTRONICS, CLOTHING",
      "400, 20.0, GROCERY, CLOTHING",
      "500, 25.0, GROCERY, ELECTRONICS"
  })
  void givenBillWithMultipleProducts_whenCalculateDiscount_thenReturnCorrectDiscount(
      int totalProductCount, BigDecimal expectedDiscount, ProductType type1, ProductType type2) {
    multipleProductsTest(totalProductCount, expectedDiscount, type1, type2);
  }

}


