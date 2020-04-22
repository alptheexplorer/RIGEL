package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;


public final class GeographicCoordinates extends SphericalCoordinates {

    private static RightOpenInterval longitude = RightOpenInterval.of(-180.0, 180.0);
    private static ClosedInterval latitude = ClosedInterval.of(-90.0, 90.0);

    private GeographicCoordinates(double l, double la) {
        super(l, la);
    }

    /**
     * @param lonDeg
     * @param latDeg
     * @return geographicCoordinates object with longitude and latitude in radians
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        if (!longitude.contains(lonDeg) || !latitude.contains(latDeg)) {
            throw new IllegalArgumentException();
        } else {
            return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
        }
    }

    /**
     * @param lonDeg
     * @return true if entry is valid longitude
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return longitude.contains(lonDeg);
    }

    /**
     * @param latDeg
     * @return true if entry is valid latitude
     */
    public static boolean isValidLatDeg(double latDeg) {
        return latitude.contains(latDeg);
    }

    /**
     * @return longitude in radians
     */
    public double lon() {
        return super.lon();
    }

    /**
     * @return longitude in degrees
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return latitude in radians
     */
    public double lat() {
        return super.lat();
    }

    /**
     * @return latitude in degrees
     */
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return "(lon=" + String.format(Locale.ROOT, "%.4f", this.lonDeg()) + "°" + "," + " " + "lat=" + String.format(Locale.ROOT, "%.4f", this.latDeg()) + "°)";
    }


}
