package com.ahmeric.store.model.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

/**
 * Request object for a bill in the store application. Contains information about the product IDs
 * included in the bill.
 */
@Data
public class BillRequest {

  @NotEmpty(message = "{api.validation.not.null.bill.products}")
  private List<String> productIdLists;
}
