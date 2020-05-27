package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.*;
import static ch.epfl.rigel.astronomy.Epoch.J2000;


/**
 * Ecliptic to equatorial coordinates conversion
 * <p>
 *     Instantiation of the conversion object allows for faster calculation of actual conversion done
 *     in apply()
 * </p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private static final  Polynomial E = Polynomial.of(Angle.ofArcsec(0.00181),
            -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815),
            Angle.ofDMS(23,26,21.45));
    private final double obliqueEcliptique;
    private final double cosObliqueEcliptique;
    private final double sinObliqueEcliptique;

    /**
     * purpose of constructor is to immediately construct non-dependent values for faster usage in apply method
     * @param when
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        this.obliqueEcliptique = E.at(J2000.julianCenturiesUntil(when));
        this.cosObliqueEcliptique = cos(obliqueEcliptique);
        this.sinObliqueEcliptique = sin(obliqueEcliptique);
    }

    /**
     * @param eclipticCoordinates to transform
     * @return equatorial coordinates transformed from ecliptic coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        double ascension = atan2((sin(eclipticCoordinates.lon()) * cosObliqueEcliptique -
                        tan(eclipticCoordinates.lat()) * sinObliqueEcliptique),
                cos(eclipticCoordinates.lon()));
        double declination = asin((sin(eclipticCoordinates.lat()) * cosObliqueEcliptique) +
                cos(eclipticCoordinates.lat()) * sinObliqueEcliptique * sin(eclipticCoordinates.lon()));
        return EquatorialCoordinates.of(Angle.normalizePositive(ascension), declination);
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


}