package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Alp Ozen
 */
public final class Angle {
    private Angle(){}
    private static ch.epfl.rigel.math.RightOpenInterval minSecInterval = ch.epfl.rigel.math.RightOpenInterval.of(0,60);

    public final static double TAU = 2.0*Math.PI;
    private final static double TO_RAD = Math.PI/180.0;
    private final static double TO_DEG = 180.0/Math.PI;

    /**
     *
     * @param rad
     * @return the corresponding angle in radians in range [0,Tau[
     */
    public static double normalizePositive(double rad){
        if(rad >= TAU){
            rad = rad % TAU;
        }
        if(rad < 0){
            do{
                rad+= TAU;
            }while(rad < 0);
        }
        return rad;
    }

    /**
     *
     * @param sec
     * @return converts arcsec to radians
     */
    public static double ofArcsec(double sec){
        return ofDeg(sec/3600.0);
    }

    public static double arcsSecToDeg(double arcsec){
        return (arcsec/3600.0);
    }

    public static double minToDeg(double min){
        return (min/60.0);
    }

    /**
     *
     * @param sec
     * @return converts seconds to radians
     */
    public static double ofSec(double sec){
        return ofDeg(sec/60.0);
    }

    /**
     * Doesn't accept negative degrees (etape 9), put a minus before the call to have correct negative angle
     * @param deg
     * @param min
     * @param sec
     * @return converts deg,arcmin,arcsec to degree.
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkInInterval(minSecInterval,sec);
        Preconditions.checkInInterval(minSecInterval,min);
        Preconditions.checkArgument(deg>=0); // etape 9 correction
        return ofDeg(deg + min/60.0 + sec/3600.0);
    }

    /**
     *
     * @param deg
     * @return converts degrees to radians
     */
    public static double ofDeg(double deg){
        return deg*TO_RAD;
    }

    /**
     *
     * @param rad
     * @return converts radians to degrees
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
        return (ofDeg(hr*15.0));
    }

    /**
     *
     * @param rad
     * @return converts radians to hours
     */
    public static double toHr(double rad){
        return(toDeg(rad)/15.0);
    }



}