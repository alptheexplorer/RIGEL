package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoonTest {
    @Test
    void moonConstructorFailsWhenEquatorialPositionIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Moon(null, 0, 0, 0);
        });
    }

    @Test
    void moonConstructorFailsWhenAngularSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            var equ = EquatorialCoordinates.of(0, 0);
            new Moon(equ, -0.1f, 0, 0);
        });
    }

    @Test
    void moonConstructorFailsWhenPhaseIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            var equ = EquatorialCoordinates.of(0, 0);
            new Moon(equ, 0f, 0, -0.00001f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            var equ = EquatorialCoordinates.of(0, 0);
            new Moon(equ, 0f, 0, +1.00001f);
        });
    }

    @Test
    void moonNameIsCorrect() {
        var equ = EquatorialCoordinates.of(0, 0);
        var s = new Moon(equ, 0, 0, 0);
        assertEquals("Lune", s.name());
    }

    @Test
    void moonAngularSizeIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(0, Math.PI);
            var m = new Moon(equ, angularSize, 0, 0);
            assertEquals(angularSize, m.angularSize());
        }
    }

    @Test
    void moonMagnitudeIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var magnitude = (float) rng.nextDouble(-30, 30);
            var m = new Moon(equ, 0, magnitude, 0);
            assertEquals(magnitude, m.magnitude());
        }
    }

    @Test
    void moonEquatorialPosIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * Math.PI);
            var dec = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
            var equ = EquatorialCoordinates.of(ra, dec);
            var m = new Moon(equ, 0, 0, 0);
            assertEquals(ra, m.equatorialPos().ra());
            assertEquals(dec, m.equatorialPos().dec());
        }
    }

    @Test
    void moonInfoIncludesPhase() {
        var equ = EquatorialCoordinates.of(0, 0);
        var m1 = new Moon(equ, 0, 0, 0.1f);
        var m2 = new Moon(equ, 0, 0, 0.9f);
        assertEquals(m1.name(), m2.name());
        assertNotEquals(m1.info(), m2.info());
    }

    @Test
    void moonHashCodeIsInheritedFromObject() {
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var m = new Moon(equ, 0, 0, 0);
            assertEquals(System.identityHashCode(m), m.hashCode());
        }
    }

    @Test
    void moonEqualsIsBasedOnIdentity() {
        var prevMoon = (Moon)null;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var m = new Moon(equ, 0, 0, 0);
            assertEquals(m, m);
            assertNotEquals(m, prevMoon);
            prevMoon = m;
        }
    }

}
