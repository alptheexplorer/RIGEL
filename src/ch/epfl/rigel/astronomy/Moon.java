package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.*;
import java.util.Locale;

/**
 * represents the Moon at a given instant
 */
public final class Moon extends CelestialObject {

    private final float phase;

    /**
     * name is "Lune"
     *
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     * @param phase         must be in [0,1]
     * @throws IllegalArgumentException if not in specified phase
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        ClosedInterval interval = ClosedInterval.of(0, 1);
        this.phase = (float)Preconditions.checkInInterval(interval, phase);
    }


    //TODO: check this one: if phase = 0.3752, print " Lune (37.5%) "
    //TODO: ask if we need to simply cut it or to approximate it:
    // what if it was 0.3757 ? 37.5% or 37.6% ?
    @Override
    /**
     * gives phase in percentage to the to one decimal place
     */
    public String info() {
        double percentage = phase * 100;
        return String.format(Locale.ROOT, "Lune (%.1f", percentage) + "%)";
    }

}

