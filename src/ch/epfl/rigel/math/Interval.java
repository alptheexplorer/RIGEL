package ch.epfl.rigel.math;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public abstract class Interval {
    private final double lowerExtrema, higherExtrema;

    protected Interval(double lowerExtrema, double higherExtrema) {
        this.lowerExtrema = lowerExtrema;
        this.higherExtrema = higherExtrema;
    }

    /**
     * @return lowerbound of interval
     */
    public double low(){
        return lowerExtrema;
    }

    /**
     * @return upperbound of interval
     */
    public double high(){
        return higherExtrema;
    }

    /**
     * @return size of interval
     */
    public double size(){
        return higherExtrema - lowerExtrema;
    }

    /**
     *
     * @param v
     * @return true if v is contained in the interval
     */
    abstract public  boolean contains(double v);

    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


}