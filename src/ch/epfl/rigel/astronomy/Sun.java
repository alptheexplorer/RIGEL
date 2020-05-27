package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.util.Objects;

/**
 * Sun in a given moment
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Sun extends CelestialObject {

    private final float meanAnomaly;
    private final EclipticCoordinates eclipticPos;

    /**
     * @param eclipticPos   celestial didn't have this
     * @param equatorialPos
     * @param angularSize
     * @param meanAnomaly   explained later ( see sunModel )
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos,
               float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, (float) -26.7);
        this.meanAnomaly = meanAnomaly;
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
    }


    /**
     *
     * @return eclipticPos
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     *
     * @return meanAnomaly
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}
