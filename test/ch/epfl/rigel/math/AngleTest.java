// Rigel stage 1

package ch.epfl.rigel.math;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AngleTest {

    @Test
    void tauIsDefinedCorrectly() {
        assertEquals(6.283185307179586, Angle.TAU);
    }

    @Test
    void normalizePositiveWorksWithAlreadyNormalizedValues() {
        var normalizedAngles = new double[]{1.999999 * PI, 0, PI, PI / 2d};
        for (double a : normalizedAngles)
            assertEquals(a, Angle.normalizePositive(a));
    }

    @Test
    void normalizePositiveWorksOnWholeTurns() {
        for (int t = -5; t <= 5; t++)
            assertEquals(0, Angle.normalizePositive(2 * PI * t));
    }

    @Test
    void ofArcsecWorksOnKnownValues() {
        assertEquals(0, Angle.ofArcsec(0));
        assertEquals(5.885638088669767E-5, Angle.ofArcsec(12.14), 1e-8);
        assertEquals(-6.205615118202061E-4, Angle.ofArcsec(-128), 1e-8);
        assertEquals(0.017453292519943295, Angle.ofArcsec(3600), 1e-8);
        assertEquals(0.017453244038575184, Angle.ofArcsec(3599.99), 1e-8);
        assertEquals(0.005982600824891674, Angle.ofArcsec(1234), 1e-8);
        assertEquals(-0.017453292519943295, Angle.ofArcsec(-3600), 1e-8);
    }

    @Test
    void ofDMSWorksOnKnownValues() {
        assertEquals(7.27220521664304E-5, Angle.ofDMS(0, 0, 15), 1e-8);
        assertEquals(0.004363323129985824, Angle.ofDMS(0, 15, 0), 1e-8);
        assertEquals(0.2617993877991494, Angle.ofDMS(15, 0, 0), 1e-8);
        assertEquals(-0.25736334261699717, Angle.ofDMS(-15, 15, 15), 1e-8);
        assertEquals(8.726646259971648, Angle.ofDMS(500, 0, 0), 1e-8);
        assertEquals(0.03579864221312814, Angle.ofDMS(2, 3, 4), 1e-8);
        assertEquals(0.035801391106700026, Angle.ofDMS(2, 3, 4.567), 1e-8);
    }

    @Test
    void ofDMSFailsWithInvalidMinutes() {
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(3, -1, 2.4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(1, -5, 7);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(122, -12, 4.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(33, 60, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(9, 65, 14);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(0, 199, 0);
        });
    }

    @Test
    void ofDMSFailsWithInvalidSeconds() {
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(12, 9, -0.0001);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(22, 34, -1.4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(1, 0, -12);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(9, 4, 60.0001);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(9, 4, 75);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(9, 4, 99);
        });
    }

    @Test
    void ofDegIsEquivalentToMathToRadians() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var a = rng.nextDouble();
            assertEquals(Math.toRadians(a), Angle.ofDeg(a));
        }
    }

    @Test
    void toDegIsEquivalentToMathToDegrees() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var a = rng.nextDouble();
            assertEquals(Math.toDegrees(a), Angle.toDeg(a));
        }
    }

    @Test
    void ofHrWorksOnKnownValues() {
        assertEquals(3.141592653589793, Angle.ofHr(12), 1e-8);
        assertEquals(1.112647398146385, Angle.ofHr(4.25), 1e-8);
        assertEquals(12.566370614359172, Angle.ofHr(48), 1e-8);
        assertEquals(-9.306968236259761, Angle.ofHr(-35.55), 1e-8);
        assertEquals(-18.84955592153876, Angle.ofHr(-72), 1e-8);
    }

    @Test
    void toHrWorksOnKnownValues() {
        assertEquals(24, Angle.toHr(2 * PI), 1e-9);
        assertEquals(-12, Angle.toHr(-PI), 1e-9);
        assertEquals(-9, Angle.toHr(-3 * PI / 4), 1e-9);
        assertEquals(-60, Angle.toHr(-5 * PI), 1e-9);
        assertEquals(2, Angle.toHr(PI / 6), 1e-9);
    }
}
