package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeographicCoordinatesTest {
    @Test
    void isValidLonDegWorks() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lonDeg = rng.nextDouble(-180, 180);
            assertTrue(GeographicCoordinates.isValidLonDeg(lonDeg));
        }
        assertFalse(GeographicCoordinates.isValidLonDeg(-180.0001));
        assertFalse(GeographicCoordinates.isValidLonDeg(+180));
    }

    @Test
    void isValidLatDegWorks() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var latDeg = rng.nextDouble(-90, 90);
            assertTrue(GeographicCoordinates.isValidLatDeg(latDeg));
        }
        assertTrue(GeographicCoordinates.isValidLatDeg(90));
        assertFalse(GeographicCoordinates.isValidLatDeg(-90.0001));
        assertFalse(GeographicCoordinates.isValidLatDeg(+90.0001));
    }

    @Test
    void geoOfDegWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lonDeg = rng.nextDouble(-180, 180);
            var latDeg = rng.nextDouble(-90, 90);
            var c = GeographicCoordinates.ofDeg(lonDeg, latDeg);
            assertEquals(lonDeg, c.lonDeg(), 1e-8);
            assertEquals(latDeg, c.latDeg(), 1e-8);
        }
    }

    @Test
    void geoOfDegFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(-180.0001, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(180, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(0, -90.0001);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(0, 90.0001);
        });
    }

    @Test
    void geoLonLatReturnValuesInRadians() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lonDeg = rng.nextDouble(-180, 180);
            var latDeg = rng.nextDouble(-90, 90);
            var c = GeographicCoordinates.ofDeg(lonDeg, latDeg);
            var lon = Math.toRadians(lonDeg);
            var lat = Math.toRadians(latDeg);
            assertEquals(lon, c.lon(), 1e-8);
            assertEquals(lat, c.lat(), 1e-8);
        }
    }

    @Test
    void geoEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = GeographicCoordinates.ofDeg(0, 0);
            c.equals(c);
        });
    }

    @Test
    void geoHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            GeographicCoordinates.ofDeg(0, 0).hashCode();
        });
    }
}