package com.ahmeric.store.model.dto;

import com.ahmeric.store.entity.UserType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for a user in the store application. Contains information about the
 * user's name, type, registration date, and login credentials.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private String id;
  private UserType userType;
  private LocalDate registrationDate;
  private String userName;
  private String password;
}
