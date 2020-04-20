package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.EuclidianDistance;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }


    // returns transformed diameter of celestial objects
    private double transformedDiscRadius(CelestialObject object, StereographicProjection projection, Transform planeToCanvas){
        if((object instanceof Sun)|| (object instanceof Moon)){
            double discRadius = projection.applyToAngle(object.angularSize())/2.0;
            Point2D transformedRadiusVector = planeToCanvas.deltaTransform(new Point2D(discRadius,0));
            return EuclidianDistance.norm(transformedRadiusVector.getX(),transformedRadiusVector.getY());
        }
        else {
            ClosedInterval interval = ClosedInterval.of(-2, 5);
            double mP = interval.clip(object.magnitude());
            double f = (99 - (17 * mP)) / 140.0;
            double discRadius =f * projection.applyToAngle(object.angularSize())/2.0;
            Point2D transformedRadiusVector = planeToCanvas.deltaTransform(new Point2D(discRadius,0));
            return EuclidianDistance.norm(transformedRadiusVector.getX(),transformedRadiusVector.getY());
        }
    }

    private Point2D transformCoordinates(double x, double y, Transform planeToCanvas){
        Point2D unTransformedVector = new Point2D(x,y);
        return planeToCanvas.transform(unTransformedVector);
    }

    /**
     * clears canvas
     * @return void
     */
    public void clear(){
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * draws stars and asterism
     * @param sky
     * @param projection
     * @param planeToAffine
     * @return void
     */
    public void drawStars(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){
        double[] starCoordinates = sky.starCoordinates();
        int i =0;
        for(Star s:sky.stars()){
            double discRadius = this.transformedDiscRadius(s,projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(starCoordinates[i],starCoordinates[i+1],planeToAffine);
            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));
            ctx.fillOval(transformedCoordinates.getX(),transformedCoordinates.getY(),discRadius,discRadius);
        }

    }









}
