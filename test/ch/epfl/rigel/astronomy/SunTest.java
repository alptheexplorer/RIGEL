package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SunTest {
    @Test
    void sunConstructorFailsWhenOnePositionIsNull() {
        assertThrows(NullPointerException.class, () -> {
            var equ = EquatorialCoordinates.of(0, 0);
            new Sun(null, equ, 0, 0);
        });
        assertThrows(NullPointerException.class, () -> {
            var ecl = EclipticCoordinates.of(0, 0);
            new Sun(ecl, null, 0, 0);
        });
    }

    @Test
    void sunConstructorFailsWhenAngularSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            var ecl = EclipticCoordinates.of(0, 0);
            var equ = EquatorialCoordinates.of(0, 0);
            new Sun(ecl, equ, -0.1f, 0);
        });
    }

    @Test
    void sunNameIsCorrect() {
        var ecl = EclipticCoordinates.of(0, 0);
        var equ = EquatorialCoordinates.of(0, 0);
        var s = new Sun(ecl, equ, 0, 0);
        assertEquals("Soleil", s.name());
    }

    @Test
    void sunAngularSizeIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ecl = EclipticCoordinates.of(0, 0);
            var equ = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(0, Math.PI);
            var s = new Sun(ecl, equ, angularSize, 0);
            assertEquals(angularSize, s.angularSize());
        }
    }

    @Test
    void sunMagnitudeIsCorrect() {
        var ecl = EclipticCoordinates.of(0, 0);
        var equ = EquatorialCoordinates.of(0, 0);
        var s = new Sun(ecl, equ, 0, 0);
        assertEquals(-26.7f, s.magnitude(), 1e-8);
    }

    @Test
    void sunEquatorialPosIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * Math.PI);
            var dec = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
            var equ = EquatorialCoordinates.of(ra, dec);
            var ecl = EclipticCoordinates.of(0, 0);
            var s = new Sun(ecl, equ, 0f, 0);
            assertEquals(ra, s.equatorialPos().ra());
            assertEquals(dec, s.equatorialPos().dec());
        }
    }

    @Test
    void sunInfoIsCorrect() {
        var ecl = EclipticCoordinates.of(0, 0);
        var equ = EquatorialCoordinates.of(0, 0);
        var s = new Sun(ecl, equ, 0, 0);
        assertEquals("Soleil", s.info());
    }

    @Test
    void sunEclipticPosIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var eclLon = rng.nextDouble(0, 2d * Math.PI);
            var eclLat = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
            var ecl = EclipticCoordinates.of(eclLon, eclLat);
            var equ = EquatorialCoordinates.of(0, 0);
            var s = new Sun(ecl, equ, 0f, 0);
            assertEquals(eclLon, s.eclipticPos().lon());
            assertEquals(eclLat, s.eclipticPos().lat());
        }
    }

    @Test
    void sunMeanAnomalyIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var meanAnomaly = (float) rng.nextDouble(0, 2d * Math.PI);
            var ecl = EclipticCoordinates.of(0, 0);
            var equ = EquatorialCoordinates.of(0, 0);
            var s = new Sun(ecl, equ, 0f, meanAnomaly);
            assertEquals(meanAnomaly, s.meanAnomaly());
        }
    }

    @Test
    void sunHashCodeIsInheritedFromObject() {
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ecl = EclipticCoordinates.of(0, 0);
            var equ = EquatorialCoordinates.of(0, 0);
            var s = new Sun(ecl, equ, 0, 0);
            assertEquals(System.identityHashCode(s), s.hashCode());
        }
    }

    @Test
    void sunEqualsIsBasedOnIdentity() {
        var prevSun = (Sun)null;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ecl = EclipticCoordinates.of(0, 0);
            var equ = EquatorialCoordinates.of(0, 0);
            var s = new Sun(ecl, equ, 0, 0);
            assertEquals(s, s);
            assertNotEquals(s, prevSun);
            prevSun = s;
        }
    }
}