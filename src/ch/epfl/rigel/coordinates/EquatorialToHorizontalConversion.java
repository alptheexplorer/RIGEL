package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Alp Ozen
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double H;
    private double phi;
    private double delta;
    private double h;

    /**
     *
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        //probably like in Sidereal we need to be more specific here(?)/convert min into decimal hours
        this.H = Angle.ofHr(when.getHour() +((double)when.getMinute()/60.0) +(double)when.getSecond()/3600.0
                + (double)when.getNano()/(1e9 * 3600.0));
        this.phi = where.lat();
    }


    /**
     *
     * @param equatorialCoordinates
     * @return returns equatorialCoordinate transformed to Horizontal coordinates.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        this.h = Math.asin(Math.sin(equatorialCoordinates.lat()*Math.sin(phi) + Math.cos(equatorialCoordinates.lat())*Math.cos(phi)*Math.cos(H)));
        double A = Math.atan2(-Math.cos(equatorialCoordinates.lat())*Math.cos(phi)*Math.sin(H),(Math.sin(equatorialCoordinates.lat() - Math.sin(phi)*Math.sin(h))));
        //need to normalize before putting into Horizontal!
        //A and h are in deg? radians?
        double Anorm = Angle.normalizePositive(A);
        double hnorm = Angle.normalizePositive(h);
        return HorizontalCoordinates.of(Anorm,hnorm);
    }

    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

}
