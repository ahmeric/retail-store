package com.ahmeric.store.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The response object for a list of bills. Contains a list of BillResponse objects.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillListResponse {

  private List<BillResponse> bills;
}
