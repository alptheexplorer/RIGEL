package ch.epfl.rigel.math;

import java.util.Locale;

public final class ClosedInterval extends Interval {

    private ClosedInterval(double lowerExtrema, double higherExtrema) {
        super(lowerExtrema, higherExtrema);
    }

    /**
     *
     * @param v
     * @return true if in interval else no
     */
    public boolean contains(double v) {
        return (v>= this.low() && v<= this.high());
    }


    /**
     *
     * @param low
     * @param high
     * @return a new interval
     */
    public static ClosedInterval of(double low, double high){
        if(high > low){
            return new ClosedInterval(low, high);
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
    public static ClosedInterval symmetric(double size){
        if(size > 0){
            return new ClosedInterval(-size/2,size/2);
        }
        else{
            throw new IllegalArgumentException();
        }
    }


    /**
     *
     * @param v
     * @return clipped values
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
     *
     * @return string representation of interval
     */
    public String toString(){
        return String.format(Locale.ROOT,
                "[%f,%f]",
                this.low(),
                this.high());

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

    private double florMod(double x, double y){
        return x- y*Math.floor(x/y);
    }




}