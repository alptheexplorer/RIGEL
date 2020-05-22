package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Right Open Interval [low, high[
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class RightOpenInterval extends Interval {


    private RightOpenInterval(double lowerExtrema, double higherExtrema) {
        super(lowerExtrema, higherExtrema);
    }


    /**
     * @param low
     * @param high
     * @return a new interval
     * @throws IllegalArgumentException if high <= low
     */
    public static RightOpenInterval of(double low, double high){
        Preconditions.checkArgument(high > low );
        return new RightOpenInterval(low, high);
    }

    /**
     * @param size
     * @return a new symmetric interval centered in 0
     * @throws IllegalArgumentException if size <= 0
     */
    public static RightOpenInterval symmetric(double size){
        Preconditions.checkArgument(size > 0);
        return new RightOpenInterval(-size/2,size/2);
    }

    /**
     * @param v
     * @return true if inside right open interval
     */
    @Override
    public boolean contains(double v) {
        return (this.low()<=v && v<this.high());
    }

    /**
     *
     * @param v
     * @return value reduced to interval
     */
    public double reduce(double v){
        return this.low() + florMod(v-this.low(), this.high() - this.low());

    }

    /**
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