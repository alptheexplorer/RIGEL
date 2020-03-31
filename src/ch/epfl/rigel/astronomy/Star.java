package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Represents a star, final, immutable and public,
 * extends celestial object.
 * Stars in this project are fixed in the equatorial coordinate system,
 * so they don't have a model associated
 *
 * In the theory: a guy in 15th century started categorizing stars (Rigel= Beta Orionis)
 * in 1990 project Hipparcus started to to the same ( Rigel= (HIP) 24436).
 *
 * About Color Index: B-V color index, the lowe the bluer, the higher the more red.
 * from the color index it's possible to deduce the temperature of a star (color temperature T):
 * formula with c for colorIndex and T for color Temperature.
 *
 */
public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final float colorIndex;
    private final ClosedInterval intervalColorIndex = ClosedInterval.of(-0.5,5.5);

    /**
     *
     * @param hipparcosId
     * @param name
     * @param equatorialPos
     * @param magnitude
     * @param colorIndex
     *
     * Constructs a star and throws an IllegalArgumentException if the
     * Hipparcos number is negative, or if the colorIndex is not in [-0.5,5.5]
     *
     * Gives 0 in angular size of Celestial Object: stars are modelled as points
     * due to their distance from earth
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos,
                float magnitude, float colorIndex){

        super(name,equatorialPos,0,magnitude);

        if(hipparcosId <0) {throw new IllegalArgumentException();}
        else{ this.hipparcosId = hipparcosId;}

        if(intervalColorIndex.contains(colorIndex)) {this.colorIndex = colorIndex;}
        else {throw new IllegalArgumentException();}
    }


    /**
     *
     * @return Hipparcos number of the star
     */
    public int hipparcosId(){
        return hipparcosId;
    }

    /**
     *
     * @return Color temperature in Kelvin degrees, rounded by default
     * (i.e. to the closest inferior integer )
     *
     * Formula from text, c is ColorIndex, T is ColorTemperature
     */
    public int colorTemperature(){

        double c = this.colorIndex;

        double T = 4600*(
                (1/(0.92*c + 1.7)) + (1/(0.92*c + 0.62))
                );

        int TRounded = (int) Math.floor(T);

        return TRounded;
    }

}
