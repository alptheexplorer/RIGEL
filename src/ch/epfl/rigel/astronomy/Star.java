package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Represents a star
 * <p>
 * final, immutable and public,
 * extends celestial object.
 * Stars in this project are fixed in the equatorial coordinate system,
 * so they don't have a model associated
 * </p> <p>
 * In the theory: a guy in 15th century started categorizing stars (Rigel= Beta Orionis)
 * in 1990 project Hipparcus started to to the same ( Rigel= (HIP) 24436).
 *
 * About Color Index: B-V color index, the lowe the bluer, the higher the more red.
 * from the color index it's possible to deduce the temperature of a star (color temperature T):
 * formula with c for colorIndex and T for color Temperature.
 * </p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Star extends CelestialObject {

    private final int hipparcosId;
    //stocked in attribute for clarity, even though we could just have color Temperature
    private final float colorIndex;
    private final int colorTemperature;
    private static final ClosedInterval INTERVAL_COLOR_INDEX = ClosedInterval.of(-0.5, 5.5);

    /**
     * Constructs a star
     * angular size is considered 0: star is a point in the sky due to distance from Earth
     * @param hipparcosId >= 0
     * @param name
     * @param equatorialPos
     * @param magnitude
     * @param colorIndex [-0.5,5.5]
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos,
                float magnitude, float colorIndex) {

        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);
        this.hipparcosId = hipparcosId;
        this.colorIndex = (float) Preconditions.checkInInterval(INTERVAL_COLOR_INDEX, colorIndex);

        // we can also already calculate color temperature
        double tmp = 0.92*this.colorIndex;
        double T = 4600 * (
                (1 / (tmp + 1.7)) + (1 / (tmp + 0.62))
        );
        colorTemperature = (int) T;
    }


    /**
     * @return Hipparcos number of the star
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * @return Color temperature in Kelvin degrees, rounded by default
     * (i.e. to the closest inferior integer )
     */
    public int colorTemperature() {
        return colorTemperature;
    }

}
