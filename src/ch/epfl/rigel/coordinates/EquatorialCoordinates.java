package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Equatorial Coordinates, a kind of spherical coordinates
 * right ascension[0,360[ corresponds to longitude, declination[-90,90] corresponds to latitude
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);

    private EquatorialCoordinates(double l, double la) {
        super(l, la);
    }


    /**
     *
     * @param ra right ascension
     * @param dec declination
     * @return EquatorialCoordinate object
     * @throws IllegalArgumentException if arguments not in range
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(Preconditions.checkInInterval(azimutRad, ra), Preconditions.checkInInterval(heightRad, dec));
    }


    /**
     *
     * @return longitude
     */
    public double ra() {
        return super.lon();
    }

    /**
     *
     * @return longitude in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     *
     * @return longitude in hours
     */
    public double raHr() {
        return (Angle.toHr(ra()));
    }

    /**
     *
     * @return declination
     */
    public double dec() {
        return super.lat();
    }

    /**
     *
     * @return declination in degrees
     */
    public double decDeg() {
        return super.latDeg();
    }

    /**
     *
     * @return String representation
     */
    @Override
    public String toString() {

        return "(ra=" + String.format(Locale.ROOT, "%.4f",
                this.raHr()) + "°" + "," + " " + "dec=" + String.format(Locale.ROOT,
                "%.4f", this.decDeg()) + "°)";
    }
}
