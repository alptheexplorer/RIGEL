// Rigel stage 1

package ch.epfl.rigel.math;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

public class ClosedIntervalTest {
    @Test
    void ofWorksWithValidValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var low = rng.nextDouble();
            var size = rng.nextDouble(Double.MIN_VALUE,500);
            var interval = ClosedInterval.of(low, low + size);
            assertEquals(low, interval.low(), 1e-6);
            assertEquals(low + size, interval.high(), 1e-6);
            assertEquals(size, interval.size(), 1e-6);
        }
    }

    @Test
    void ofFailsOnTrivialIntervals() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lowAndHigh = rng.nextDouble();
            assertThrows(IllegalArgumentException.class, () -> {
                ClosedInterval.of(lowAndHigh, lowAndHigh);
            });
        }
    }

    @Test
    void ofFailsOnInvalidIntervals() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var v1 = rng.nextDouble();
            var v2 = rng.nextDouble();
            assertThrows(IllegalArgumentException.class, () -> {
                ClosedInterval.of(max(v1, v2), min(v1, v2));
            });
        }
    }

    @Test
    void symmetricWorksWithValidValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var size = rng.nextDouble(Double.MIN_VALUE,500);
            var interval = ClosedInterval.symmetric(size);
            assertEquals(-size / 2d, interval.low(), 1e-6);
            assertEquals(size / 2d, interval.high(), 1e-6);
            assertEquals(size, interval.size(), 1e-6);
        }
    }

    @Test
    void symmetricFailsWithNegativeSize() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var size = -rng.nextDouble(Double.MIN_VALUE,10);
            assertThrows(IllegalArgumentException.class, () -> {
                ClosedInterval.symmetric(size);
            });
        }
    }

    @Test
    void symmetricFailsOnTrivialInterval() {
        assertThrows(IllegalArgumentException.class, () -> {
            ClosedInterval.symmetric(0);
        });
    }

    @Test
    void containWorksOnNonSymmetricInterval() {
        var interval = ClosedInterval.of(-5, -2);
        assertTrue(interval.contains(-5));
        assertTrue(interval.contains(-2));
        assertTrue(interval.contains(-3));

        assertFalse(interval.contains(-6));
        assertFalse(interval.contains(0));
    }

    @Test
    void containWorksOnSymmetricInterval() {
        var interval = ClosedInterval.symmetric(9);
        assertTrue(interval.contains(-4.5));
        assertTrue(interval.contains(4.5));
        assertTrue(interval.contains(0));

        assertFalse(interval.contains(4.6));
        assertFalse(interval.contains(12));
        assertFalse(interval.contains(-9));
    }

    @Test
    void clipWorksOnNonSymmetricInterval() {
        var interval = ClosedInterval.of(15, 25);
        assertEquals(15, interval.clip(15));
        assertEquals(25, interval.clip(25));
        assertEquals(15, interval.clip(3));
        assertEquals(25, interval.clip(35));
        assertEquals(20, interval.clip(20));
    }

    @Test
    void clipWorksOnSymmetricInterval() {
        var interval = ClosedInterval.symmetric(5);
        assertEquals(-2.5, interval.clip(-2.5));
        assertEquals(-2.5, interval.clip(-30));
        assertEquals(0, interval.clip(0));
        assertEquals(2.5, interval.clip(2.5));
        assertEquals(2, interval.clip(2));
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var interval = ClosedInterval.symmetric(1);
            interval.equals(interval);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            ClosedInterval.symmetric(1).hashCode();
        });
    }
}
