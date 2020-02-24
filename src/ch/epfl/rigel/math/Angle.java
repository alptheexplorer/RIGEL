package ch.epfl.rigel.math;

/**
 * @author Alp Ozen
 */
public final class Angle {
    private Angle(){}
    private static ch.epfl.rigel.math.RightOpenInterval minSecInterval = ch.epfl.rigel.math.RightOpenInterval.of(0,60);

    public final static double TAU = 2*Math.PI;
    private final static double TO_RAD = Math.PI/180;
    private final static double TO_DEG = 180/Math.PI;

    /**
     *
     * @param rad
     * @return the corresponding angle in radians in range [0,Tau[
     */
    public static double normalizePositive(double rad){
        if(rad > TAU){
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
        return ofDeg(sec/3600);
    }


    /**
     *
     * @param deg
     * @param min
     * @param sec
     * @return converts deg,arcmin,arcsec to degree
     */
    public static double ofDMS(int deg, int min, double sec){
        double floatpoint;
        if(!minSecInterval.contains(deg) || !minSecInterval.contains(min)){
            throw new IllegalArgumentException();
        }
        else{
           floatpoint = (sec/3600) + (min/60);
           return deg += floatpoint;
        }
    }

    /**
     *
     * @param deg
     * @return converts degrees to radians
     */
    public static double ofDeg(double deg){
        return deg* TO_RAD;
    }

    /**
     *
     * @param rad
     * @return converts radians to degrees
     */
    public static double toDeg(double rad){
        return rad* TO_DEG;
    }


    /**
     *
     * @param hr
     * @return converts hours to radians
     */
    public static double ofHr(double hr){
        return ofDeg(hr/60);
    }

    /**
     *
     * @param rad
     * @return converts radians to hours
     */
    public static double toHr(double rad){
        return(toDeg(rad)*60);
    }

    //hi


}