package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

/**
 * stereographic projection of horizontal coordinates
 *
 * lambda = longitude = az() = lon()
 * phi = latitude = lat() = lat()
 */
public class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>{

    // attribute names based on formula in 2.1- Projection stéréographique
    //they are all in function of the center
   // private double absX, ordY, lambda, phi,  lambdaDelta, d, rho;
    private double sinPhiCenter, sinLambdaCenter, cosPhiCenter, cosLambdaCenter, lambdaCenter, phiCenter;


    /**
     *
     * @param center
     * @return the stereographic projection centered in @center
     * we calculate all the necessary attributes here
     */
    public StereographicProjection(HorizontalCoordinates center){

        // how to define lambda and phi now? WE CANNOT, but we can define the
        // attributes that depend only on the center
        // this is just a projection so it only serves to calculate
        // where to actually project stuff
        /**lambdaDelta = lambda - center.lon();
        d = 1/(
                1+ Math.sin(phi)*Math.sin(center.lat()) +
                        Math.cos(phi)*Math.sin(center.lat())*Math.cos(lambdaDelta)
                );
        absX = d*Math.cos(phi)*Math.sin(lambdaDelta);
        ordY = d*(
                Math.sin(phi)*Math.cos(center.lat()) -
                        Math.cos(phi)*Math.sin(center.lat())*Math.cos(lambdaDelta)
                );
        rho=**/

        lambdaCenter = center.lon();
        phiCenter = center.lat();
        sinLambdaCenter = Math.sin(lambdaCenter);
        sinPhiCenter = Math.sin(phiCenter);
        cosLambdaCenter = Math.cos(lambdaCenter);
        cosPhiCenter = Math.cos(phiCenter);



    }

    /**
     *
     * @param hor
     * @return Cartesian Coordinates of center of circle corresponding to the projection
     * of the parallel passing though @hor
     * the ordY of the center could be infinite, that means that the "cercle"
     * is in fact a simple line ( equator)
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){


    }

    /**TODO
     *
     * @param parallel
     * @return radius of cercle correspondant to the projection of the parallel
     * passing throw the point of coord hor (? error here in cours)(@parallel)
     * could be infinite (equator)
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){

    }

    /**
     *TODO
     * @param rad
     * @return projected diameter of a sphere of angular size @rad centered
     * at the center of the projection, admitting that the latter is on the horizon
     */
    public double applyToAngle(double rad){

    }


    /**
     *
     * @param azAlt
     * @return stereographic projection of Horizontal Coordinates @azAlt onto cartesian plane,
     * giving the corresponding Cartesian Coordinates
     */
    public CartesianCoordinates apply(HorizontalCoordinates azAlt){



        double phi = azAlt.lat();
        double lambda = azAlt.lon();
        double lambdaDelta = lambda- lambdaCenter;
        double cosLambdaDelta = Math.cos(lambdaDelta);
        double d = 1/(
                1+ Math.sin(phi)*sinLambdaCenter +
                        Math.cos(phi)*cosPhiCenter*Math.cos(lambdaDelta) );

        double x = d*Math.cos(phi)*Math.sin(lambdaDelta);
        double y = d*( Math.sin(phi)*cosPhiCenter -
                Math.cos(phi)*sinPhiCenter*cosLambdaDelta);

        return CartesianCoordinates.of(x,y);

    }


    /**
     *
     * @param xy
     * @return inverse of stereographic projection: from Cartesian Coordinates ( plane, @xy) to
     * spherical coordinates ( Horizontal )
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double xPwr = xy.x() * xy.x();
        double yPwr = xy.y() * xy.y();
        double rho = Math.sqrt(xPwr + yPwr);
        double rhoPwr = rho*rho;
        double sinC = 2*rho/(
                rhoPwr + 1 );
        double cosC = (1- rhoPwr)/(
                rhoPwr + 1 );

        double lambda = Math.atan2(xy.x()*sinC,
                rho*cosPhiCenter*cosC - xy.y()*sinPhiCenter*sinC) + lambdaCenter;

        double phi= Math.asin(cosC*sinPhiCenter +
                (xy.y()*sinC*cosPhiCenter)/rho );
        return HorizontalCoordinates.of(lambda, phi);

    }


    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


    /**
     *
     * @return text version, free format but needs to contain " StereographicProjection "
     * and coordinates of the projection's center
     */
    public String toString(){
        //TODO
        return String.format(Locale.ROOT,
                "(%f,%f)",
                absX,
                ordY);
    }
}