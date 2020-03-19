package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EclipticToEquatorialConversionTest {
    // Convert an angle given in HMS to hours (not available in Angle).
    private static double hmsToHr(int h, int m, double s) {
        assert 0 <= h && 0 <= m && m < 60 && 0 <= s && s < 60;
        return h + (m / 60d) + (s / 3600d);
    }

    // Same, but when using degrees (name is clearer).
    private static double dmsToDeg(int d, int m, double s) {
        assert 0 <= d && 0 <= m && m < 60 && 0 <= s && s < 60;
        return d + (m / 60d) + (s / 3600d);
    }

    private static final ZonedDateTime ZDT_SEMESTER_START = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(13, 15),
            ZoneOffset.ofHours(1));

    @Test
    void e2eApplyWorksOnBookExample() {
        // PACS4, §27, p. 53
        var when = ZonedDateTime.of(
                LocalDate.of(2009, Month.JULY, 6),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC);
        var eclPos = EclipticCoordinates.of(
                Angle.ofDMS(139, 41, 10),
                Angle.ofDMS(4, 52, 31));
        var conversion = new EclipticToEquatorialConversion(when);
        var equPos = conversion.apply(eclPos);
        assertEquals(
                hmsToHr(9, 34, 53.32),
                equPos.raHr(),
                hmsToHr(0, 0, 0.01 / 2d));
        assertEquals(
                dmsToDeg(19, 32, 6.01),
                equPos.decDeg(),
                dmsToDeg(0, 0, 0.01 / 2d));
    }


    @Test
    void e2eApplyWorksFor00() {
        var conversion = new EclipticToEquatorialConversion(ZDT_SEMESTER_START);
        var converted00 = conversion.apply(EclipticCoordinates.of(0, 0));
        assertEquals(0, converted00.ra());
        assertEquals(0, converted00.dec());
    }

    @Test
    void e2eApplyWorksOnKnownValues() {
        var conversion = new EclipticToEquatorialConversion(ZDT_SEMESTER_START);

        var ecl1 = conversion.apply(EclipticCoordinates.of(4.9541, -1.4153));
        var ecl2 = conversion.apply(EclipticCoordinates.of(3.1282, +1.0420));
        var ecl3 = conversion.apply(EclipticCoordinates.of(5.8611, -1.1461));
        var ecl4 = conversion.apply(EclipticCoordinates.of(4.6253, +0.7497));
        var ecl5 = conversion.apply(EclipticCoordinates.of(3.9206, -0.6974));

        assertEquals(1.4264185128557192, ecl1.ra(), 1e-9);
        assertEquals(-1.3102026517802514, ecl1.dec(), 1e-9);
        assertEquals(3.7308460797246488, ecl2.ra(), 1e-9);
        assertEquals(0.9187943476739282, ecl2.dec(), 1e-9);
        assertEquals(0.5044624835494554, ecl3.ra(), 1e-9);
        assertEquals(-1.12700571268378, ecl3.dec(), 1e-9);
        assertEquals(4.644768981025202, ecl4.ra(), 1e-9);
        assertEquals(0.3418235985604023, ecl4.dec(), 1e-9);
        assertEquals(3.5540472307869595, ecl5.ra(), 1e-9);
        assertEquals(-0.9330452897310164, ecl5.dec(), 1e-9);
    }

    @Test
    void e2eEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = new EclipticToEquatorialConversion(ZDT_SEMESTER_START);
            c.equals(c);
        });
    }

    @Test
    void e2eHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new EclipticToEquatorialConversion(ZDT_SEMESTER_START).hashCode();
        });
    }
}
