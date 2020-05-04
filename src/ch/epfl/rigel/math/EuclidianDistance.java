package ch.epfl.rigel.math;

// this class offers static methods useful with vectors or points on a plane
public class EuclidianDistance {

    /**
     *
     * @param x
     * @param y
     * @return finds norm of specified CartesianCoordinates
     */
    public static double norm(double x, double y){
        return Math.sqrt((x*x) + (y*y));
    }

    /**
     *
     * @param subjectOneX
     * @param subjectOneY
     * @param subjectTwoX
     * @param subjectTwoY
     * @return finds distance between two CartesianCoordinates
     */
    public static double distance(double subjectOneX, double subjectOneY, double subjectTwoX, double subjectTwoY){
        return Math.sqrt(
                (subjectOneX - subjectTwoX)*(subjectOneX - subjectTwoX)
                + (subjectOneY - subjectTwoY)*(subjectOneY - subjectTwoY)
        );
    }

}
