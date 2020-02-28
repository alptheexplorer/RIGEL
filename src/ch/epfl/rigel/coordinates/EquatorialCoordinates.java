package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class EquatorialCoordinates extends ch.epfl.rigel.coordinates.SphericalCoordinates {

    private EquatorialCoordinates(double l, double la) {
        super(l, la);
    }

    public static EquatorialCoordinates of(double ra, double dec){
        return new EquatorialCoordinates(ra, dec);
    }

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
