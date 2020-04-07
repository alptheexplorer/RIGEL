package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;


public final class HorizontalCoordinates extends
        SphericalCoordinates {

    //Bounds of az and alt, added both in degrees and rad for clarity
    private static RightOpenInterval AZIMUT = RightOpenInterval.of(0,360);
    private static ClosedInterval HEIGHT = ClosedInterval.symmetric(180);
    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     *
     * @param az
     * @param alt
     * @return accepts arguments in radians and return horizontalcoordinate object
     */
    public static HorizontalCoordinates of(double az, double alt){

        return new HorizontalCoordinates(Preconditions.checkInInterval(azimutRad,az),
                Preconditions.checkInInterval(heightRad,alt));
    }

    /**
     *
     * @param az
     * @param alt
     * @return accepts arguments in degrees and return horizontalcoordinate object
     */
    public static HorizontalCoordinates ofDeg(double az, double alt) {
        double azRad = Preconditions.checkInInterval(azimutRad,Angle.ofDeg(az));
        double altRad = Preconditions.checkInInterval(heightRad,Angle.ofDeg(alt));
        return new HorizontalCoordinates(azRad,altRad);
    }

    /**
     *
     * @return longitude
     */
    public double az(){
        return super.lon();
    }

    /**
     *
     * @return longitude in degrees
     */
    public double azDeg(){
        return super.lonDeg();
    }

    /**
     *
     * @return latitude
     */
    public double alt() {
        return super.lat();
    }

    /**
     *
     * @return latitude in degrees
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     *
     * @param n
     * @param e
     * @param s
     * @param w
     * @return the relevant octant
     */
    public String azOctantName(String n, String e, String s, String w){
        double azimut = AZIMUT.reduce(this.azDeg());
        RightOpenInterval oct1 = RightOpenInterval.of(0,22.5);
        RightOpenInterval oct2 = RightOpenInterval.of(22.5,67.5);
        RightOpenInterval oct3 = RightOpenInterval.of(67.5,112.5);
        RightOpenInterval oct4 = RightOpenInterval.of(112.5,157.5);
        RightOpenInterval oct5 = RightOpenInterval.of(157.5,202.5);
        RightOpenInterval oct6 = RightOpenInterval.of(202.5,247.5);
        RightOpenInterval oct7 = RightOpenInterval.of(247.5,292.5);
        //RightOpenInterval oct8 = RightOpenInterval.of(292.5,337.5);
        RightOpenInterval oct9 = RightOpenInterval.of(337.5,360);

        if(oct1.contains(azimut) || oct9.contains(azimut)){
            return n;
        }
        else if(oct2.contains(azimut)){
            return n + e;
        }
        else if(oct3.contains(azimut)){
            return  e;
        }
        else if(oct4.contains(azimut)){
            return s + e;
        }
        else if(oct5.contains(azimut)){
            return s;
        }
        else if(oct6.contains(azimut)){
            return s + w;
        }
        else if(oct7.contains(azimut)){
            return w;
        }
        else {
            return n + w;
        }

    }

    /**
     *
     * @param that
     * @return angular distance between current object
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        double result = Math.acos(Math.sin(this.alt())*Math.sin(that.alt()) + Math.cos(this.alt())*Math.cos(that.alt())*Math.cos(this.az() - that.az()));
        return result;
    }

    @Override
    public String toString(){

        return "(az=" + String.format(Locale.ROOT,"%.4f",this.azDeg()) + "°" + "," + " " + "alt=" + String.format(Locale.ROOT,"%.4f",this.altDeg()) + "°)";
    }



}
