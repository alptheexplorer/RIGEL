package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates {

    private static RightOpenInterval AZIMUT = RightOpenInterval.of(0, 360);
    private static ClosedInterval HEIGHT = ClosedInterval.symmetric(180);
    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);

    private EquatorialCoordinates(double l, double la) {
        super(l, la);
    }


    /**
     *
     * @param ra
     * @param dec
     * @return EquatorialCoordinate object
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(Preconditions.checkInInterval(azimutRad, ra), Preconditions.checkInInterval(heightRad, dec));
    }

    /**
     *
     * @param raDeg
     * @param decDeg
     * @return EquatorialCoordinate object with degrees input
     */
    public static EquatorialCoordinates ofDeg(double raDeg, double decDeg) {
        return new EquatorialCoordinates(Angle.ofDeg(Preconditions.checkInInterval(AZIMUT, raDeg)),
                Angle.ofDeg(Preconditions.checkInInterval(HEIGHT, decDeg)));
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
        return (super.lonDeg() * (12.0 / 180));
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
