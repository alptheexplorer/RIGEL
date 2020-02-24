// Rigel stage 1

package ch.epfl.rigel.math;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

class RightOpenIntervalTest {
    @Test
    void ofWorksWithValidValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var low = rng.nextDouble();
            var size = rng.nextDouble(Double.MIN_VALUE,500);
            var interval = RightOpenInterval.of(low, low + size);
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
                RightOpenInterval.of(lowAndHigh, lowAndHigh);
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
                RightOpenInterval.of(max(v1, v2), min(v1, v2));
            });
        }
    }

    @Test
    void symmetricWorksWithValidValues() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var size = rng.nextDouble(Double.MIN_VALUE,500);
            var interval = RightOpenInterval.symmetric(size);
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
                RightOpenInterval.symmetric(size);
            });
        }
    }

    @Test
    void symmetricFailsOnTrivialInterval() {
        assertThrows(IllegalArgumentException.class, () -> {
            RightOpenInterval.symmetric(0);
        });
    }

    @Test
    void containWorksOnNonSymmetricInterval() {
        var interval = RightOpenInterval.of(-5, -2);
        assertTrue(interval.contains(-5));
        assertTrue(interval.contains(-2.0000000001));
        assertTrue(interval.contains(-3));

        assertFalse(interval.contains(-6));
        assertFalse(interval.contains(0));
    }

    @Test
    void containWorksOnSymmetricInterval() {
        var interval = RightOpenInterval.symmetric(9);
        assertTrue(interval.contains(-4.5));
        assertTrue(interval.contains(4.49999999999));
        assertTrue(interval.contains(0));

        assertFalse(interval.contains(4.6));
        assertFalse(interval.contains(12));
        assertFalse(interval.contains(-9));
    }

    @Test
    void reduceWorksOnNonSymmetricInterval() {
        var range = RightOpenInterval.of(-1, 7);
        assertEquals(3, range.reduce(-5));
        assertEquals(0, range.reduce(0));
        assertEquals(6, range.reduce(-10));
        assertEquals(4, range.reduce(100));
        assertEquals(6, range.reduce(6));
    }

    @Test
    void reduceWorksOnSymmetricInterval() {
        var range = RightOpenInterval.symmetric(9);
        assertEquals(3.5, range.reduce(-5.5));
        assertEquals(0, range.reduce(0));
        assertEquals(-1, range.reduce(-10));
        assertEquals(1, range.reduce(100));
        assertEquals(-3, range.reduce(6));
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var interval = RightOpenInterval.symmetric(1);
            interval.equals(interval);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            RightOpenInterval.symmetric(1).hashCode();
        });
    }
}