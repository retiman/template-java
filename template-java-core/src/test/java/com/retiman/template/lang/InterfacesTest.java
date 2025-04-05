package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

interface Java8Interface {
  default int getValue() {
    return 10;
  }
}

public final class InterfacesTest {
  @Test
  public void testDefaultMethodsInInterfaces() {
    Java8Interface instance = new Java8Interface() {};

    assertThat(instance.getValue()).isEqualTo(10);
  }
}
