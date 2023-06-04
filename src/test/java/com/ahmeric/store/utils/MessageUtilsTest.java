package com.ahmeric.store.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class MessageUtilsTest {

  @InjectMocks
  private MessageUtils messageUtils;

  @Mock
  private MessageSource messageSource;

  @Test
  void shouldReturnMessage_whenValidMessageKeyProvidedWithoutArgs() {
    // Given
    String messageKey = "test.message.key";
    String expectedMessage = "This is a test message";
    when(messageSource.getMessage(messageKey, null, Locale.getDefault())).thenReturn(
        expectedMessage);

    // When
    String actualMessage = messageUtils.getMessage(messageKey);

    // Then
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldReturnMessage_whenValidMessageKeyAndArgsProvided() {
    // Given
    String messageKey = "test.message.key";
    Object[] args = new Object[]{"arg1", "arg2"};
    String expectedMessage = "This is a test message with args: arg1, arg2";
    when(messageSource.getMessage(messageKey, args, Locale.getDefault())).thenReturn(
        expectedMessage);

    // When
    String actualMessage = messageUtils.getMessage(messageKey, args);

    // Then
    assertEquals(expectedMessage, actualMessage);
  }

}

