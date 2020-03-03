package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HorizontalCoordinatesTest {
    @Test
    void horOfWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var az = rng.nextDouble(0, 2d * PI);
            var alt = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = HorizontalCoordinates.of(az, alt);
            assertEquals(az, c.az(), 1e-8);
            assertEquals(alt, c.alt(), 1e-8);
        }
    }

    @Test
    void horOfFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(-1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(2d * PI + 1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(0, -(PI + 1e-8));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(0, PI + 1e-8);
        });
    }

    @Test
    void horOfDegWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var azDeg = rng.nextDouble(0, 360);
            var altDeg = rng.nextDouble(-90, 90);
            var c = HorizontalCoordinates.ofDeg(azDeg, altDeg);
            assertEquals(azDeg, c.azDeg(), 1e-8);
            assertEquals(altDeg, c.altDeg(), 1e-8);
        }
    }

    @Test
    void horOfDegFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(-0.0001, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(360, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, -90.0001);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, 90.0001);
        });
    }

    @Test
    void horLonLatReturnValuesInRadians() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var azDeg = rng.nextDouble(0, 360);
            var altDeg = rng.nextDouble(-90, 90);
            var c = HorizontalCoordinates.ofDeg(azDeg, altDeg);
            var az = Math.toRadians(azDeg);
            var alt = Math.toRadians(altDeg);
            assertEquals(az, c.lon(), 1e-8);
            assertEquals(alt, c.lat(), 1e-8);
        }
    }

    @Test
    public void azOctantNameCorrectlyCyclesThroughValues() {
        String n = "north", e = "east", s = "south", w = "west";
        var expected = new ArrayList<String>();
        expected.addAll(Collections.nCopies(45, n));
        expected.addAll(Collections.nCopies(45, n + e));
        expected.addAll(Collections.nCopies(45, e));
        expected.addAll(Collections.nCopies(45, s + e));
        expected.addAll(Collections.nCopies(45, s));
        expected.addAll(Collections.nCopies(45, s + w));
        expected.addAll(Collections.nCopies(45, w));
        expected.addAll(Collections.nCopies(45, n + w));
        Collections.rotate(expected, -22);

        for (var azDeg = 0; azDeg < 360; ++azDeg) {
            var c = HorizontalCoordinates.ofDeg(azDeg, 0);
            assertEquals(expected.get(azDeg), c.azOctantName(n, e, s, w));
        }
    }

    @Test
    public void angularDistanceToWorksAtHorizon() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var azDeg1 = rng.nextDouble(360);
            var azDeg2 = rng.nextDouble(360);
            var azDiffDeg = Math.abs(azDeg1 - azDeg2);
            if (azDiffDeg > 180)
                azDiffDeg = 360 - azDiffDeg;
            if (azDiffDeg < 1e-6) {
                // Avoid very small differences, for which our formula is not very stable
                continue;
            }
            var c1 = HorizontalCoordinates.ofDeg(azDeg1, 0);
            var c2 = HorizontalCoordinates.ofDeg(azDeg2, 0);
            assertEquals(azDiffDeg, Math.toDegrees(c1.angularDistanceTo(c2)), 1e-8);
        }
    }

    @Test
    public void angularDistanceToWorksOnMeridians() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var azDeg = rng.nextDouble(360);
            var altDeg1 = rng.nextDouble(-90, 90);
            var altDeg2 = rng.nextDouble(-90, 90);
            var altDiffDeg = Math.abs(altDeg1 - altDeg2);
            if (altDiffDeg < 1e-6) {
                // Avoid very small differences, for which our formula is not very stable
                continue;
            }
            var c1 = HorizontalCoordinates.ofDeg(azDeg, altDeg1);
            var c2 = HorizontalCoordinates.ofDeg(azDeg, altDeg2);
            assertEquals(altDiffDeg, Math.toDegrees(c1.angularDistanceTo(c2)), 1e-8);
        }
    }

    @Test
    public void angularDistanceToIsCommutative() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var azDeg1 = rng.nextDouble(360);
            var azDeg2 = rng.nextDouble(360);
            var altDeg1 = rng.nextDouble(-90, 90);
            var altDeg2 = rng.nextDouble(-90, 90);
            var c1 = HorizontalCoordinates.ofDeg(azDeg1, altDeg1);
            var c2 = HorizontalCoordinates.ofDeg(azDeg2, altDeg2);
            assertEquals(0, c1.angularDistanceTo(c2) - c2.angularDistanceTo(c1), 1e-8);
        }
    }


    @Test
    public void angularDistanceToWorksOnKnownExample() {
        var c1 = HorizontalCoordinates.ofDeg(5, 17);
        var c2 = HorizontalCoordinates.ofDeg(18, -22);
        assertEquals(0.7160252718946277, c1.angularDistanceTo(c2), 1e-8);
    }

    @Test
    void horEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = HorizontalCoordinates.ofDeg(0, 0);
            c.equals(c);
        });
    }

    @Test
    void horHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            HorizontalCoordinates.ofDeg(0, 0).hashCode();
        });
    }
}
