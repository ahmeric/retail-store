package com.ahmeric.store.model.response;

import com.ahmeric.store.entity.UserType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for a user. Contains details about the user including id, user type, registration
 * date, and username.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

  private String id;
  private UserType userType;
  private LocalDate registrationDate;
  private String userName;
}
