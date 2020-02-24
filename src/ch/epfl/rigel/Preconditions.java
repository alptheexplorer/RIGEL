package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;



/**
 * @author Alp Ozen
 */
public final class Preconditions {
    private Preconditions(){};

    /**
     *
     * @param isTrue
     *
     */
    public static void checkArgument(boolean isTrue){
        if(isTrue == false) {
            throw new IllegalArgumentException();
        }
    }

    /**
     *
     * @param interval
     * @param value
     * @return value if in range else exception
     * @throws IllegalArgumentException
     */

    public static double checkInInterval(Interval interval, double value){
        if(value <= interval.high() && value >= interval.low()){
            return value;
        }
        else{
            throw new IllegalArgumentException();
        }
    }


}