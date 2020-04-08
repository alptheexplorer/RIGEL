package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;



/**
 * Useful class to check conditions and throwing exceptions
 */
public final class Preconditions {
    private Preconditions(){};

    /**
     * Checks boolean, if false throws IAException
     * @param isTrue
     * @throws IllegalArgumentException
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
        if(interval.contains(value)){
            return value;
        }
        else{
            throw new IllegalArgumentException();
        }
    }


}