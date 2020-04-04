package ch.epfl.rigel.math;

import java.util.Locale;

/**
 * @author Alp Ozen
 */
public final class RightOpenInterval extends Interval {


    private RightOpenInterval(double lowerExtrema, double higherExtrema) {
        super(lowerExtrema, higherExtrema);
    }


    /**
     *
     * @param low
     * @param high
     * @return a new interval
     */
    public static RightOpenInterval of(double low, double high){
        if(high > low){
            return new RightOpenInterval(low, high);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     *
     * @param size
     * @return a new symmetric interval centered in 0.
     */
    public static RightOpenInterval symmetric(double size){
        if(size > 0){
            return new RightOpenInterval(-size/2,size/2);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     *
     * @param v
     * @return true if inside right open interval
     */

    @Override
    public boolean contains(double v) {
        return (v >= this.low() && v < this.high());
    }

    /**
     *
     * @param v
     * @return reduced form
     *
     */
    public double reduce(double v){
        return this.low() + florMod(v-this.low(), this.high() - this.low());

    }



    /**
     *
     * @return string representation of the interval
     */
    public String toString(){
        return String.format(Locale.ROOT,
                "[%f,%f[",
                this.low(),
                this.high());
    }

    private double florMod(double x, double y){
        return x- y*Math.floor(x/y);
    }



}