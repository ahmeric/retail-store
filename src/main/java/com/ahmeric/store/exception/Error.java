package com.ahmeric.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing a standard error with a code and a message key.
 */
@Getter
@AllArgsConstructor
public class Error {

  /**
   * The error code.
   */
  private int code;

  /**
   * The error message key.
   */
  private String messageKey;
}
