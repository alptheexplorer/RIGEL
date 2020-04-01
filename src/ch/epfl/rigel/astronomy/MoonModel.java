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
         * declaration of all local variables first constants then values
         */
        final double DAYS_SINCE = daysSinceJ2010;
        final double l_null = Angle.ofDeg(91.929336);
        final double P_null = Angle.ofDeg(130.143076);
        final double N_null = Angle.ofDeg(291.682547);
        final double i = Angle.ofDeg(5.145396);
        final double e = 0.0549;
        /**
         * Constant names in order:
         * average orbital longitude
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
         *mean anomaly of sun
         * longitude ecliptic of the sun
         */
        final double l,lPP,lP,m,mP,E,A,A3,A4,eC,V,n,nP,lambda,beta;

        // and some constants from the sun:
        final double LAMBDA_SUN, M_SUN, V_SUN;
        final double MEAN_ANG_VEL = Angle.TAU/365.242191;
        final double EPSILON_RAD = Angle.ofDeg(279.557208);
        final double OMEGA_RAD = Angle.ofDeg(283.112438);
        final double E_SUN = 0.016705;
        M_SUN= MEAN_ANG_VEL*DAYS_SINCE + EPSILON_RAD - OMEGA_RAD;
        V_SUN = M_SUN + 2*E_SUN*Math.sin(M_SUN);
        LAMBDA_SUN = V_SUN + OMEGA_RAD;

        //calculating orbital Longitude
        l = (Angle.ofDeg(13.1763966)*DAYS_SINCE) + l_null;
        m = l - Angle.ofDeg(0.1114041)*DAYS_SINCE - P_null;
        E = Angle.ofDeg(1.2739)*Math.sin(2*(l-LAMBDA_SUN) - m);
        A = Angle.ofDeg(0.1858)*Math.sin(M_SUN);
        A3 = Angle.ofDeg(0.37)*Math.sin(M_SUN);
        mP = m + E - A - A3;
        eC = Angle.ofDeg(6.2886)*Math.sin(mP);
        A4 = Angle.ofDeg(0.214)*Math.sin(2*mP);
        lP = l + e + eC - A + A4;
        V = Angle.ofDeg(0.6583)*Math.sin(2*(lP-LAMBDA_SUN));
        lPP = lP + V;

        //calculating ecliptic position
        n = N_null - (Angle.ofDeg(0.0529539)*DAYS_SINCE);
        nP = n - Angle.ofDeg(0.16)*Math.sin(M_SUN);
        lambda = Angle.normalizePositive(
                Math.atan2(
                        Math.sin(lPP - nP) * Math.cos(i),
                        Math.cos(lPP - nP) )
                        + nP );
        beta = Angle.normalizePositive(
                Math.asin(
                        Math.sin(lPP - nP) * Math.sin(i)
                ));

        //calculating phase
        double phase = (1 - Math.cos(lPP-LAMBDA_SUN) )/ 2;

        //calculating angular size:
        double rho = (1 - (e*e) )/( 1 + e*Math.cos(mP + eC) );
        double angularSize = Angle.normalizePositive(Angle.ofDeg(0.5181)/rho );

        //final steps
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda,beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);
        return new Moon(equatorialCoordinates, (float)angularSize, 0, (float)phase);
    }
}
