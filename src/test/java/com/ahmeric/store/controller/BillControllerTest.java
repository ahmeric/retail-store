package com.ahmeric.store.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.ProductType;
import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.request.BillRequest;
import com.ahmeric.store.model.response.BillListResponse;
import com.ahmeric.store.model.response.BillResponse;
import com.ahmeric.store.service.BillService;
import com.ahmeric.store.utils.Mapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BillControllerTest {

  @Mock
  private BillService billService;

  @Mock
  private Mapper modelMapper;

  @InjectMocks
  private BillController controller;

  private BillRequest billRequest;
  private BillResponse billResponse;
  private BillDto billDto;

  @BeforeEach
  void setUp() {
    billRequest = new BillRequest();
    billRequest.setProductIdLists(List.of("1", "2", "3"));

    UserDto user = UserDto.builder()
        .id("1")
        .userType(UserType.EMPLOYEE)
        .userName("userName")
        .password("password")
        .build();

    ProductDto product = ProductDto.builder()
        .id("1")
        .name("Product Name")
        .price(new BigDecimal("100.00"))
        .type(ProductType.GROCERY)
        .build();

    billDto = BillDto.builder()
        .id("1")
        .user(user)
        .products(Collections.singletonList(product))
        .totalAmount(new BigDecimal("100.00"))
        .discount(new BigDecimal("10.00"))
        .netAmount(new BigDecimal("90.00"))
        .appliedDiscounts(Collections.singletonList("Discount 1"))
        .productIdList(List.of("1", "2", "3"))
        .build();

    billResponse = BillResponse.builder()
        .id("1")
        .totalAmount(new BigDecimal("100.00"))
        .discount(new BigDecimal("10.00"))
        .netAmount(new BigDecimal("90.00"))
        .appliedDiscounts(Collections.singletonList("Discount 1")).build();
  }

  @Test
  void givenBillRequest_whenCreateBill_thenInvokeServiceAndReturnBillResponse() {
    when(billService.generateBillByProductIds(any(BillDto.class))).thenReturn(billDto);
    when(modelMapper.map(billDto, BillResponse.class)).thenReturn(billResponse);

    ResponseEntity<BillResponse> response = controller.createBill(billRequest);

    verify(billService, times(1)).generateBillByProductIds(any(BillDto.class));
    verify(modelMapper, times(1)).map(billDto, BillResponse.class);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(billResponse, response.getBody());
  }

  @Test
  void whenGetAllBills_thenInvokeServiceAndReturnListOfBillResponse() {
    List<BillDto> billDtoList = List.of(billDto);
    when(billService.getAllBills()).thenReturn(billDtoList);
    when(modelMapper.map(billDtoList, Mapper.BILL_RESPONSE_LIST_TYPE)).thenReturn(
        List.of(billResponse));

    ResponseEntity<BillListResponse> response = controller.getAllBills();

    verify(billService, times(1)).getAllBills();
    verify(modelMapper, times(1)).map(billDtoList, Mapper.BILL_RESPONSE_LIST_TYPE);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(BillListResponse.builder().bills(List.of(billResponse)).build(),
        response.getBody());
  }

  @Test
  void givenId_whenGetBillById_thenInvokeServiceAndReturnBillResponse() {
    String id = "id";
    when(billService.getBillById(id)).thenReturn(billDto);
    when(modelMapper.map(billDto, BillResponse.class)).thenReturn(billResponse);

    ResponseEntity<BillResponse> response = controller.getBillById(id);

    verify(billService, times(1)).getBillById(id);
    verify(modelMapper, times(1)).map(billDto, BillResponse.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(billResponse, response.getBody());
  }

}

