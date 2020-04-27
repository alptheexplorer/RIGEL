package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.EuclidianDistance;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        //setting black background
        this.clear();
    }


    // returns transformed diameter of celestial objects
    private double transformedDiscDiameter(CelestialObject object, StereographicProjection projection, Transform planeToCanvas){
        if((object instanceof Sun)|| (object instanceof Moon)){
            double discRadius = projection.applyToAngle(Angle.ofDeg(0.5));
            Point2D transformedRadiusVector = planeToCanvas.deltaTransform(new Point2D(discRadius,0));
            return EuclidianDistance.norm(transformedRadiusVector.getX(),transformedRadiusVector.getY());
        }
        else {
            ClosedInterval interval = ClosedInterval.of(-2, 5);
            double mP = interval.clip(object.magnitude());
            double f = (99 - (17 * mP)) / 140.0;
            double discRadius = f*projection.applyToAngle(Angle.ofDeg(0.5));
            Point2D transformedRadiusVector = planeToCanvas.deltaTransform(new Point2D(discRadius,0));
            return EuclidianDistance.norm(transformedRadiusVector.getX(),transformedRadiusVector.getY());
        }
    }

    private Point2D transformCoordinates(double x, double y, Transform planeToCanvas){
        Point2D unTransformedVector = new Point2D(x,y);
        return planeToCanvas.transform(unTransformedVector);
    }

    /**
     * clears canvas and set black background
     * @return void
     */
    public void clear(){
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill (Color.BLACK);
        ctx.fillRect ( 0 , 0 , canvas.getWidth (), canvas.getHeight ());
    }

    public int j = 0;
    /**
     * draws stars and asterism
     * @param sky
     * @param projection
     * @param planeToAffine
     * @return void
     */
    public void drawStars(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){

        int i =0;
        for(Star s:sky.stars()){
            double discRadius = this.transformedDiscDiameter(s,projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(sky.starCoordinates()[i],sky.starCoordinates()[i+1],planeToAffine);
            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));
            ctx.fillOval(transformedCoordinates.getX()-discRadius,transformedCoordinates.getY()+discRadius,discRadius,discRadius);
            i+=2;
            j++;
        }
    }

    /**
     * draws planets to canvas
     * @param sky
     * @param projection
     * @param planeToAffine
     */
    public void drawPlanets(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){

        int i =0;
        for(Planet planet:sky.planets()){
            double discRadius = this.transformedDiscDiameter(planet,projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(sky.planetCoordinates()[i],sky.planetCoordinates()[i+1],planeToAffine);
            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(transformedCoordinates.getX()-discRadius,transformedCoordinates.getY()+discRadius,discRadius,discRadius);
            i+=2;
        }
    }

    /**
     * draws sun to canvas
     * @param sky
     * @param projection
     * @param planeToAffine
     */
    public void drawSun(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){

            double discRadius = this.transformedDiscDiameter(sky.sun(),projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(sky.sunPosition().x(),sky.sunPosition().y(),planeToAffine);
            // drawing smallest disc
            ctx.setFill(Color.WHITE);
            ctx.fillOval(transformedCoordinates.getX()-discRadius,transformedCoordinates.getY()+discRadius,discRadius,discRadius);
            //drawing middle size disc
            ctx.setFill(Color.YELLOW);
            ctx.fillOval(transformedCoordinates.getX()-(discRadius+2),transformedCoordinates.getY()+(discRadius+2),(discRadius+2),(discRadius+2));
            //drawing largest disc the halo
            canvas.setOpacity(0.25);
            ctx.setFill(Color.YELLOW);
            ctx.fillOval(transformedCoordinates.getX()-(discRadius*2.2),transformedCoordinates.getY()+(discRadius*2.2),(discRadius*2.2),(discRadius*2.2));
            canvas.setOpacity(1);

        }

    /**
     * draws moon to canvas
     * @param sky
     * @param projection
     * @param planeToAffine
     */

    public void drawMoon(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){
        double discRadius = this.transformedDiscDiameter(sky.moon(),projection,planeToAffine);
        Point2D transformedCoordinates = this.transformCoordinates(sky.moonPosition().x(),sky.moonPosition().y(),planeToAffine);
        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoordinates.getX()-discRadius,transformedCoordinates.getY()+discRadius,discRadius,discRadius);
    }

    public void drawHorizon(StereographicProjection projection, Transform planeToAffine){
        HorizontalCoordinates horizonCoord = HorizontalCoordinates.ofDeg(0, 0);
        double discRadius = projection.circleRadiusForParallel(horizonCoord);
        CartesianCoordinates circleCenter = projection.circleCenterForParallel(horizonCoord);
        Point2D transformedRadius = planeToAffine.deltaTransform(discRadius,discRadius);
        Point2D transformedCoordinates = this.transformCoordinates(circleCenter.x(),circleCenter.y(),planeToAffine);
        ctx.setStroke(Color.RED);
        ctx.strokeOval(transformedCoordinates.getX() - transformedRadius.getX(),transformedCoordinates.getY() + transformedRadius.getX(), transformedRadius.getX()*2, transformedRadius.getX()*2);

    }
}


















