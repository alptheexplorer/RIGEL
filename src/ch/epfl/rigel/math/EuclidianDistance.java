package ch.epfl.rigel.math;


/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 * this class is extra, it offers a method to obtain the Euclidian norm, and a method that provides the distance between two vectors
 */
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
        return
                norm((subjectOneX - subjectTwoX), (subjectOneY - subjectTwoY));

    }

}
