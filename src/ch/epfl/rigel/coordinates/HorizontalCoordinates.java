package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Horizontal Coordinates, a kind of spherical coordinates
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    //Bounds of az and alt
    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * @param az
     * @param alt
     * @return accepts arguments in radians and return horizontalcoordinate object
     */
    public static HorizontalCoordinates of(double az, double alt) {

        return new HorizontalCoordinates(Preconditions.checkInInterval(azimutRad, az),
                Preconditions.checkInInterval(heightRad, alt));
    }

    /**
     * @param az azimut in degrees [0,360[
     * @param alt altitude in degrees [-90,90]
     * @return accepts arguments in degrees and return horizontalcoordinate object
     * @throws IllegalArgumentException if arguments not in intervals
     */
    public static HorizontalCoordinates ofDeg(double az, double alt) {
        double azRad = Preconditions.checkInInterval(azimutRad, Angle.ofDeg(az));
        double altRad = Preconditions.checkInInterval(heightRad, Angle.ofDeg(alt));
        return new HorizontalCoordinates(azRad, altRad);
    }

    /**
     * @return longitude
     */
    public double az() {
        return super.lon();
    }

    /**
     * @return longitude in degrees
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * @return latitude
     */
    public double alt() {
        return super.lat();
    }

    /**
     * @return latitude in degrees
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * @param n north
     * @param e east
     * @param s south
     * @param w west
     * @return the corresponding octant
     */
    public String azOctantName(String n, String e, String s, String w) {
        //this interval is easier to use because we start at the north-west bound of the north octant
        //then we can just add 45 degrees 7 times to have all the octants
        RightOpenInterval shiftedInterval = RightOpenInterval.of(-22.5, 337.5);
        //azimut reduced to [-22.5,337.5[ (i.e. shifted [0,360[ by half an octave)
        double azimut = shiftedInterval.reduce(this.azDeg());
        //creates the 8 octaves as intervals of 45 degrees starting from north and checks in which we are
        int octantNumber = 0;
        do {
            RightOpenInterval octN = RightOpenInterval.of(-22.5 + octantNumber*45.0,  22.5 + octantNumber*45.0);
            if (octN.contains(azimut)) {
                switch (octantNumber) {
                    case 0:
                        return n;
                    case 1:
                        return n + e;
                    case 2:
                        return e;
                    case 3:
                        return s + e;
                    case 4:
                        return s;
                    case 5:
                        return s + w;
                    case 6:
                        return w;
                    case 7:
                        return n + w;
                }
            }
            ++octantNumber;
        } while(octantNumber < 8);
        return null; //should never return this
    }

    /**
     * @param that
     * @return angular distance between current object (this) and that
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double result = Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt()) * Math.cos(that.alt()) * Math.cos(this.az() - that.az()));
        return result;
    }

    @Override
    public String toString() {

        return "(az=" + String.format(Locale.ROOT, "%.4f", this.azDeg()) + "°" + "," + " " + "alt=" + String.format(Locale.ROOT, "%.4f", this.altDeg()) + "°)";
    }


}
