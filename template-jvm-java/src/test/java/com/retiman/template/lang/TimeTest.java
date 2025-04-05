package com.retiman.template.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.*;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

public final class TimeTest {
  @Test
  public void testLocalDate() {
    // With Java 8, JodaTime is obsolete.  Migrate to java.time instead.
    // LocalDate represents a date only.  No time, no timezone.
    var today = LocalDate.now();
    var yesterday = today.minusDays(1);

    assertThat(yesterday.isBefore(today)).isTrue();
  }

  @Test
  public void testLocalTime() {
    // LocalTime represents a time only.  No date, no timezone.
    var morning = LocalTime.of(8, 0);
    var noon = LocalTime.of(12, 0);

    assertThat(morning.isBefore(noon)).isTrue();
  }

  @Test
  public void testLocalDateTime() {
    // LocalDateTime represents a date and time.  No timezone.
    var start = LocalDateTime.now();
    var end = start.plusDays(2).plusHours(5).plusMinutes(37);

    assertThat(start.isBefore(end)).isTrue();
  }

  @Test
  public void testZonedDateTime() {
    // ZonedDateTime represents a date and time with a timezone.
    var start = ZonedDateTime.now(ZoneId.of("America/New_York"));
    var end = start.plusDays(2).plusHours(5).plusMinutes(37);

    assertThat(start.isBefore(end)).isTrue();
  }

  @Test
  public void testInstant() {
    // Instants represent a timestamp (in UTC).
    var now = Instant.now();

    assertThat(now.toEpochMilli()).isGreaterThan(0);
  }

  @Test
  public void testDuration() {
    // Durations represent a based difference like hours, minutes, seconds.
    var duration = Duration.ofHours(1);
    var start = ZonedDateTime.now(ZoneId.of("UTC"));
    var end = start.plus(duration);

    assertThat(start.isBefore(end)).isTrue();
  }

  @Test
  public void testPeriod() {
    // Periods represent a date based difference like years, months, days.
    var period = Period.ofYears(2);
    var start = ZonedDateTime.now(ZoneId.of("UTC"));
    var end = start.plus(period);

    assertThat(start.isBefore(end)).isTrue();
  }

  @Test
  public void testDateTimeFormatter() {
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    var datetime = LocalDateTime.of(2025, 4, 1, 14, 0);

    assertThat(datetime.format(formatter)).isEqualTo("2025-04-01 14:00");

    var parsed = LocalDateTime.parse("2025-04-01 14:00", formatter);

    assertThat(parsed).isEqualTo(datetime);
  }
}
