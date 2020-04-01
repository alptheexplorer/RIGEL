package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CartesianCoordinatesTest {
    @Test
    void ofAndGettersWork() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var x = rng.nextDouble(-1e6, 1e6);
            var y = rng.nextDouble(-1e6, 1e6);
            var c = CartesianCoordinates.of(x, y);
            assertEquals(x, c.x());
            assertEquals(y, c.y());
        }
    }

    @Test
    void ccEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = CartesianCoordinates.of(0, 0);
            c.equals(c);
        });
    }

    @Test
    void ccHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates.of(0, 0).hashCode();
        });
    }

}
