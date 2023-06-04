package com.ahmeric.store.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.Bill;
import com.ahmeric.store.entity.Product;
import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.repository.BillRepository;
import com.ahmeric.store.service.discount.DiscountService;
import com.ahmeric.store.utils.Mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

  @Mock
  private BillRepository billRepository;

  @Mock
  private DiscountService discountService;

  @Mock
  private ProductService productService;

  @Mock
  private UserService userService;

  @Mock
  private Mapper modelMapper;

  @InjectMocks
  private BillService billService;

  @Mock
  private Authentication authentication;

  @Mock
  private User user;

  private BillDto billDto;
  private Bill bill;
  private UserDto userDto;
  private ProductDto productDto;

  @BeforeEach
  void setUp() {
    productDto = ProductDto.builder().id("product1")
        .price(new BigDecimal("100.00")).type(ProductType.GROCERY).build();
    userDto = UserDto.builder().id("user1").userName("testUser").userType(UserType.EMPLOYEE)
        .build();
    billDto = BillDto.builder().id("bill1").user(userDto).products(List.of(productDto))
        .totalAmount(new BigDecimal("100.00")).discount(BigDecimal.ZERO)
        .netAmount(new BigDecimal("100.00")).productIdList(List.of("productId")).build();

    bill = Bill.builder().id("bill1").userId("user1").userType(UserType.EMPLOYEE)
        .products(List.of(new Product()))
        .totalAmount(new BigDecimal("100.00")).discount(BigDecimal.ZERO)
        .netAmount(new BigDecimal("100.00")).build();
  }

  @Test
  void givenBillDto_whenGenerateBillByProductIds_thenReturnBillDto() {
    // Set up the user principal
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    Mockito.when(user.getUsername()).thenReturn("testUser");

    when(productService.getProductById(anyString())).thenReturn(productDto);
    when(userService.findByUserName(anyString())).thenReturn(userDto);
    when(billRepository.save(any(Bill.class))).thenReturn(bill);
    when(modelMapper.map(billDto, Bill.class)).thenReturn(bill);
    when(modelMapper.map(bill, BillDto.class)).thenReturn(billDto);

    // Act
    BillDto result = billService.generateBillByProductIds(billDto);

    // Assert
    verify(productService, times(billDto.getProductIdList().size())).getProductById(anyString());
    verify(userService, times(1)).findByUserName(anyString());
    verify(billRepository, times(1)).save(any(Bill.class));
    verify(modelMapper, times(1)).map(billDto, Bill.class);
    verify(modelMapper, times(1)).map(bill, BillDto.class);
    assertEquals(billDto, result);
  }

  @Test
  void whenGetAllBills_thenReturnListOfBillDto() {
    // Arrange
    List<Bill> bills = List.of(bill);
    when(billRepository.findAll()).thenReturn(bills);
    when(modelMapper.map(bills, Mapper.BILL_DTO_LIST_TYPE)).thenReturn(List.of(billDto));

    // Act
    List<BillDto> result = billService.getAllBills();

    // Assert
    verify(billRepository, times(1)).findAll();
    verify(modelMapper, times(1)).map(bills, Mapper.BILL_DTO_LIST_TYPE);
    assertEquals(billDto, result.get(0));
  }

  @Test
  void givenId_whenGetBillById_thenReturnBillDto() {
    // Arrange
    when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
    when(modelMapper.map(bill, BillDto.class)).thenReturn(billDto);

    // Act
    BillDto result = billService.getBillById("id");

    // Assert
    verify(billRepository, times(1)).findById(anyString());
    verify(modelMapper, times(1)).map(bill, BillDto.class);
    assertEquals(billDto, result);
  }

  @Test
  void givenId_whenGetBillByIdAndBillNotFound_thenThrowRetailStoreException() {
    // Arrange
    when(billRepository.findById(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RetailStoreException.class, () -> billService.getBillById("id"));
    verify(billRepository, times(1)).findById(anyString());
  }
}

