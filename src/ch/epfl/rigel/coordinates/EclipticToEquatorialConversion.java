package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;


public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>  {

    private Polynomial E = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), (Angle.ofDeg(23)+ Angle.ofSec(26) + Angle.ofArcsec(21.45)));
    private double obliqueEcliptique;
    private double cosObliqueEcliptique;
    private double sinObliqueEcliptique;

    /**
     *
     * @param when
     */
    public EclipticToEquatorialConversion(ZonedDateTime when){
        this.obliqueEcliptique = E.at(J2000.julianCenturiesUntil(when));
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
        double ascension = Math.atan2((Math.sin(eclipticCoordinates.lon()*cosObliqueEcliptique - Math.tan(eclipticCoordinates.lat()*sinObliqueEcliptique))), Math.cos(eclipticCoordinates.lon()));
        double declination = Math.asin(Math.sin(eclipticCoordinates.lat()*cosObliqueEcliptique) + Math.cos(eclipticCoordinates.lat())*sinObliqueEcliptique*Math.sin(eclipticCoordinates.lon()));
        return EquatorialCoordinates.of(ascension, declination);
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
