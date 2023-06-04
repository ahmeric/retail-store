package com.ahmeric.store.utils;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Utility class for retrieving messages from a message source.
 */
@Component
@RequiredArgsConstructor
public class MessageUtils {

  private final MessageSource messageSource;

  /**
   * Retrieves a message by its key.
   *
   * @param messageKey The key of the message.
   * @return The message.
   */
  public String getMessage(String messageKey) {
    return messageSource.getMessage(messageKey, null, Locale.getDefault());
  }

  /**
   * Retrieves a message by its key and fills in the message parameters with provided args.
   *
   * @param messageKey The key of the message.
   * @param args       The arguments to fill in the message.
   * @return The message.
   */
  public String getMessage(String messageKey, Object[] args) {
    return messageSource.getMessage(messageKey, args, Locale.getDefault());
  }
}

