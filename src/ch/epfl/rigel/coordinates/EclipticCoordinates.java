package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Ecliptic Coordinates, a kind o spherical coordinates
 * ecliptic longitude and ecliptic latitude usually noted respectively lambda and beta
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    //Bounds of l and la
    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);

    private EclipticCoordinates(double l, double la) {
        super(l, la);
    }

    /**
     * @param lon ecliptic longitude (lambda)
     * @param lat ecliptic latitude (beta)
     * @return immutable eclipticCoordinates object, note that arguments are in RADIANS!
     * @throws IllegalArgumentException if arguments out of range
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(Preconditions.checkInInterval(azimutRad, lon),
                Preconditions.checkInInterval(heightRad, lat));
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


    /**
     * @return String representation of object
     */
    @Override
    public String toString() {
        return "(λ=" + String.format(Locale.ROOT, "%.4f", this.lonDeg()) + "°" + "," + " " + "β=" + String.format(Locale.ROOT, "%.4f", this.latDeg()) + "°)";
    }
}
