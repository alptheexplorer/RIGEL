package ch.epfl.rigel.math;

public abstract class Interval {
    private final double lowerExtrema, higherExtrema;

    protected Interval(double lowerExtrema, double higherExtrema) {
        this.lowerExtrema = lowerExtrema;
        this.higherExtrema = higherExtrema;
    }

    /**
     *
     * @return lowerbound of interval
     */
    public double low(){
        return lowerExtrema;
    }

    /**
     *
     *
     * @return upperbound of interval
     */
    public double high(){
        return higherExtrema;
    }

    /**
     *
     *
     * @return size of interval
     */

    public double size(){
        return higherExtrema - lowerExtrema;
    }

    public abstract boolean contains(double v);

    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


}
