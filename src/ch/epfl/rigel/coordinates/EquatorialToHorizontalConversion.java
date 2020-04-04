package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Alp Ozen
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double phi;
    private double h;

    private double sidTime;

    /**
     *
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        //probably like in Sidereal we need to be more specific here(?)/convert min into decimal hours
        //this.H = Angle.ofHr(when.getHour() +((double)when.getMinute()/60.0) +(double)when.getSecond()/3600.0
          //      + (double)when.getNano()/(1e9 * 3600.0));

        this.sidTime = SiderealTime.local(when, where);
        this.phi = where.lat();

    }


    /**
     *
     * @param equatorialCoordinates
     * @return returns equatorialCoordinate transformed to Horizontal coordinates.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        // H is in Hours--> converted to rad
        double H = sidTime - equatorialCoordinates.ra();
        RightOpenInterval toReduce = RightOpenInterval.of(Angle.ofDeg(-90),Angle.ofDeg(90));

        this.h = Math.asin(
                Math.sin(equatorialCoordinates.dec()) * Math.sin(phi) + Math.cos(equatorialCoordinates.dec()) * Math.cos(phi)*Math.cos(H)
        );
        double A = Math.atan2(
                -Math.cos(equatorialCoordinates.dec())*Math.cos(phi)*Math.sin(H),
                Math.sin(equatorialCoordinates.dec()) - Math.sin(phi)*Math.sin(h)
        );
        //need to normalize before putting into Horizontal!
        double A_NORM = Angle.normalizePositive(A);
        double h_NORM = toReduce.reduce(h);


        return HorizontalCoordinates.of(A_NORM,h_NORM);
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
