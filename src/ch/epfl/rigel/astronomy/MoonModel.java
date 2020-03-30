package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum MoonModel implements CelestialObjectModel<Moon>{

    MOON;


    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        /**
         * declaration of all variables
         */
        final double DAYS_SINCE = daysSinceJ2010;
        /**
         * Constant names in order:
         * lprimprime
         * lprime
         * Mean anomaly
         * Mean anomaly corrected
         * evection
         * Correction of annual equation
         * Correction 3
         * Correction 4
         * correction of centre equation
         * variation
         * Mean longitude of the ascending node
         * corrected Mean longitude of the ascending node
         * ecliptic longitude
         * ecliptic latitude
         */
        double lPP,lP,m,mP,E,A,A3,A4,eC,V,n,nP,lambda,beta ;

        //calculating orbitalLongitude



        //final calculations
        EclipticCoordinates eclipticCoordinates= EclipticCoordinates.of(lambda,phi);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);
        return new Moon(EquatorialCoordinates equatorialPos, float angularSize, 0, float phase);
    }
}
