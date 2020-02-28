package ch.epfl.rigel.coordinates;

import java.util.Locale;

public class EclipticCoordinates extends ch.epfl.rigel.coordinates.SphericalCoordinates {
    private EclipticCoordinates(double l, double la) {
        super(l, la);
    }

    /**
     *
     * @param lon
     * @param lat
     * @return immutable eclipticCoordinates object
     */
    public static EclipticCoordinates of(double lon, double lat){
        return new EclipticCoordinates(lon, lat);
    }

    /**
     *
     * @return longitude in radians
     */
    public double lon(){
        return super.lon();
    }

    /**
     *
     * @return longitude in degrees
     */
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     *
     * @return latitude in radians
     */
    public double lat(){
        return super.lat();
    }

    /**
     *
     * @return latitude in degrees
     */
    public double latDeg(){
        return super.latDeg();
    }


    /**
     *
     * @return String representation of object
     */
    @Override
    public String toString(){
        return "(λ=" + String.format(Locale.ROOT,"%.4f",this.lonDeg()) + "°" + "," + " " + "β=" + String.format(Locale.ROOT,"%.4f",this.latDeg()) + "°)";
    }
}
