package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model of a Planet in our solar system
 * <p>
 * Theory:
 * The position of a planet is more complicated to calculate: a planet doesn't orbit
 * in an ecliptic's plane, but has his own. Furthermore we want to determine the
 * planet's position in geocentric ecliptic coordinates so we need to know at
 * the same time the planet position as well as the Earth position.
 * </p> <p>
 * We'll proceed in 4 steps:
 * 1-planet's position on his own orbit ( similar to Sun )
 * 2-projecting 1) on an ecliptic plane, express it in heliocentric ecliptic coord. ( Sun as origin)
 * 3-Earth Position in same coordinate system as 2)
 * 4-combine 2) and 3) and obtain planet's position in geocentric ecliptic coordinates
 * </p> <p>
 * As with SunModel, be careful with the units/Angles: they are given in degrees
 * but need to be stocked in rad
 * AU = Astronomical Unit = Average distance between Sun and Earth
 * </p> <p>
 * Theory- Outer and Inner Planets:
 * Inner: closer to the Sun than Earth: Mercure et Venus
 * Outer: further away from the Sun than Earth: Mars, Jupiter, Saturne, Uranus et Neptune
 * </p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String frenchName;

    //revolution period [tropical year]; Longitude at J2010[deg or rad]; Longitude at perigee [deg or rad]
    private final double tP, epsilonRad, omegaRad;
    //Orbit's Eccentricity; Half Orbit's major-axe [UA]; Orbit's inclination at ecliptic [deg or rad]
    private final double E, A, I;
    //Longitude of ascendant node [Deg or rad] (see wiki: usually it's not exactly an omega but we use that here )
    private final double bigOmegaRad;
    // given in constructor are the angular size and magnitude of the planet at a distance of 1UA
    private final double angSizeUa, magnitudeUa; //Ang size given in arcsec! then transformed in [rad]



    //store all ENUM objects
    public static List<PlanetModel> ALL = new ArrayList<>(Arrays.asList(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE));

    //COMMENTS ON VARIABLES THAT WILL BE DEFINED IN AT()
    //the angular size and magnitude are a function of the distance from the point of observation: angularSize, magnitude;
    //Days since J2010 [Day]; Planet's Mean Anomaly [Rad]; True (Vrai) Anomaly [Rad]: d,m, l;
    //radius/distance from Sun [UA]; heliocentric longitude [rad]: r, l;
    //radius projection on the ecliptic [UA]; heliocentric ecliptic longitude and latitude [rad]: rPrime, lPrime, psi;
    //Geocentric ecliptic longitude and latitude [rad]: lambda, beta;

    //useful stuff to lighten at()
    private final double MEAN_ANG_VEL = Angle.TAU / 365.242191;
    private double oneMinusE, sinI, cosI;



    PlanetModel(String frenchName, double revolutionPeriod, double lonJ2010, double lonPerigee,
                double eccentricity, double halfOrbitMajorAxe, double inclination, double lonAscendNode, double angularSizeUA, double magnitudeUA) {
        this.frenchName = frenchName;
        tP = revolutionPeriod;
        epsilonRad = Angle.ofDeg(lonJ2010);
        omegaRad = Angle.ofDeg(lonPerigee);
        E = eccentricity;
        A = halfOrbitMajorAxe;
        I = Angle.ofDeg(inclination);
        bigOmegaRad = Angle.ofDeg(lonAscendNode);
        this.angSizeUa = Angle.ofArcsec(angularSizeUA);
        this.magnitudeUa = magnitudeUA;

        //stuff we can calculate here
        oneMinusE = 1 - (E * E);
        sinI = Math.sin(I);
        cosI = Math.cos(I);
    }


    /**
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return Planet at epoch J2010 + @daysSinceJ2010, using @eclipticToEquatorialConversion
     * to obtain equatorial coordinates from ecliptic one
     * Formula in the text, see book for better references and comments at the beginning for meaning of variables
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double d = daysSinceJ2010;
        double m = Angle.normalizePositive(MEAN_ANG_VEL * d / tP) + epsilonRad - omegaRad;
        double v = Angle.normalizePositive(m + 2 * E * Math.sin(m));
        double r = (A * oneMinusE) / (1 + E * Math.cos(v));
        double l = Angle.normalizePositive(v + omegaRad);

        double sinLBigOmega = Math.sin(l - bigOmegaRad);
        double cosLBigOmega = Math.cos(l - bigOmegaRad);
        double psi = Math.asin(sinLBigOmega * sinI);
        double rPrime = r * Math.cos(psi);
        double lPrime = Math.atan2(sinLBigOmega * cosI, cosLBigOmega
        ) + bigOmegaRad;

        // Earth R and L using auxiliary method
        double[] bigRL= earthRL(d);
        double bigR = bigRL[0];
        double bigL = bigRL[1];

        double bigLMinusLPrime = bigL - lPrime;

        double lambda, beta;
        if (this.A <= 1) {
            lambda = Angle.normalizePositive(Math.PI + bigL + Math.atan2(
                    rPrime * Math.sin(bigLMinusLPrime),
                    bigR - rPrime * Math.cos(bigLMinusLPrime))
            );
        } else {
            lambda = Angle.normalizePositive(lPrime + Math.atan2(
                    bigR * Math.sin(-bigLMinusLPrime),
                    rPrime - bigR * Math.cos(-bigLMinusLPrime)
            ));

        }

        beta = Math.atan(
                (rPrime * Math.tan(psi) * Math.sin(lambda - lPrime)) /
                        (bigR * Math.sin(-bigLMinusLPrime))
        );

        //distance from Earth
        double rho = Math.sqrt(
                bigR * bigR + r * r - 2 * bigR * r * Math.cos(l - bigL) * Math.cos(psi)
        );
        double angularSize = angSizeUa / rho;

        //phase= illuminated portion of the Planet's disk as seen from Earth
        double bigF = (1 + Math.cos(lambda - l)) / 2.0;
        double magnitude = magnitudeUa + 5 * Math.log10(r * rho / Math.sqrt(bigF));


        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Planet(frenchName, equatorialCoordinates, (float) angularSize, (float) magnitude);

    }

    /**
     *
     * @return copy of frenchname
     */
    public String getFrenchName(){
        return this.frenchName;
    }


    //auxiliary method for Earth
    private double[] earthRL(double daysSinceJ2010){
    double[] bigRbigL = new double[2];
    double d = daysSinceJ2010;
    double oneMinEEarth = EARTH.oneMinusE;
    double mEarth = Angle.normalizePositive(MEAN_ANG_VEL * d / EARTH.tP) + EARTH.epsilonRad - EARTH.omegaRad;
    double vEarth = Angle.normalizePositive(mEarth + 2 * EARTH.E * Math.sin(mEarth));
    double bigR = (EARTH.A * oneMinEEarth) / (1 + EARTH.E * Math.cos(vEarth));
    double bigL = Angle.normalizePositive(vEarth + EARTH.omegaRad);
    bigRbigL[0] = bigR;
    bigRbigL[1] = bigL;
    return bigRbigL;
    }

}
