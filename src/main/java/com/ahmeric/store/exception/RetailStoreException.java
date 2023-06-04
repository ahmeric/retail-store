package com.ahmeric.store.exception;

import lombok.Getter;

/**
 * Exception representing an error in the retail store application.
 */
@Getter
public class RetailStoreException extends RuntimeException {

  /**
   * The error registry entry associated with the exception.
   */
  private final ErrorRegistry errorRegistry;

  public RetailStoreException(ErrorRegistry errorRegistry) {
    super(errorRegistry.getError().getMessageKey());
    this.errorRegistry = errorRegistry;
  }

}
