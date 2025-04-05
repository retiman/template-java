package com.retiman.template.lang;

import static com.ibm.icu.impl.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public final class OptionalTest {
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
}
