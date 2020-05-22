package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private Polynomial E = Polynomial.of(Angle.ofArcsec(0.00181),
            -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815),
            Angle.ofDMS(23,26,21.45));
    private double obliqueEcliptique;
    private double cosObliqueEcliptique;
    private double sinObliqueEcliptique;

    /**
     * purpose of constructor is to immediately construct non-dependent values for faster usage in apply method
     *
     * @param when
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        this.obliqueEcliptique = E.at(J2000.julianCenturiesUntil(when));
        this.cosObliqueEcliptique = Math.cos(obliqueEcliptique);
        this.sinObliqueEcliptique = Math.sin(obliqueEcliptique);
    }

    /**
     * @param eclipticCoordinates
     * @return returns ecliptic coordinate transformed to equatorial coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        RightOpenInterval toReduce = RightOpenInterval.of(-Math.PI / 2, Math.PI / 2);
        double ascension = Math.atan2((Math.sin(eclipticCoordinates.lon()) * cosObliqueEcliptique -
                        Math.tan(eclipticCoordinates.lat()) * sinObliqueEcliptique),
                Math.cos(eclipticCoordinates.lon()));
        double declination = toReduce.reduce(Math.asin((Math.sin(eclipticCoordinates.lat()) * cosObliqueEcliptique) +
                Math.cos(eclipticCoordinates.lat()) * sinObliqueEcliptique * Math.sin(eclipticCoordinates.lon())));
        return EquatorialCoordinates.of(Angle.normalizePositive(ascension), toReduce.reduce(declination));
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


}