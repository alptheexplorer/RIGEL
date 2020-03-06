package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Alp Ozen
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double phi;
    private double delta;
    private double h;
    private double sidTime;

    /**
     *
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        //probably like in Sidereal we need to be more specific here(?)/convert min into decimal hours
       // this.H = Angle.ofHr(when.getHour() +((double)when.getMinute()/60.0) +(double)when.getSecond()/3600.0
               // + (double)when.getNano()/(1e9 * 3600.0));
        this.sidTime = SiderealTime.local(when, where);
        //System.out.println(sidTime);
        this.phi = where.lat();
        //System.out.println(Angle.toDeg(this.phi));
        //System.out.println(phi);
    }


    /**
     *
     * @param equatorialCoordinates
     * @return returns equatorialCoordinate transformed to Horizontal coordinates.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        // H is in Hours--> converted to rad
        double H = Angle.ofHr(sidTime -equatorialCoordinates.ra());
        //System.out.println(equatorialCoordinates.ra());
        //System.out.println(Angle.toDeg(H));
        this.h = Math.asin(
                Math.sin(equatorialCoordinates.dec()) * Math.sin(phi) + Math.cos(equatorialCoordinates.dec()) * Math.cos(phi)*Math.cos(H)
        );
        double A = Math.atan2(
                -Math.cos(equatorialCoordinates.dec())*Math.cos(phi)*Math.sin(H),
                Math.sin(equatorialCoordinates.dec()) - Math.sin(phi)*Math.sin(h)
        );
        //need to normalize before putting into Horizontal!
        //A and h are in deg? radians?
        double Anorm = Angle.normalizePositive(A);
        //double hnorm = Angle.normalizePositive(h);
        return HorizontalCoordinates.of(Anorm,h);
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
