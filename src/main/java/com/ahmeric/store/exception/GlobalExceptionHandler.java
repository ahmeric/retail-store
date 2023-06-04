package com.ahmeric.store.exception;


import com.ahmeric.store.model.response.ErrorResponse;
import com.ahmeric.store.utils.MessageUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class represents a global exception handler. It is used to manage the way exceptions are
 * handled throughout the application.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  // Pattern to extract enum values from exception messages
  private static final Pattern ENUM_MSG = Pattern.compile(
      "values accepted for Enum class: \\[([^\\]]+)\\]");
  private final MessageUtils messageUtils;

  private static String getExceptionMessage(Exception exception) {
    return (exception != null && exception.getCause() != null) ? exception.getCause().getMessage()
        : "";
  }

  /**
   * Handles generic exceptions. If an exception message is not available, the exception's full
   * string representation will be used.
   *
   * @param exception The thrown exception.
   * @return A ResponseEntity containing an ErrorResponse and HTTP status code.
   */
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {

    String errorMessageText = exception.getLocalizedMessage() == null ? exception.toString()
        : exception.getLocalizedMessage();
    ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date())
        .message(errorMessageText).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles custom RetailStoreExceptions. The message key for the error is fetched from the
   * MessageUtils.
   *
   * @param exception The thrown RetailStoreException.
   * @return A ResponseEntity containing an ErrorResponse and HTTP status code.
   */
  @ExceptionHandler(value = {RetailStoreException.class})
  public ResponseEntity<Object> handleException(RetailStoreException exception) {

    String errorMessageText = messageUtils.getMessage(
        exception.getErrorRegistry().getError().getMessageKey());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(new Date())
        .message(errorMessageText)
        .cause(getExceptionMessage(exception))
        .build();
    return new ResponseEntity<>(errorResponse, exception.getErrorRegistry().getStatus());
  }

  /**
   * Handles validation errors which occur when method arguments are not valid. The message from the
   * first field error is used.
   *
   * @param exception The thrown MethodArgumentNotValidException.
   * @return A ResponseEntity containing an ErrorResponse and HTTP status code.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    FieldError fieldError = exception.getBindingResult().getFieldErrors().get(0);
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(new Date())
        .message(fieldError.getDefaultMessage())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles JSON parsing errors. In case of an InvalidFormatException, an additional check for enum
   * values is performed.
   *
   * @param exception The thrown HttpMessageNotReadableException.
   * @return A ResponseEntity containing an ErrorResponse and HTTP status code.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonErrors(HttpMessageNotReadableException exception) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(new Date())
        .message(exception.getMessage())
        .cause(getExceptionMessage(exception))
        .build();
    if (exception.getCause() instanceof InvalidFormatException) {
      Matcher match = ENUM_MSG.matcher(exception.getCause().getMessage());
      if (match.find()) {
        errorResponse.setMessage("The enum value should be: " + match.group(1));
      }
    }
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles AuthenticationExceptions.
   *
   * @param exception The thrown AuthenticationException.
   * @return A ResponseEntity containing an ErrorResponse and HTTP status code.
   */

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException exception) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(new Date())
        .message(exception.getMessage())
        .cause(getExceptionMessage(exception))
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

}
