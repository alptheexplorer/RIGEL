package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;


public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>  {

    private Polynomial E = Polynomial.of(Angle.arcsSecToDeg(0.00181), -Angle.arcsSecToDeg(0.0006), -Angle.arcsSecToDeg(46.815), (23+ Angle.minToDeg(26) + Angle.arcsSecToDeg(21.45)));
    private double obliqueEcliptique;
    private double cosObliqueEcliptique;
    private double sinObliqueEcliptique;

    /**
     * purpose of constructor is to immediately construct non-dependent values for faster usage in apply method
     * @param when
     */
    public EclipticToEquatorialConversion(ZonedDateTime when){
        this.obliqueEcliptique = Angle.ofDeg(E.at(J2000.julianCenturiesUntil(when)));
        this.cosObliqueEcliptique = Math.cos(obliqueEcliptique);
        this.sinObliqueEcliptique = Math.sin(obliqueEcliptique);
    }

    /**
     *
     * @param eclipticCoordinates
     * @return returns ecliptic coordinate transformed to equatorial coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        RightOpenInterval toReduce = RightOpenInterval.of(Angle.ofDeg(-90),Angle.ofDeg(90));
        double ascension = Math.atan2((Math.sin(eclipticCoordinates.lon())*cosObliqueEcliptique - Math.tan(eclipticCoordinates.lat())*sinObliqueEcliptique), Math.cos(eclipticCoordinates.lon()));
        double declination = toReduce.reduce(Math.asin((Math.sin(eclipticCoordinates.lat())*cosObliqueEcliptique) + Math.cos(eclipticCoordinates.lat())*sinObliqueEcliptique*Math.sin(eclipticCoordinates.lon())));
        return EquatorialCoordinates.of(Angle.normalizePositive(ascension), toReduce.reduce(declination));
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