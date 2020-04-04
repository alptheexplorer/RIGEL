package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates{

    private static RightOpenInterval AZIMUT = RightOpenInterval.of(0,360);
    private static ClosedInterval HEIGHT = ClosedInterval.of(-90,90);
    private static RightOpenInterval AZIMUT_RAD = RightOpenInterval.of(Angle.ofDeg(0),Angle.ofDeg(360));
    private static ClosedInterval heightRad = ClosedInterval.of(Angle.ofDeg(-90),Angle.ofDeg(90));

    private EquatorialCoordinates(double l, double la) {
        super(l, la);
    }

    public static EquatorialCoordinates of(double ra, double dec){
        return new EquatorialCoordinates(Preconditions.checkInInterval(AZIMUT_RAD,ra), Preconditions.checkInInterval(heightRad,dec));
    }

    public static EquatorialCoordinates ofDeg(double raDeg, double decDeg) { return new EquatorialCoordinates(Angle.ofDeg(Preconditions.checkInInterval(AZIMUT,raDeg)), Angle.ofDeg(Preconditions.checkInInterval(HEIGHT,decDeg))); }

    public double ra(){
        return super.lon();
    }

    public double raDeg(){
        return super.lonDeg();
    }

    public double raHr(){
        return (super.lonDeg()*(12.0/180));
    }

    public double dec(){
        return super.lat();
    }

    public double decDeg(){
        return super.latDeg();
    }

    @Override
    public String toString(){

        return "(ra=" + String.format(Locale.ROOT,"%.4f",this.raHr()) + "°" + "," + " " + "dec=" + String.format(Locale.ROOT,"%.4f",this.decDeg()) + "°)";
    }
}
