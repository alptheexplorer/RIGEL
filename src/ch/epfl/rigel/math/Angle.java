package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Angle and useful methods, use radians unless specified
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Angle {
    private Angle(){}

    /**
     * TAU = 360° = 2*PI
     */
    public final static double TAU = 2.0*Math.PI;

    private final static double TO_RAD = Math.PI/180.0;
    private final static double TO_DEG = 180.0/Math.PI;
    private final static double MIN_PER_DEG = 60.0;
    private final static double SEC_PER_DEG = 3600.0;
    private final static double DEG_PER_HR = 15.0;

    private static ch.epfl.rigel.math.RightOpenInterval minSecInterval = ch.epfl.rigel.math.RightOpenInterval.of(0,60);
    private static ch.epfl.rigel.math.RightOpenInterval angleInterval = ch.epfl.rigel.math.RightOpenInterval.of(0,TAU);


    /**
     * @param rad
     * @return the corresponding angle in radians in range [0,Tau[
     */
    public static double normalizePositive(double rad){
        return angleInterval.reduce(rad);
    }

    /**
     * @param sec
     * @return angle from arcsec to radians
     */
    public static double ofArcsec(double sec){
        return ofDeg(sec/SEC_PER_DEG);
    }

    /**
     * Doesn't accept negative degrees (etape 9)
     * to have correct negative angle put a minus before the call
     * @param deg
     * @param min
     * @param sec
     * @return angle from deg,arcmin,arcsec to radians.
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkInInterval(minSecInterval,sec);
        Preconditions.checkInInterval(minSecInterval,min);
        Preconditions.checkArgument(deg>=0); // etape 9 correction
        return ofDeg(deg + min/MIN_PER_DEG + sec/SEC_PER_DEG);
    }

    /**
     *
     * @param deg
     * @return angle from degrees to radians
     */
    public static double ofDeg(double deg){
        return deg*TO_RAD;
    }

    /**
     *
     * @param rad
     * @return angle from radians to degrees
     */
    public static double toDeg(double rad){
        return rad*TO_DEG;
    }

    /**
     *
     * @param hr
     * @return converts hours to radians
     */
    public static double ofHr(double hr){
        return (ofDeg(hr*DEG_PER_HR));
    }

    /**
     *
     * @param rad
     * @return converts radians to hours
     */
    public static double toHr(double rad){
        return(toDeg(rad)/DEG_PER_HR);
    }



}