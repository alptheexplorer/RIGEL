package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Theory:
 * The position of a planet is more complicated to calculate: a planet doesn't orbit
 * in an ecliptic's plane, but has his own. Furthermore we want to determine the
 * planet's position in geocentric ecliptic coordinates so we need to know at
 * the same time the planet position as well as the Earth position.
 * <p>
 * We'll proceed in 4 steps:
 * 1-planet's position on his own orbit ( similar to Sun )
 * 2-projecting 1) on an ecliptic plane, express it in heliocentric ecliptic coord. ( Sun as origin)
 * 3-Earth Position in same coordinate system as 2)
 * 4-combine 2) and 3) and obtain planet's position in geocentric ecliptic coordinates
 * <p>
 * As with SunModel, be careful with the units/Angles: they are given in degrees
 * but need to be stocked in rad
 * AU = Astronomical Unit = Average distance between Sun and Earth
 * <p>
 * Theory- Outer and Inner Planets:
 * Inner: closer to the Sun than Earth: Mercure et Venus
 * Outer: further away from the Sun than Earth: Mars, Jupiter, Saturne, Uranus et Neptune
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
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String frenchName;

    //revolution period [tropical year]; Longitude at J2010[deg or rad]; Longitude at perigee [deg or rad]
    private final double T_P, EPSILON_RAD, OMEGA_RAD;
    //Orbit's Eccentricity; Half Orbit's major-axe [UA]; Orbit's inclination at ecliptic [deg or rad]
    private final double E, A, I;
    //Longitude of ascendant node [Deg or rad] (see wiki: usually it's not exactly an omega but we use that here )
    private final double BIG_OMEGA_RAD;
    // given in constructor are the angular size and magnitude of the planet at a distance of 1UA
    private final double ANG_SIZE_UA, MAGNITUDE_UA; //Ang size given in arcsec! then transformed in [rad]

    //the angular size and magnitude are a function of the distance from the point of observation
    private double angularSize, magnitude;

    //store all ENUM objects
    public static List<PlanetModel> ALL = new ArrayList<>(Arrays.asList(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE));

    //Days since J2010 [Day]; Planet's Mean Anomaly [Rad]; True (Vrai) Anomaly [Rad]
    private double d, m, v;


    //radius/distance from Sun [UA]; heliocentric longitude [rad]
    private double r, l;
    //radius projection on the ecliptic [UA]; heliocentric ecliptic longitude and latitude [rad]
    private double rPrime, lPrime, psi;
    //Geocentric ecliptic longitude and latitude [rad]
    private double lambda, beta;

    private double sinLBigOmega, cosLBigOmega;

    //stuff we can calculate here to lighten at() and useful intervals (use Horizontal ones)
    private final double MEAN_ANG_VEL = Angle.TAU / 365.242191;
    private static RightOpenInterval AZIMUT = RightOpenInterval.of(0, 360);
    private static ClosedInterval HEIGHT = ClosedInterval.symmetric(180);
    private static RightOpenInterval azimutRad = RightOpenInterval.of(0, Angle.TAU);
    private static ClosedInterval heightRad = ClosedInterval.symmetric(Math.PI);


    PlanetModel(String frenchName, double revolutionPeriod, double lonJ2010, double lonPerigee,
                double eccentricity, double halfOrbitMajorAxe, double inclination, double lonAscendNode, double angularSizeUA, double magnitudeUA) {
        this.frenchName = frenchName;
        T_P = revolutionPeriod;
        EPSILON_RAD = Angle.ofDeg(lonJ2010);
        OMEGA_RAD = Angle.ofDeg(lonPerigee);
        E = eccentricity;
        A = halfOrbitMajorAxe;
        I = Angle.ofDeg(inclination);
        BIG_OMEGA_RAD = Angle.ofDeg(lonAscendNode);
        this.ANG_SIZE_UA = Angle.ofArcsec(angularSizeUA);
        this.MAGNITUDE_UA = magnitudeUA;

    }


    /**
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return Planet at epoch J2010 + @daysSinceJ2010, using @eclipticToEquatorialConversion
     * to obtain equatorial coordinates from ecliptic one
     * Formula in the text, see book for better references
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double oneMinusE = 1 - (E * E);
        double oneMinEEarth = 1 - (EARTH.E * EARTH.E);

        d = daysSinceJ2010;
        m = Angle.normalizePositive(MEAN_ANG_VEL * d / T_P) + EPSILON_RAD - OMEGA_RAD;
        double mEarth = Angle.normalizePositive(MEAN_ANG_VEL * d / EARTH.T_P) + EARTH.EPSILON_RAD - EARTH.OMEGA_RAD;
        v = Angle.normalizePositive(m + 2 * E * Math.sin(m));
        double vEarth = Angle.normalizePositive(mEarth + 2 * EARTH.E * Math.sin(mEarth));
        r = (A * oneMinusE) / (1 + E * Math.cos(v));
        l = Angle.normalizePositive(v + OMEGA_RAD);
        double bigR = (EARTH.A * oneMinEEarth) / (1 + EARTH.E * Math.cos(vEarth));
        double bigL = Angle.normalizePositive(vEarth + EARTH.OMEGA_RAD);

        sinLBigOmega = Math.sin(l - BIG_OMEGA_RAD);
        cosLBigOmega = Math.cos(l - BIG_OMEGA_RAD);
        psi = Math.asin(sinLBigOmega * Math.sin(I));
        rPrime = r * Math.cos(psi);
        lPrime = Math.atan2(sinLBigOmega * Math.cos(I), cosLBigOmega
        ) + BIG_OMEGA_RAD;

        double bigLMinusLPrime = bigL - lPrime;

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
        angularSize = ANG_SIZE_UA / rho;

        //phase= illuminated portion of the Planet's disk as seen from Earth
        double bigF = (1 + Math.cos(lambda - l)) / 2.0;
        magnitude = MAGNITUDE_UA + 5 * Math.log10(r * rho / Math.sqrt(bigF));


        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Planet(frenchName, equatorialCoordinates, (float) angularSize, (float) magnitude);

    }

}
