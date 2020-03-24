package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetTest {
    @Test
    void planetConstructorFailsWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            var eqPos = EquatorialCoordinates.of(0, 0);
            new Planet(null, eqPos, 0, 0);
        });
    }

    @Test
    void planetConstructorFailsWhenEquatorialPositionIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Planet("Planet B", null, 0, 0);
        });
    }

    @Test
    void planetConstructorFailsWhenAngularSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            var eqPos = EquatorialCoordinates.of(0, 0);
            new Planet("Planet B", eqPos, -0.1f, 0);
        });
    }

    private static String randomPlanetName(SplittableRandom rng) {
        var vowels = "aeiouy";
        var consonants = "bcdfghjklmnpqrstvwxz";
        var nameLen = rng.nextInt(2, 8);
        var nameBuilder = new StringBuilder(nameLen);
        for (int i = 0; i < nameLen; i++) {
            var actualAlphabet = ((i % 2) == 0) ? vowels : consonants;
            if (i == 0)
                actualAlphabet = actualAlphabet.toUpperCase();
            nameBuilder.append(actualAlphabet.charAt(rng.nextInt(actualAlphabet.length())));
        }
        return nameBuilder.toString();
    }

    @Test
    void planetNameIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var planetName = randomPlanetName(rng);
            var p = new Planet(planetName, equ, 0, 0);
            assertEquals(planetName, p.name());
        }
    }

    @Test
    void planetAngularSizeIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(0, Math.PI);
            var p = new Planet("Planet B", equ, angularSize, 0);
            assertEquals(angularSize, p.angularSize());
        }
    }

    @Test
    void planetMagnitudeIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var magnitude = (float) rng.nextDouble(-30, 30);
            var p = new Planet("Planet B", equ, 0, magnitude);
            assertEquals(magnitude, p.magnitude());
        }
    }

    @Test
    void planetEquatorialPosIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * Math.PI);
            var dec = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
            var equ = EquatorialCoordinates.of(ra, dec);
            var p = new Planet("Planet B", equ, 0, 0);
            assertEquals(ra, p.equatorialPos().ra());
            assertEquals(dec, p.equatorialPos().dec());
        }
    }

    @Test
    void planetInfoIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var planetName = randomPlanetName(rng);
            var p = new Planet(planetName, equ, 0, 0);
            assertEquals(planetName, p.info());
        }
    }

    @Test
    void planetHashCodeIsInheritedFromObject() {
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var p = new Planet("Planet B", equ, 0, 0);
            assertEquals(System.identityHashCode(p), p.hashCode());
        }
    }

    @Test
    void planetEqualsIsBasedOnIdentity() {
        var prevPlanet = (Planet)null;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var p = new Planet("Planet B", equ, 0, 0);
            assertEquals(p, p);
            assertNotEquals(p, prevPlanet);
            prevPlanet = p;
        }
    }

}
