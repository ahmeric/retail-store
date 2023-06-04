package com.ahmeric.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enum representing a registry of standard errors with associated HTTP statuses.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorRegistry {
  USER_NOT_FOUND(2001,
      "api.error.user.not.found",
      HttpStatus.NOT_FOUND),
  BILL_NOT_FOUND(2002,
      "api.error.bill.not.found",
      HttpStatus.NOT_FOUND),
  PRODUCT_NOT_FOUND(2003,
      "api.error.product.not.found",
      HttpStatus.NOT_FOUND),
  PROBLEM_ON_MAPPER(2004,
      "api.error.mapper.exception",
      HttpStatus.BAD_REQUEST),
  INVALID_USER(2005,
      "api.error.invalid.user",
      HttpStatus.FORBIDDEN),
  USER_ALREADY_EXISTS(2006,
      "api.error.user.already.exist",
      HttpStatus.BAD_REQUEST),
  SAME_ORDER_MULTIPLE_STRATEGIES(2007, "api.error.multiple.discount.strategies.have.same.order",
      HttpStatus.INTERNAL_SERVER_ERROR);

  /**
   * The error associated with the registry entry.
   */
  private Error error;

  /**
   * The HTTP status associated with the registry entry.
   */
  private HttpStatus status;

  ErrorRegistry(int code, String messageKey, HttpStatus status) {
    this.error = new Error(code, messageKey);
    this.status = status;
  }
}
