package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Stereographic Projection of horizontal coordinates. Instantiation leads to faster conversion in apply().
 * <p>
 * lambda = longitude = az() = lon()
 * phi = latitude = lat() = lat()
 * the parallels will be projected as circles on the plane
 * the meridians will be projected into circles too, but in this
 * project we will not draw those, so we won't compute them
 * </p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    //attribute names based on formula in 2.1- Projection stéréographique
    //they are all in function of the center
    final private double sinPhiCenter, cosPhiCenter, lambdaCenter, phiCenter;
    final private HorizontalCoordinates centerHor;

    /**
     * we calculate all the necessary attributes here to speed up on apply()
     * @param center
     * @return the stereographic projection centered in @center
     *
     */
    public StereographicProjection(HorizontalCoordinates center) {

        //only formulas we can calculate in the constructor
        //i.e. they depend only on the center of the projection
        centerHor = center;
        lambdaCenter = center.az();
        phiCenter = center.alt();
        cosPhiCenter = Math.cos(phiCenter);
        sinPhiCenter = Math.sin(phiCenter);
    }

    /**
     * @param hor
     * @return Cartesian Coordinates of center of circle corresponding to the projection
     * of the parallel passing though @hor
     * <p>
     *     the ordY of the center could be infinite, that means that the "cercle"
     *     is in fact a simple line ( equator)
     *     note also that the projections of the paralallels will not be concentric as
     *     we move from the north or south poles
     * </p>
     *
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        //Only need the latitude=phi of @hor to project it
        double circleCenterX = 0.0;
        double phi = hor.lat();

        double circleCenterY = cosPhiCenter / (
                Math.sin(phi) + sinPhiCenter);
        return CartesianCoordinates.of(circleCenterX, circleCenterY);
    }

    /**
     * @param parallel
     * @return radius=rho(in formula) of cercle correspondant to the projection of the parallel
     * passing throw the point of coord hor (? error here in cours)(@parallel)
     * could be infinite (equator)
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        //we only need the phi=latitude of the parallel to project it

        double phi = parallel.lat();
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        double radius = cosPhi / (
                sinPhi + sinPhiCenter);
        return radius;
    }

    /**
     * @param rad
     * @return projected diameter of a sphere of angular size @rad centered
     * at the center of the projection, admitting that the latter is on the horizon
     * <p>
     * in particular we will draw the Moon and the Sun etc.. with a diameter always
     * as if they were exactly in the center of our projection, for simplicity
     * therefore they will not change size when we move the projection
     */
    public double applyToAngle(double rad) {
        double diameter = 2 * Math.tan(rad / 4.0);
        return diameter;
    }


    /**
     * @param azAlt
     * @return stereographic projection of Horizontal Coordinates @azAlt onto cartesian plane,
     * giving the corresponding Cartesian Coordinates
     */
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double phi = azAlt.lat();
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);
        double lambda = azAlt.lon();
        double lambdaDelta = (lambda - lambdaCenter);
        double cosLambdaDelta = Math.cos(lambdaDelta);
        double d = 1.0 / (
                1 + sinPhi * sinPhiCenter +
                       cosPhi * cosPhiCenter * Math.cos(lambdaDelta));

        double x = d * cosPhi * Math.sin(lambdaDelta);
        double y = d * (sinPhi * cosPhiCenter -
               cosPhi * sinPhiCenter * cosLambdaDelta);

        return CartesianCoordinates.of(x, y);
    }


    /**
     * @param xy
     * @return inverse of stereographic projection: from Cartesian Coordinates ( plane, @xy) to
     * spherical coordinates ( Horizontal )
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double xPwr = xy.x() * xy.x();
        double yPwr = xy.y() * xy.y();
        double rho = Math.sqrt(xPwr + yPwr);
        double rhoPwr = xPwr + yPwr;
        double sinC = 2 * rho / (
                rhoPwr + 1);
        double cosC = (1 - rhoPwr) / (
                rhoPwr + 1);

        double atanNum = xy.x() * sinC;
        double atanDen = rho * cosPhiCenter * cosC - xy.y() * sinPhiCenter * sinC;

        if(xy.x()==0 & xy.y()==0){
            //correction of error ETAPE 9
            return HorizontalCoordinates.of(0,0);
        }
        else{
            double lambda = Angle.normalizePositive(Math.atan2(
                    atanNum,
                    atanDen) + lambdaCenter);

            double phi = Math.asin(
                    cosC * sinPhiCenter +
                            (xy.y() * sinC * cosPhiCenter) / rho
                    );
            return HorizontalCoordinates.of(lambda, phi);
        }
    }


    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


    /**
     * @return text version, free format but needs to contain " StereographicProjection "
     * and coordinates of the projection's center
     */
    public String toString() {
        return String.format(Locale.ROOT,
                "  StereographicProjection (center): (%f,%f) --> (%f,%f)",
                phiCenter,
                lambdaCenter, apply(centerHor).x(), apply(centerHor).y())
                ;
    }
}