package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Closed Interval [low, high]
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class ClosedInterval extends Interval {

    private ClosedInterval(double lowerExtrema, double higherExtrema) {
        super(lowerExtrema, higherExtrema);
    }

    /**
     * @param v
     * @return true if in interval else false
     */
    public boolean contains(double v) {
        return (this.low()<=v && v<=this.high());
    }


    /**
     * @throws IllegalArgumentException if high <= low
     * @param low
     * @param high
     * @return a new interval
     */
    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * @throws IllegalArgumentException if size <= 0
     * @param size
     * @return a new symmetric interval centered in 0.
     */
    public static ClosedInterval symmetric(double size){
        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size/2,size/2);
    }


    /**
     * @param v
     * @return value clipped to interval
     */
    public double clip(double v){
        if(v >= this.high()){
            return this.high();
        }
        else if(v <= this.low()){
            return this.low();
        }
        else{
            return v;
        }
    }

    /**
     * @return string representation of interval
     */
    public String toString(){
        return String.format(Locale.ROOT,
                "[%f,%f]",
                this.low(),
                this.high());
    }





}