package com.ahmeric.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RetailStoreApplicationTests {

  @Test
  void contextLoads() {
    Assertions.assertDoesNotThrow(() -> RetailStoreApplication.main(new String[]{}));
  }

}
