package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SiderealTimeTest {
    // Convert an angle given in HMS to radians (not available in Angle).
    private static double hmsToRad(int h, int m, double s) {
        assert 0 <= h && 0 <= m && m < 60 && 0 <= s && s < 60;
        var hr = h + (m / 60d) + (s / 3600d);
        return Math.toRadians(hr * 15d);
    }

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

    private static final Duration SIDEREAL_DAY = Duration.ofSeconds((23 * 60 + 56) * 60 + 4);

    private static final long MIN_PER_YEAR = 365 * 24 * 60;

    @Test
    void greenwichWorksOnBookExample() {
        // PACS4, §12, p. 23
        var when = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneOffset.UTC);
        // The book gives the result with 2 decimals for the seconds, use that as a delta.
        assertEquals(
                hmsToRad(4, 40, 5.23),
                SiderealTime.greenwich(when),
                hmsToRad(0, 0, 0.01 / 2));
    }

    @Test
    void greewichSiderealDayHasCorrectLength() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var siderealDays = rng.nextInt(-100, 100);
            var zdt1 = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var zdt2 = zdt1.plus(SIDEREAL_DAY.multipliedBy(siderealDays));
            var gst1 = SiderealTime.greenwich(zdt1);
            var gst2 = SiderealTime.greenwich(zdt2);
            assertEquals(gst1, gst2, 1e-3);
        }
    }

    @Test
    void greenwichCorrectlyHandlesTimeZones() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var zdt = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var expectedGST = SiderealTime.greenwich(zdt);
            for (int j = 0; j < 10; j++) {
                var secOffset = rng.nextInt(-64800, 64800);
                var zoneOffset = ZoneOffset.ofTotalSeconds(secOffset);
                var zdt2 = zdt.withZoneSameInstant(zoneOffset);
                var actualGST = SiderealTime.greenwich(zdt2);
                assertEquals(expectedGST, actualGST);
            }
        }
    }

    @Test
    void localWorksOnBookExample() {
        // PACS4, §14, p. 27
        var when = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51, 670_000_000),
                ZoneOffset.UTC);
        var where = GeographicCoordinates.ofDeg(-64, 47);
        // The book gives the result with 2 decimals for the seconds, use that as a delta.
        assertEquals(
                hmsToRad(0, 24, 5.23),
                SiderealTime.local(when, where),
                hmsToRad(0, 0, 0.01 / 2));
    }

    @Test
    void localSiderealDayHasCorrectLength() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var geoLonDeg = rng.nextDouble(-180, 180);
            var geoLatDeg = rng.nextDouble(-90, 90);
            var where = GeographicCoordinates.ofDeg(geoLonDeg, geoLatDeg);
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var siderealDays = rng.nextInt(-100, 100);
            var zdt1 = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var zdt2 = zdt1.plus(SIDEREAL_DAY.multipliedBy(siderealDays));
            var gst1 = SiderealTime.local(zdt1, where);
            var gst2 = SiderealTime.local(zdt2, where);
            assertEquals(gst1, gst2, 1e-3);
        }
    }

    @Test
    void localCorrectlyHandlesTimeZones() {
        var where = GeographicCoordinates.ofDeg(-64, 47);
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var minDiff = rng.nextLong(-MIN_PER_YEAR, MIN_PER_YEAR);
            var zdt = ZDT_SEMESTER_START.plusMinutes(minDiff);
            var expectedLST = SiderealTime.local(zdt, where);
            for (int j = 0; j < 10; j++) {
                var secOffset = rng.nextInt(-64800, 64800);
                var zoneOffset = ZoneOffset.ofTotalSeconds(secOffset);
                var zdt2 = zdt.withZoneSameInstant(zoneOffset);
                var actualLST = SiderealTime.local(zdt2, where);
                assertEquals(expectedLST, actualLST);
            }
        }
    }


    @Test
    void localWorksOnKnownValues() {
        var GC1 = GeographicCoordinates.ofDeg(-64, 47);
        var GC2 = GeographicCoordinates.ofDeg(+64, -47);
        var GC3 = GeographicCoordinates.ofDeg(0, -12.1);
        var GC4 = GeographicCoordinates.ofDeg(0, 0);
        assertEquals(2.2453233976477964, SiderealTime.local(ZDT1, GC1), 1e-9);
        assertEquals(0.9058298133785208, SiderealTime.local(ZDT2, GC2), 1e-9);
        assertEquals(1.1691660434672428, SiderealTime.local(ZDT3, GC3), 1e-9);
        assertEquals(5.55606471152322, SiderealTime.local(ZDT4, GC4), 1e-9);
    }
}
