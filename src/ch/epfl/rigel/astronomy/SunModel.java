package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * See CelestialObjectModel for the theory and method at()
 *
 * Sun is the Celestial object with the simplest position to calculate.
 * The model we use supposes the Sun orbiting around Earth, it's the opposite in reality.
 * The constants given are valid for epoch J2010, they are used as a starting point
 * for the calculations. See book for better references.
 * Be careful with the units: the constants are given in degrees but the formula are in
 * radiants as we use that as a default in the project
 * I'll name the constants as in the text, with comments.
 *
 * The formulas allow to determine the Sun's position in Geocentric Ecliptic Coordinates,
 * i.e. Center of the Earth as origin, in a given instant
 *
 * Pag105 (and on) of Book for test
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

    //Constants to use as a reference in J2010:

    //Sun's Longitude in J2010, in degrees
    private final double EPSILON_DEG = 279.557208;
    private final double EPSILON_RAD = Angle.ofDeg(EPSILON_DEG);

    //Sun's Longitude at perigee ( closest point to Earth in orbit ), in deg
    private final double OMEGA_DEG = 283.112438;
    private final double OMEGA_RAD = Angle.ofDeg(OMEGA_DEG);

    //Eccentricity of the orbit Sun/Earth ( note that is very close to a circle indeed)
    private final double E = 0.016705;

    //Values/Variables to calculate:

    //Days after/since J2010 [Day]; Sun's Mean Anomaly [Rad]; True (Vraie) Anomaly [Rad]
    private double d,m,v;

    //Geocentric Ecliptic Longitude (Lambda) and Latitude (Phi) [Rad]
    //latitude is 0 by definition as the reference plane is the ecliptic in which
    //we find both Earth and Sun; I won't put it as a constant for better understanding
    private double lambda,phi;

    //Sun's angular size seen from Earth. It varies with distance from Earth
    //being the orbit elliptic
    private double theta;

    //Useful stuff to calculate once:

    //Mean angular velocity of rotation of Earth around Sun. 365.24.. are
    //the days needed for a complete rotation = Tropical Year
    private final double MEAN_ANG_VEL= Angle.TAU/365.242191;

    //distance of semi-major axis [km]. ( Not requested )
    private final double R_0 = 1.495985*1e8;

    //angular size (diameter) at R_0
    private final double THETA_0_DEG = 0.533128;
    private final double THETA_0_RAD = Angle.ofDeg(THETA_0_DEG);


    /**
     *
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return Sun at epoch J2010 + @daysSinceJ2010, using @eclipticToEquatorialConversion
     * to obtain equatorial coordinates from ecliptic ones
     * Formula in the text, see book for better references
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        d= daysSinceJ2010; //TODO
        m= MEAN_ANG_VEL*d + EPSILON_RAD - OMEGA_RAD;
        v = m + 2*E*Math.sin(m);
        lambda = v + OMEGA_RAD;
        phi = 0;
        double tmp= (1+Math.cos(v))/(1- E*E);
        theta = THETA_0_RAD*tmp; //the result should be in rad TODO
        //if we want distance r = R_0/tmp

        //check if I used the conversion correctly TODO
        EclipticCoordinates eclipticCoordinates= EclipticCoordinates.of(lambda,phi);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);
        Sun sun = new Sun(eclipticCoordinates,equatorialCoordinates,(float)theta,(float)v);

        return sun;
    }
}
