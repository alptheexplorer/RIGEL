package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StarTest {
    @Test
    void constructorFailsWhenHipparcosNumberIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(-1, "Rigel", EquatorialCoordinates.of(0, 0), 0, 0);
        });
    }

    @Test
    void constructorFailsWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Star(1, null, EquatorialCoordinates.of(0, 0), 0, 0);
        });
    }

    @Test
    void constructorFailsWhenEquatorialPosIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Star(1, "Rigel", null, 0, 0);
        });
    }

    @Test
    void constructorFailsWhenColorIndexIsOutOfBounds() {
        EquatorialCoordinates eqPos = EquatorialCoordinates.of(0, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(1, "Rigel", eqPos, 0, -0.6f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(1, "Rigel", eqPos, 0, +5.6f);
        });
    }

    private static String randomStarName(SplittableRandom rng) {
        var vowels = "aeiouy";
        var consonants = "bcdfghjklmnpqrstvwxz";
        var nameLen = rng.nextInt(3, 10);
        var nameBuilder = new StringBuilder(nameLen);
        for (int i = 0; i < nameLen; i++) {
            var actualAlphabet = ((i % 2) == 0) ? consonants : vowels;
            if (i == 0)
                actualAlphabet = actualAlphabet.toUpperCase();
            nameBuilder.append(actualAlphabet.charAt(rng.nextInt(actualAlphabet.length())));
        }
        return nameBuilder.toString();
    }

    @Test
    void starNameIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomStarName(rng);
            var s = new Star(1, name, EquatorialCoordinates.of(0, 0), 0, 0);
            assertEquals(name, s.name());
        }
    }

    @Test
    void starAngularSizeIs0() {
        var s = new Star(1, "Rigel", EquatorialCoordinates.of(1, 1), 2, 3);
        assertEquals(0, s.angularSize());
    }

    @Test
    void starMagnitudeIsCorrect() {
        EquatorialCoordinates eqPos = EquatorialCoordinates.of(0, 0);
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var m = (float) rng.nextDouble(-30, 30);
            var s = new Star(1, "Rigel", eqPos, m, 0);
            assertEquals(m, s.magnitude());
        }
    }

    @Test
    void starEquatorialPosIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * Math.PI);
            var dec = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
            var eqPos = EquatorialCoordinates.of(ra, dec);
            var s = new Star(1, "Rigel", eqPos, 0, 0);
            assertEquals(ra, s.equatorialPos().ra());
            assertEquals(dec, s.equatorialPos().dec());
        }
    }

    @Test
    void starInfoIsCorrect() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var equ = EquatorialCoordinates.of(0, 0);
            var starName = randomStarName(rng);
            var s = new Planet(starName, equ, 0, 0);
            assertEquals(starName, s.info());
        }
    }

    @Test
    void starHipparcosIdIsCorrect() {
        for (int hip = 0; hip < 10; hip++) {
            EquatorialCoordinates eqPos = EquatorialCoordinates.of(0, 0);
            var s = new Star(hip, "Rigel", eqPos, 0, 0);
            assertEquals(hip, s.hipparcosId());
        }
    }

    private static Star newStarWithColorIndex(float colorIndex) {
        EquatorialCoordinates eqPos = EquatorialCoordinates.of(0, 0);
        return new Star(1, "Rigel", eqPos, 0, colorIndex);
    }


    @Test
    void starColorTemperatureIsCorrect() {
        var s1 = newStarWithColorIndex(-0.5f);
        var s2 = newStarWithColorIndex(0f);
        var s3 = newStarWithColorIndex(+2.0f);
        var s4 = newStarWithColorIndex(+4.0f);
        var s5 = newStarWithColorIndex(+5.5f);

        assertEquals(32459, s1.colorTemperature());
        assertEquals(10125, s2.colorTemperature());
        assertEquals(3169, s3.colorTemperature());
        assertEquals(1924, s4.colorTemperature());
        assertEquals(1490, s5.colorTemperature());
    }
}
