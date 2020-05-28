package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Moon model
 * Theory in other models
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    private static final double LONG_MOY = Angle.ofDeg(91.929336); // l0
    private static final double LONG_MOY_PER = Angle.ofDeg(130.143076); //P0
    private static final double LONG_NAS = Angle.ofDeg(291.682547); //N0
    private static final double INC_ORB = Angle.ofDeg(5.145396); //i
    private static final double EXEN_ORB = 0.0549;//e


    //some angles to use less magic numbers and stuff we can calculate once
    private static final double ANG_283 = Angle.ofDeg(283.112438);
    private static final double ANG_13 = Angle.ofDeg(13.1763966);
    private static final double ANG_1 = Angle.ofDeg(1.2739);
    private static final double ANG_0_11 = Angle.ofDeg(0.1114041);
    private static final double ANG_0_18 = Angle.ofDeg(0.1858);
    private static final double ANG_0_37 = Angle.ofDeg(0.37);
    private static final double ANG_6 = Angle.ofDeg(6.2886);
    private static final double ANG_0_6 = Angle.ofDeg(0.6583);
    private static final double ANG_0_5 = Angle.ofDeg(0.5181);
    private static final double ANG_0_05 = Angle.ofDeg(0.0529539);
    private static final double ANG_0_16 = Angle.ofDeg(0.16);
    private static final double ANG_0_2 = Angle.ofDeg(0.214);

    private static final double COS_INC_ORB = Math.cos(INC_ORB);
    private static final double SIN_INC_ORB = Math.sin(INC_ORB);
    @Override
    /**
     * @return Moon object
     */
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        //Sun's stuff
        double sunMeanAnom = SunModel.SUN.meanAnomaly(daysSinceJ2010);//ES
        double sinSunMeanAnom = Math.sin(sunMeanAnom);
        double sunLongEcl = Angle.normalizePositive(sunMeanAnom + 2 * 0.016705 * sinSunMeanAnom + ANG_283);//LAMBDAS

        double orbLongAv = ANG_13 * daysSinceJ2010 + LONG_MOY;//L
        double anomAv = orbLongAv - (ANG_0_11 * daysSinceJ2010) - LONG_MOY_PER;//M
        double evec = ANG_1 * Math.sin(2 * (orbLongAv - sunLongEcl) - anomAv);//EV
        double eqAnCorrec = ANG_0_18 * sinSunMeanAnom;//AE
        double cor3 = ANG_0_37 * sinSunMeanAnom;//A3
        double anomCor = anomAv + evec - eqAnCorrec - cor3;//Mp
        double corCent = ANG_6 * Math.sin(anomCor);//EC
        double cor4 = ANG_0_2 * Math.sin(2 * anomCor);//A4
        double orbLongCor = orbLongAv + evec + corCent - eqAnCorrec + cor4;//Lp
        double variation = ANG_0_6 * Math.sin(2 * (orbLongCor - sunLongEcl)); //V
        double orbLongCorV = orbLongCor + variation; //Lpp

        double longMoyN = LONG_NAS - (ANG_0_05 * daysSinceJ2010); //N
        double longCorN = longMoyN - (ANG_0_16 * sinSunMeanAnom); //Np

        double sinOrbLonCVMinusN = Math.sin(orbLongCorV - longCorN);

        double lambda = Angle.normalizePositive(Math.atan2(sinOrbLonCVMinusN * COS_INC_ORB, Math.cos(orbLongCorV - longCorN)) + longCorN);
        double beta = Math.asin(sinOrbLonCVMinusN * SIN_INC_ORB);
        double phase = (1 - Math.cos(orbLongCorV - sunLongEcl)) / 2.0;
        double rho = (1 - (EXEN_ORB * EXEN_ORB)) / (1 + (EXEN_ORB * Math.cos(anomCor + corCent)));

        double ang = ANG_0_5 / rho;

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Moon(equatorialCoordinates, (float) ang, (float) 0, (float) phase);
    }
}


