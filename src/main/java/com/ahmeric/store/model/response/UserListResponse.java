package com.ahmeric.store.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for a list of users. Contains a list of UserResponse objects.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

  private List<UserResponse> users;
}
