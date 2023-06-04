package com.ahmeric.store.model.request;

import com.ahmeric.store.entity.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for a user in the store application. Contains information about the user's name,
 * type, and login credentials.
 */
@Data
@Builder
public class UserRequest {

  @NotNull(message = "{api.validation.not.null.user.name}")
  private String userName;
  @NotNull(message = "{api.validation.not.null.user.type}")
  private UserType userType;
  @NotNull(message = "{api.validation.not.null.password}")
  private String password;

  /**
   * Returns a string representation of the object that hides the password field.
   */
  @Override
  public String toString() {
    return "UserRequest{"
        + " 'userName' ='" + userName + '\''
        + ", 'userType' =" + userType
        + ", 'password' ='***'"
        + '}';
  }
}
