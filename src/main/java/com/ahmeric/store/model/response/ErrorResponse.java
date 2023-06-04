package com.ahmeric.store.model.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for an error in the system. Contains details about the error including timestamp,
 * message, and cause.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {

  private Date timestamp;
  private String message;
  private String cause;
}
