package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public final class StreamsTest {
  @Test
  public void testStreamsAPI() {
    var xs = List.of(1, 2, 3, 4);

    var result =
        xs.stream()
            .filter(x -> x > 1)
            .map(x -> x + 1)
            // Java 9 introduced takeWhile, dropWhile, and ofNullable.
            .takeWhile(x -> x < 5)
            .reduce(0, Integer::sum);

    assertThat(result).isEqualTo(7);
  }
}
