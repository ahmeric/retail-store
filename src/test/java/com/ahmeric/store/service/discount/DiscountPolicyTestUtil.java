package com.ahmeric.store.service.discount;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DiscountPolicyTestUtil {

  public static BillDto createBillWithProductPriceAndType(BigDecimal productPrice,
      ProductType type) {
    BillDto billDto = new BillDto();
    List<ProductDto> products = new ArrayList<>();

    products.add(ProductDto.builder().price(productPrice).type(type).build());

    billDto.setProducts(products);
    return billDto;
  }

  public static BillDto createBillWithTotalAmountAndMultipleTypes(int totalProductCount,
      ProductType type1,
      ProductType type2) {
    BillDto billDto = new BillDto();
    List<ProductDto> products = new ArrayList<>();
    // Assuming each product has a price of 1 for simplicity
    // Distribute the products evenly between the two types
    for (int i = 0; i < totalProductCount / 2; i++) {
      products.add(ProductDto.builder().price(BigDecimal.ONE).type(type1).build());
      products.add(ProductDto.builder().price(BigDecimal.ONE).type(type2).build());
    }
    billDto.setProducts(products);
    return billDto;
  }
}
