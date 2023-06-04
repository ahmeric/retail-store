package com.ahmeric.store.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The response object returned on successful authentication. Contains the authentication token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

  private String token;

  /**
   * Returns a string representation of the object that hides the token field.
   */
  @Override
  public String toString() {
    return "AuthenticationResponse{"
        + "'token'='***'"
        + '}';
  }
}
