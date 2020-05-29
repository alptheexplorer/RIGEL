package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Geographical Coordinates, a kind of spherical coordinates
 * Longitude [-180,180[; latitude [-90,90] degrees
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval LONG_INTERVAL = RightOpenInterval.symmetric(360);
    private final static ClosedInterval LAT_INTERVAL = ClosedInterval.symmetric(180);

    private GeographicCoordinates(double l, double la) {
        super(l, la);
    }

    /**
     * @param lonDeg longitude in degrees
     * @param latDeg latitude in degrees
     * @return geographicCoordinates object with longitude and latitude in radians
     * @throws IllegalArgumentException if arguments not in right intervals ( through preconditions)
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions.checkInInterval(LONG_INTERVAL,lonDeg);
        Preconditions.checkInInterval(LAT_INTERVAL,latDeg);
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * @param lonDeg longitude in degrees
     * @return true if entry is valid longitude
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return LONG_INTERVAL.contains(lonDeg);
    }

    /**
     * @param latDeg latitude in degrees
     * @return true if entry is valid latitude
     */
    public static boolean isValidLatDeg(double latDeg) {
        return LAT_INTERVAL.contains(latDeg);
    }

    /**
     * @return longitude in radians
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @return longitude in degrees
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return latitude in radians
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @return latitude in degrees
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return "(lon=" + String.format(Locale.ROOT, "%.4f", this.lonDeg()) + "°" + "," + " " + "lat=" + String.format(Locale.ROOT, "%.4f", this.latDeg()) + "°)";
    }


}
