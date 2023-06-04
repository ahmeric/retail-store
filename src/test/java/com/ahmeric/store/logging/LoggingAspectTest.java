package com.ahmeric.store.logging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

  @Spy
  private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  @InjectMocks
  private final LoggingAspect loggingAspect = new LoggingAspect();

  @Mock
  private ProceedingJoinPoint proceedingJoinPoint;

  @Mock
  private JoinPoint joinPoint;

  @Mock
  private Signature signature;

  @Test
  void givenProceedingJoinPoint_whenLogAround_thenLogInfo() throws Throwable {
    // inject the spy logger into the loggingAspect
    ReflectionTestUtils.setField(loggingAspect, "log", logger);

    when(proceedingJoinPoint.getSignature()).thenReturn(signature);
    when(signature.getDeclaringTypeName()).thenReturn("TypeName");
    when(signature.getName()).thenReturn("MethodName");
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{});
    when(proceedingJoinPoint.proceed()).thenReturn("Result");

    loggingAspect.logAround(proceedingJoinPoint);

    verify(logger, times(2)).info(any(String.class), eq("TypeName"), eq("MethodName"),
        any(Object.class));
    verify(logger, times(1)).info(any(String.class), eq("TypeName"), eq("MethodName"),
        eq("Result"));
  }

  @Test
  void givenJoinPoint_whenLogAfterThrowing_thenLogError() {
    // inject the spy logger into the loggingAspect
    ReflectionTestUtils.setField(loggingAspect, "log", logger);

    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getDeclaringTypeName()).thenReturn("TypeName");
    when(signature.getName()).thenReturn("MethodName");

    loggingAspect.logAfterThrowing(joinPoint, new Exception("Exception"));

    verify(logger, times(1)).error(any(String.class), eq("TypeName"), eq("MethodName"),
        any(Object.class));
  }

  @Test
  void logAround_whenProceedThrowsIllegalArgumentException_thenLogErrorAndRethrow()
      throws Throwable {

    ReflectionTestUtils.setField(loggingAspect, "log", logger);

    when(proceedingJoinPoint.getSignature()).thenReturn(signature);
    when(signature.getDeclaringTypeName()).thenReturn("TypeName");
    when(signature.getName()).thenReturn("MethodName");
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{});
    when(proceedingJoinPoint.proceed()).thenThrow(IllegalArgumentException.class);

    assertThrows(IllegalArgumentException.class,
        () -> loggingAspect.logAround(proceedingJoinPoint));
    verify(logger, times(1)).info(any(String.class), eq("TypeName"), eq("MethodName"),
        any(Object.class));
    verify(logger, times(1)).error(any(String.class), any(Object.class),
        eq("TypeName"), eq("MethodName")
    );

  }

}
