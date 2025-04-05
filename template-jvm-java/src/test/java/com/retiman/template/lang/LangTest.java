package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public final class LangTest {
  @Test
  public void testEqualityWithIntegerPool() {
    // https://stackoverflow.com/questions/13098143/why-does-the-behavior-of-the-integer-constant-pool-change-at-127
    // https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.7
    assertThat(isIntegerEquals(127, 127)).isTrue();
    assertThat(isIntegerEquals(128, 128)).isFalse();
  }

  @Test
  public void testAbsoluteValue() {
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#abs-int-
    assertThat(Math.abs(Integer.MIN_VALUE)).isLessThan(0);
    assertThat(Math.abs(-2147483648)).isLessThan(0);
  }

  @Test
  public void testVar() {
    // Java 10 introduced the var keyword.  For some reason, you also declare constants with the var
    // keyword.
    var x = 1;
    final var y = 1;

    assertThat(x + y).isEqualTo(2);
  }

  @Test
  public void testOptional() {
    // Java 9 introduced Optional#ifPresentOrElse() so you can handle the just and nothing case
    // separately.
    Optional.of("Alice")
        .ifPresentOrElse(
            name -> assertThat(name).isEqualTo("Alice"), () -> fail("No value present"));

    // Java 9 introduced Optional#or() so you can supply multiple fallbacks.
    var fallbacks =
        Optional.empty()
            .or(() -> Optional.of("first fallback"))
            .or(() -> Optional.of("second fallback"))
            .or(() -> Optional.of("third fallback"));
    assertThat(fallbacks.get()).isEqualTo("first fallback");
  }

  @SuppressFBWarnings("RC_REF_COMPARISON")
  private boolean isIntegerEquals(Integer a, Integer b) {
    return a == b;
  }
}
