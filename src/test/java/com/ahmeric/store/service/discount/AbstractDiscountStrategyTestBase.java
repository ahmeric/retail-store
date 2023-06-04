package com.ahmeric.store.service.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.DiscountResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDiscountStrategyTestBase {

  protected abstract DiscountStrategy getDiscountStrategy();

  protected abstract BigDecimal getDiscountPercentage();

  public void singleProductTest(
      BigDecimal productPrice, BigDecimal expectedDiscount, ProductType type) {

    BillDto billDto = DiscountPolicyTestUtil.createBillWithProductPriceAndType(productPrice, type);

    DiscountResult discountResult = getDiscountStrategy().calculateDiscount(billDto);

    assertEquals(expectedDiscount, discountResult.discountAmount());
    String expectedDescription = String.format("%s - with percentage %s amount: %s",
        getDiscountStrategy().getClass().getSimpleName(),
        getDiscountPercentage().toPlainString(),
        expectedDiscount.toPlainString());

    assertEquals(expectedDescription, discountResult.discountDescription());

  }

  public void multipleProductsTest(
      int totalProductCount, BigDecimal expectedDiscount, ProductType type1, ProductType type2) {

    BillDto billDto = DiscountPolicyTestUtil.createBillWithTotalAmountAndMultipleTypes(
        totalProductCount, type1, type2);

    DiscountResult discountResult = getDiscountStrategy().calculateDiscount(billDto);

    assertEquals(expectedDiscount, discountResult.discountAmount());
    String expectedDescription = String.format("%s - with percentage %s amount: %s",
        getDiscountStrategy().getClass().getSimpleName(),
        getDiscountPercentage().toPlainString(),
        expectedDiscount.toPlainString());

    assertEquals(expectedDescription, discountResult.discountDescription());
  }

  @Test
  void givenStrategy_whenGetOrder_thenCorrectOrder() {
    assertEquals(1, getDiscountStrategy().getOrder());
  }

}

