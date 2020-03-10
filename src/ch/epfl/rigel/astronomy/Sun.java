package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.util.Objects;

/**
 * represents Sun in a given moment
 */
public final class Sun extends CelestialObject{

    private final float meanAnomaly;
    private final EclipticCoordinates eclipticPos;

    /**
     *
     * @param eclipticPos celestial didn't have this
     * @param equatorialPos
     * @param angularSize
     * @param meanAnomaly  explained later
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos,
               float angularSize, float meanAnomaly){
        super("Soleil", equatorialPos, angularSize, (float)-26.7);
        this.meanAnomaly = meanAnomaly;
        this.eclipticPos = Objects.requireNonNull(eclipticPos);

    }


    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    public double meanAnomaly(){
        return meanAnomaly;
    }
}
