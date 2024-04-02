package com.retiman.template;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public final class JavaLangTest {
  @Test
  public void testEqualityWithIntegerPool() {
    // See https://stackoverflow.com/questions/13098143/why-does-the-behavior-of-the-integer-constant-pool-change-at-127
    // See https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.7
    assertThat(isIntegerEquals(127, 127)).isTrue();
    assertThat(isIntegerEquals(128, 128)).isFalse();
  }

  @Test
  public void testAbsoluteValue() {
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#abs-int-
    assertThat(Math.abs(Integer.MIN_VALUE)).isLessThan(0);
    assertThat(Math.abs(-2147483648)).isLessThan(0);
  }

  private boolean isIntegerEquals(Integer a, Integer b) {
    return a == b;
  }
}