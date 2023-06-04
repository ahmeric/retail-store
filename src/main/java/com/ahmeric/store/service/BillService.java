package com.ahmeric.store.service;

import com.ahmeric.store.entity.Bill;
import com.ahmeric.store.exception.ErrorRegistry;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.repository.BillRepository;
import com.ahmeric.store.service.discount.DiscountService;
import com.ahmeric.store.utils.Mapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * Service class for Bill operations. Handles bill generation, and retrieving bills.
 */
@Service
@RequiredArgsConstructor
public class BillService {

  private final BillRepository billRepository;
  private final DiscountService discountService;
  private final ProductService productService;
  private final UserService userService;
  private final Mapper modelMapper;

  /**
   * Generates a bill based on provided Product IDs. Throws an exception if the bill cannot be
   * generated.
   *
   * @param billDto DTO of the bill to be generated.
   * @return Generated BillDto.
   */
  public BillDto generateBillByProductIds(BillDto billDto) {
    gatherBillDto(billDto);
    discountService.applyDiscount(billDto);
    var bill = modelMapper.map(billDto, Bill.class);
    bill.setUserId(billDto.getUser().getId());
    bill.setUserType(billDto.getUser().getUserType());
    bill = billRepository.save(bill);
    return modelMapper.map(bill, BillDto.class);
  }

  private void checkAndFillProducts(BillDto billDto) {
    List<ProductDto> products = billDto.getProductIdList().stream()
        .map(productService::getProductById)
        .toList();
    billDto.setProducts(products);
  }

  /**
   * Returns a list of all bills.
   *
   * @return List of BillDto.
   */
  public List<BillDto> getAllBills() {
    return modelMapper.map(billRepository.findAll(), Mapper.BILL_DTO_LIST_TYPE);
  }

  /**
   * Returns a bill by its ID. Throws an exception if the bill with the provided ID cannot be
   * found.
   *
   * @param id The ID of the bill.
   * @return BillDto for the requested bill.
   */
  public BillDto getBillById(String id) {
    var bill = billRepository.findById(id).orElseThrow(() -> new RetailStoreException(
        ErrorRegistry.BILL_NOT_FOUND));
    return modelMapper.map(bill, BillDto.class);
  }

  private void gatherBillDto(BillDto billDto) {
    var auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userDto = userService.findByUserName(auth.getUsername());
    checkAndFillProducts(billDto);
    billDto.setUser(userDto);
    billDto.setTotalAmount(getTotalAmount(billDto));
    billDto.setDiscount(BigDecimal.ZERO);
    billDto.setNetAmount(billDto.getTotalAmount());
  }

  private BigDecimal getTotalAmount(BillDto billDto) {
    return billDto.getProducts().stream()
        .map(ProductDto::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
