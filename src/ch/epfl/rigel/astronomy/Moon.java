package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.*;

import java.util.Locale;

/**
 * Moon at a given instant
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Moon extends CelestialObject {

    private final float phase;
    private static final ClosedInterval phaseInterval = ClosedInterval.of(0, 1);

    /**
     * name is "Lune"
     *
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     * @param phase         must be in [0,1]
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float) Preconditions.checkInInterval(phaseInterval, phase);
    }



    /**
     * gives phase in percentage to the to one decimal place
     */
    @Override
    public String info() {
        String name = super.info();
        double percentage = phase * 100;
        return String.format(Locale.ROOT, name + " (%.1f", percentage) + "%)";
    }

}

