package ch.epfl.rigel.astronomy;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpochTest {

    private static final ZonedDateTime ZDT_J2000 = ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.NOON,
            ZoneOffset.UTC);

    private static final ZonedDateTime ZDT_J2010 = ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC);

    private static final ZonedDateTime ZDT_SEMESTER_START = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(13, 15),
            ZoneOffset.ofHours(1));

    private static final ZonedDateTime ZDT1 = ZonedDateTime.of(
            LocalDate.of(1970, Month.SEPTEMBER, 25),
            LocalTime.of(12, 34, 56),
            ZoneOffset.UTC
    );

    private static final ZonedDateTime ZDT2 = ZonedDateTime.of(
            LocalDate.of(1999, Month.AUGUST, 19),
            LocalTime.of(1, 23, 45),
            ZoneOffset.UTC
    );

    private static final ZonedDateTime ZDT3 = ZonedDateTime.of(
            LocalDate.of(2015, Month.DECEMBER, 1),
            LocalTime.of(23, 45, 54, 321_000_000),
            ZoneOffset.UTC
    );

    private static final ZonedDateTime ZDT4 = ZonedDateTime.of(
            LocalDate.of(2045, Month.MARCH, 7),
            LocalTime.of(10, 11, 12),
            ZoneOffset.UTC
    );

    private static final long MIN_PER_YEAR = 365 * 24 * 60;

    @Test
    void j2000StartsAtCorrectDate() {
        assertEquals(0, Epoch.J2000.daysUntil(ZDT_J2000));
    }

    @Test
    void j2010StartsAtCorrectDate() {
        assertEquals(0, Epoch.J2010.daysUntil(ZDT_J2010));
    }

    @Test
    void daysUntilCorrectlyHandlesTimeZones() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var zdt = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var expectedDays = Epoch.J2010.daysUntil(zdt);
            for (int j = 0; j < 10; j++) {
                var secOffset = rng.nextInt(-64800, 64800);
                var zoneOffset = ZoneOffset.ofTotalSeconds(secOffset);
                var zdt2 = zdt.withZoneSameInstant(zoneOffset);
                var actualDays = Epoch.J2010.daysUntil(zdt2);
                assertEquals(expectedDays, actualDays);
            }
        }
    }

    @Test
    void daysUntilWorksWithKnownValues() {
        assertEquals(-10689.97574074074, Epoch.J2000.daysUntil(ZDT1), 1e-9);
        assertEquals(-135.44184027777777, Epoch.J2000.daysUntil(ZDT2), 1e-9);
        assertEquals(5813.490212048611, Epoch.J2000.daysUntil(ZDT3), 1e-9);
        assertEquals(16501.924444444445, Epoch.J2000.daysUntil(ZDT4), 1e-9);
        assertEquals(-14341.47574074074, Epoch.J2010.daysUntil(ZDT1), 1e-9);
        assertEquals(-3786.941840277778, Epoch.J2010.daysUntil(ZDT2), 1e-9);
        assertEquals(2161.9902120486113, Epoch.J2010.daysUntil(ZDT3), 1e-9);
        assertEquals(12850.424444444445, Epoch.J2010.daysUntil(ZDT4), 1e-9);
    }

    @Test
    void julianCenturiesUntilCorrectlyHandlesTimeZones() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var zdt = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var expectedJulianCenturies = Epoch.J2000.julianCenturiesUntil(zdt);
            for (int j = 0; j < 10; j++) {
                var secOffset = rng.nextInt(-64800, 64800);
                var zoneOffset = ZoneOffset.ofTotalSeconds(secOffset);
                var zdt2 = zdt.withZoneSameInstant(zoneOffset);
                var actualJulianCenturies = Epoch.J2000.julianCenturiesUntil(zdt2);
                assertEquals(expectedJulianCenturies, actualJulianCenturies);
            }
        }
    }

    @Test
    void julianCenturiesUntilWorksWithKnownValues() {
        assertEquals(-0.29267558496210105, Epoch.J2000.julianCenturiesUntil(ZDT1), 1e-9);
        assertEquals(-0.0037081954901513428, Epoch.J2000.julianCenturiesUntil(ZDT2), 1e-9);
        assertEquals(0.15916468753042057, Epoch.J2000.julianCenturiesUntil(ZDT3), 1e-9);
        assertEquals(0.4517980682941669, Epoch.J2000.julianCenturiesUntil(ZDT4), 1e-9);
        assertEquals(-0.39264820645422976, Epoch.J2010.julianCenturiesUntil(ZDT1), 1e-9);
        assertEquals(-0.10368081698228003, Epoch.J2010.julianCenturiesUntil(ZDT2), 1e-9);
        assertEquals(0.05919206603829189, Epoch.J2010.julianCenturiesUntil(ZDT3), 1e-9);
        assertEquals(0.3518254468020382, Epoch.J2010.julianCenturiesUntil(ZDT4), 1e-9);
    }

}
