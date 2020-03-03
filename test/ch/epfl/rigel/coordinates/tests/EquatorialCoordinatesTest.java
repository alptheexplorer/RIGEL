package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquatorialCoordinatesTest {
    @Test
    void equOfWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(ra, c.ra(), 1e-8);
            assertEquals(dec, c.dec(), 1e-8);
        }
    }

    @Test
    void equOfFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(2d * PI + 1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(-1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, PI + 1e-8);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, -(PI + 1e-8));
        });
    }

    @Test
    void raDegAndDecDegReturnCoordinatesInDegrees() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(Math.toDegrees(ra), c.raDeg(), 1e-8);
            assertEquals(Math.toDegrees(dec), c.decDeg(), 1e-8);
        }
    }

    @Test
    void raHrReturnsRightAscensionInHours() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(Math.toDegrees(ra) / 15d, c.raHr(), 1e-8);
        }
    }

    @Test
    void equEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = EquatorialCoordinates.of(0, 0);
            c.equals(c);
        });
    }

    @Test
    void equHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EquatorialCoordinates.of(0, 0).hashCode();
        });
    }
}