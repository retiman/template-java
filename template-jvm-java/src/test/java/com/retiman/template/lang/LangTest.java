package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.function.Supplier;
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
    // Java 10 introduced the var keyword.  Java favors orthogonality and final already exists, so
    // you can combine language features like building blocks here.  Uh huh.
    var x = 1;
    final var y = 1;

    assertThat(x + y).isEqualTo(2);
  }

  @Test
  public void testStrings() {
    // Java 11 introduced String#strip() to augment String#trim(), which only works on ASCII.
    assertThat(" 你好，世界 ".strip()).isEqualTo("你好，世界");
    assertThat(" Hello World ".trim()).isEqualTo("Hello World");
  }

  @Test
  public void testSwitch() {
    // Java 12 introduced switch expressions.
    var day = (Supplier<String>) () -> "TUESDAY";
    var letters =
        switch (day.get()) {
          case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
          case "TUESDAY" -> 7;
          case "THURSDAY", "SATURDAY" -> 8;
          case "WEDNESDAY" -> 9;
          default -> throw new IllegalArgumentException("Invalid day: " + day);
        };

    assertThat(letters).isEqualTo(7);
  }

  @Test
  public void testTextBlocks() {
    var text = """
      Java 15 introduced text blocks.
     """;

    assertThat(text.trim()).isEqualTo("Java 15 introduced text blocks.");
  }

  @Test
  public void testPatternMatchingForInstanceOf() {
    Object obj = "Hello";

    if (obj instanceof String s) {
      assertThat(s.length()).isEqualTo(5);
    } else {
      fail();
    }
  }

  @SuppressFBWarnings("RC_REF_COMPARISON")
  private boolean isIntegerEquals(Integer a, Integer b) {
    return a == b;
  }
}
