// Rigel stage 1

package ch.epfl.rigel;

import ch.epfl.rigel.math.ClosedInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PreconditionsTest {
    @Test
    void checkArgumentSucceedsForTrue() {
        Preconditions.checkArgument(true);
    }

    @Test
    void checkArgumentFailsForFalse() {
        assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(false);
        });
    }

    @Test
    void checkInIntervalSucceedsForValuesInInterval() {
        Preconditions.checkInInterval(ClosedInterval.of(0, 100), 50);
    }

    @Test
    void checkInIntervalFailsForValuesNotInInterval() {
        assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkInInterval(ClosedInterval.of(50, 100), 0);
        });
    }
}
