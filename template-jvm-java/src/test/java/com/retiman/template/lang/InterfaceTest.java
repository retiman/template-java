package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;

import com.retiman.template.lang.testing.JavaInterface;
import org.junit.jupiter.api.Test;

public final class InterfaceTest {
  @Test
  public void testDefaultMethodsInInterfaces() {
    var instance = new JavaInterface() {};

    assertThat(instance.getValue()).isEqualTo(40);
  }
}
