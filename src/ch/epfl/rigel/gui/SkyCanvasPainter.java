
package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.EuclidianDistance;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * Paints onto a given canvas
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    /**
     *
     * @param canvas
     */
    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        //setting black background
        this.clear();
    }


    // returns transformed diameter of celestial objects (ignores traslation and treats diameter as vector)
    private double transformedDiscDiameter(CelestialObject object, StereographicProjection projection, Transform planeToCanvas){
        double halfDegreeDiameter = projection.applyToAngle(Angle.ofDeg(0.5));

        if((object instanceof Sun) || (object instanceof Moon)){
            Point2D transformedDiameterVector = planeToCanvas.deltaTransform(new Point2D(halfDegreeDiameter,0));
            return EuclidianDistance.norm(transformedDiameterVector.getX(),transformedDiameterVector.getY());
        }
        else {
            ClosedInterval interval = ClosedInterval.of(-2, 5);
            double mP = interval.clip(object.magnitude());
            double f = (99 - (17 * mP)) / 140.0;
            double discDiameter = f*halfDegreeDiameter;
            Point2D transformedDiameterVector = planeToCanvas.deltaTransform(new Point2D(discDiameter,0));
            return EuclidianDistance.norm(transformedDiameterVector.getX(),transformedDiameterVector.getY());
        }
    }

    //returns transformed coordinates ( ignores dilatation being just a point )
    private Point2D transformCoordinates(double x, double y, Transform planeToCanvas){
        Point2D pointToTransform = new Point2D(x,y);
        return planeToCanvas.transform(pointToTransform);
    }

    /**
     * clears canvas and set black background
     * @return void
     */
    public void clear(){
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill (Color.BLACK);
        ctx.fillRect ( 0 , 0 , canvas.getWidth (), canvas.getHeight());
    }

    /**
     * draws stars and asterisms
     * @param sky
     * @param projection
     * @param planeToAffine
     * @return void
     */
    public void drawStars(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){
        //draw Asterisms
        Bounds canvasBound = canvas.getBoundsInLocal();
        for(Asterism a: sky.asterisms()){
            ctx.beginPath();
            ctx.setLineWidth(1);
            int i = 0;
            int j = 1;
            while(j < sky.starIndices(a).size()){
                //current and previous index
                int currentIndex = sky.starIndices(a).get(i);
                int nextIndex = sky.starIndices(a).get(j);
                //transformed coordinates of the current and previous star
                Point2D currentPos = transformCoordinates(sky.starPositions()[2*currentIndex], sky.starPositions()[2*currentIndex+1], planeToAffine);
                Point2D nextPos = transformCoordinates(sky.starPositions()[2*nextIndex], sky.starPositions()[2*nextIndex+1], planeToAffine);
                //move head
                ctx.moveTo(currentPos.getX(),currentPos.getY());
                //draw line if at least one between the current and the previous star is inside the canvas bounds
                if(canvasBound.contains(currentPos.getX(),currentPos.getY()) || canvasBound.contains(nextPos.getX(), nextPos.getY())){
                    ctx.lineTo(nextPos.getX(), nextPos.getY());
                }
                ++i;
                ++j;
            }
            ctx.closePath();
            ctx.setStroke(Color.BLUE);
            ctx.stroke();
        }
        //draw stars
        int i=0;
        for(Star s:sky.stars()){
            double discDiameter = transformedDiscDiameter(s,projection,planeToAffine);
            Point2D transformedCoordinates = transformCoordinates(sky.starPositions()[i],
                    sky.starPositions()[i+1],planeToAffine);
            ctx.setFill(BlackBodyColor.colorForTemperature(s.colorTemperature()));
            ctx.fillOval(transformedCoordinates.getX() - (discDiameter/2),
                    transformedCoordinates.getY() - (discDiameter/2), discDiameter, discDiameter);
            i += 2;
        }
    }



    /**
     * draws planets to canvas
     * @param sky
     * @param projection
     * @param planeToAffine
     */
    public void drawPlanets(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){

        int i = 0;
        for(Planet planet:sky.planets()){
            double discDiameter = this.transformedDiscDiameter(planet,projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(sky.planetPositions()[i],
                    sky.planetPositions()[i+1],planeToAffine);
            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(transformedCoordinates.getX() - (discDiameter/2),
                    transformedCoordinates.getY() - (discDiameter/2), discDiameter,discDiameter);
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

        double discDiameter = this.transformedDiscDiameter(sky.sun(),projection,planeToAffine);
        Point2D transformedCoordinates = this.transformCoordinates(sky.sunPosition().x(),sky.sunPosition().y(),planeToAffine);
        // drawing smallest disc
        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoordinates.getX() - (discDiameter/2),
                transformedCoordinates.getY() - (discDiameter/2),discDiameter,discDiameter);
        //drawing middle size disc
        ctx.setFill(Color.YELLOW);
        ctx.fillOval(transformedCoordinates.getX() - (discDiameter+2)/2,
                transformedCoordinates.getY() - (discDiameter+2)/2,(discDiameter+2),(discDiameter+2));
        //drawing largest disc: the halo
        ctx.setFill(Color.YELLOW.deriveColor(0,1,1,0.25));
        ctx.fillOval(transformedCoordinates.getX() - (discDiameter*2.2)/2,
                transformedCoordinates.getY() - (discDiameter*2.2)/2,(discDiameter*2.2),(discDiameter*2.2));
    }

    /**
     * draws moon to canvas
     * @param sky
     * @param projection
     * @param planeToAffine
     */

    public void drawMoon(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){
        double discDiameter = this.transformedDiscDiameter(sky.moon(),projection,planeToAffine);
        Point2D transformedCoordinates = this.transformCoordinates(sky.moonPosition().x(),sky.moonPosition().y(),planeToAffine);
        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoordinates.getX() - (discDiameter/2),
                transformedCoordinates.getY() - (discDiameter/2),discDiameter,discDiameter);
    }

    /**
     * draws horizon and annotation of cardinal points
     * @param sky
     * @param projection
     * @param planeToAffine
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToAffine){
        HorizontalCoordinates horizonCoord = HorizontalCoordinates.ofDeg(0, 0);
        double discRadius = projection.circleRadiusForParallel(horizonCoord);
        CartesianCoordinates circleCenter = projection.circleCenterForParallel(horizonCoord);
        double transformedRadius = planeToAffine.deltaTransform(new Point2D(discRadius,0)).getX();
        Point2D transformedCoordinates = this.transformCoordinates(circleCenter.x(),circleCenter.y(),planeToAffine);
        double horizonX = transformedCoordinates.getX() - transformedRadius;
        double horizonY = transformedCoordinates.getY() - transformedRadius;
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(horizonX,horizonY, transformedRadius*2, transformedRadius*2);

        //adding horizon annotations
        ctx.setFill(Color.RED);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.TOP);

        for(double azDeg = 0; azDeg <360; azDeg+=45){
            HorizontalCoordinates horizCoord = HorizontalCoordinates.ofDeg(azDeg,-0.5);
            CartesianCoordinates cartesianCoord = projection.apply(horizCoord);
            Point2D canvasCoordinates = transformCoordinates(cartesianCoord.x(),cartesianCoord.y(),planeToAffine);
            ctx.fillText(horizCoord.azOctantName("N","E","S","O"), canvasCoordinates.getX(),canvasCoordinates.getY());
        }

    }
}














