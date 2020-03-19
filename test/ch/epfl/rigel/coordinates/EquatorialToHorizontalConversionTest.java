package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class EquatorialToHorizontalConversionTest {
    private static final GeographicCoordinates EPFL =
            GeographicCoordinates.ofDeg(6.57, 46.52);

    private static final ZonedDateTime ZDT_SEMESTER_START = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(13, 15),
            ZoneOffset.ofHours(1));

    @Test
    void e2hApplyWorksOnBookExample() {
        // PACS4, §25, p. 47
        var where = GeographicCoordinates.ofDeg(0, 52);
        var when = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 6),
                LocalTime.of(17, 0),
                ZoneOffset.UTC);
        var equToHor = new EquatorialToHorizontalConversion(when, where);
        var starEquPos = EquatorialCoordinates.of(
                5.7936855654392385,
                Angle.ofDMS(23, 13, 10));
        var starHorPos = equToHor.apply(starEquPos);
        assertEquals(
                Angle.ofDMS(283, 16, 15.70),
                starHorPos.az(),
                Angle.ofDMS(0, 0, 0.01 / 2d));
        assertEquals(
                Angle.ofDMS(19, 20, 3.64),
                starHorPos.alt(),
                Angle.ofDMS(0, 0, 0.01 / 2d));
    }


    @Test
    void e2hApplyWorksOnKnownValues() {
        var conversion = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL);

        var ecl1 = conversion.apply(EquatorialCoordinates.of(4.9541, -1.4153));
        var ecl2 = conversion.apply(EquatorialCoordinates.of(3.1282, +1.0420));
        var ecl3 = conversion.apply(EquatorialCoordinates.of(5.8611, -1.1461));
        var ecl4 = conversion.apply(EquatorialCoordinates.of(4.6253, +0.7497));
        var ecl5 = conversion.apply(EquatorialCoordinates.of(3.9206, -0.6974));

        assertEquals(3.3066186595315328, ecl1.az(), 1e-9);
        assertEquals(-0.712006491500507, ecl1.alt(), 1e-9);
        assertEquals(6.0837767698593845, ecl2.az(), 1e-9);
        assertEquals(0.3094716108610624, ecl2.alt(), 1e-9);
        assertEquals(3.1528850649053615, ecl3.az(), 1e-9);
        assertEquals(-0.3873294393853973, ecl3.alt(), 1e-9);
        assertEquals(5.127316217769322, ecl4.az(), 1e-9);
        assertEquals(0.7048208908932408, ecl4.alt(), 1e-9);
        assertEquals(4.400817599512725, ecl5.az(), 1e-9);
        assertEquals(-0.7328787267995615, ecl5.alt(), 1e-9);
    }

    @Test
    void e2hEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL);
            c.equals(c);
        });
    }

    @Test
    void e2hHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL).hashCode();
        });
    }
}
