package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EclipticCoordinatesTest {
    @Test
    void eclOfWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(0, 2d * PI);
            var lat = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EclipticCoordinates.of(lon, lat);
            assertEquals(lon, c.lon(), 1e-8);
            assertEquals(lat, c.lat(), 1e-8);
        }
    }

    @Test
    void eclOfFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(2d * PI + 1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(-1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, PI + 1e-8);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, -(PI + 1e-8));
        });
    }

    @Test
    void lonDegAndLatDegReturnCoordinatesInDegrees() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(0, 2d * PI);
            var lat = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EclipticCoordinates.of(lon, lat);
            assertEquals(Math.toDegrees(lon), c.lonDeg(), 1e-8);
            assertEquals(Math.toDegrees(lat), c.latDeg(), 1e-8);
        }
    }

    @Test
    void ecEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = EclipticCoordinates.of(0, 0);
            c.equals(c);
        });
    }

    @Test
    void ecHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EclipticCoordinates.of(0, 0).hashCode();
        });
    }
}