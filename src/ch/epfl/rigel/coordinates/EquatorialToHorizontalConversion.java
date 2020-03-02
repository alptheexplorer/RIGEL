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
        this.H = Angle.ofHr(when.getHour()) + Angle.ofSec(when.getMinute());
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
        return HorizontalCoordinates.of(A,h);
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
