package com.ahmeric.store.controller;


import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.request.BillRequest;
import com.ahmeric.store.model.response.BillListResponse;
import com.ahmeric.store.model.response.BillResponse;
import com.ahmeric.store.service.BillService;
import com.ahmeric.store.utils.Mapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class manages bills-related operations. It provides REST endpoints for creating, retrieving
 * all bills and retrieving bill by id.
 */
@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class BillController {

  /**
   * The BillService to handle bills-related operations. The Mapper to convert between various model
   * classes.
   */
  private final BillService billService;
  private final Mapper modelMapper;

  /**
   * Endpoint for creating a bill.
   *
   * @param billRequest The request containing product ids for creating a bill.
   * @return A response entity containing the created bill.
   */
  @PostMapping
  public ResponseEntity<BillResponse> createBill(@RequestBody @Valid BillRequest billRequest) {
    var billDto = BillDto.builder().productIdList(billRequest.getProductIdLists()).build();
    billDto = billService.generateBillByProductIds(billDto);
    var billResponse = modelMapper.map(billDto, BillResponse.class);
    return new ResponseEntity<>(billResponse, HttpStatus.CREATED);
  }

  /**
   * Endpoint for retrieving all bills.
   *
   * @return A response entity containing a list of all bills.
   */
  @GetMapping
  public ResponseEntity<BillListResponse> getAllBills() {
    List<BillResponse> billList = modelMapper.map(billService.getAllBills(),
        Mapper.BILL_RESPONSE_LIST_TYPE);
    return ResponseEntity.ok(BillListResponse.builder().bills(billList).build());
  }

  /**
   * Endpoint for retrieving a bill by id.
   *
   * @param id The id of the bill to be retrieved.
   * @return A response entity containing the bill with the given id.
   */
  @GetMapping("/{id}")
  public ResponseEntity<BillResponse> getBillById(@PathVariable String id) {
    var billDto = billService.getBillById(id);
    return ResponseEntity.ok(modelMapper.map(billDto, BillResponse.class));
  }

}

