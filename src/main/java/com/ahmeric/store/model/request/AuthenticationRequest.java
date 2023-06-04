package com.ahmeric.store.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for authentication. Contains information about the username and password for
 * authentication.
 */
@Data
@Builder
public class AuthenticationRequest {

  @NotNull(message = "{api.validation.not.null.user.name}")
  private String userName;
  @NotNull(message = "{api.validation.not.null.password}")
  private String password;

  /**
   * Returns a string representation of the object that hides the password field.
   */
  @Override
  public String toString() {
    return "AuthenticationRequest{"
        + " 'userName' ='" + userName + '\''
        + ", 'password' ='***'"
        + '}';
  }
}
