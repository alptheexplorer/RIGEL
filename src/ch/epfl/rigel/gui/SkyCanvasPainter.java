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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;

public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;
    private Point2D[] transformedStarPos;

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

    /**
     * draws stars and asterism
     * @param sky
     * @param projection
     * @param planeToAffine
     * @return void
     */
    public void drawStars(ObservedSky sky,StereographicProjection projection, Transform planeToAffine){
        this.transformedStarPos = new Point2D[sky.stars().length];
        int i =0;
        int j = 0;
        for(Star s:sky.stars()){
            double discRadius = this.transformedDiscDiameter(s,projection,planeToAffine);
            Point2D transformedCoordinates = this.transformCoordinates(sky.starCoordinates()[i],sky.starCoordinates()[i+1],planeToAffine);
            //we store transformedstarcoordinates in an array to use when drawing asterism later.
            transformedStarPos[j] =transformedCoordinates;
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

    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToAffine){
        HorizontalCoordinates horizonCoord = HorizontalCoordinates.ofDeg(0, 23);
        double discRadius = projection.circleRadiusForParallel(horizonCoord);
        CartesianCoordinates circleCenter = projection.circleCenterForParallel(horizonCoord);
        double transformedRadius = planeToAffine.deltaTransform(new Point2D(discRadius,discRadius)).getX();
        Point2D transformedCoordinates = this.transformCoordinates(circleCenter.x(),circleCenter.y(),planeToAffine);
        double horizonX = transformedCoordinates.getX() - transformedRadius;
        double horizonY = transformedCoordinates.getY() + transformedRadius;
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(horizonX,horizonY, transformedRadius*2, transformedRadius*2);
        ctx.setLineWidth(1);
        //adding horizon annotations

        ctx.setFill(Color.RED);
        ctx.setTextAlign(TextAlignment.CENTER);
        // all 8 annotations
        ctx.fillText("S",horizonX+transformedRadius,horizonY);
        ctx.fillText("SE",horizonX + transformedRadius + transformedRadius*Math.cos(Angle.ofDeg(45)),horizonY+(transformedRadius - transformedRadius*Math.sin(Angle.ofDeg(45))));
        ctx.fillText("E",horizonX+transformedRadius*2,horizonY+transformedRadius);
        ctx.fillText("NE",horizonX + transformedRadius*Math.cos(Angle.ofDeg(45)),horizonY+(transformedRadius + transformedRadius*Math.sin(Angle.ofDeg(45))));
        ctx.fillText("N",horizonX+transformedRadius,horizonY+transformedRadius*2);
        ctx.fillText("NO", horizonX + (transformedRadius-transformedRadius*Math.cos(Angle.ofDeg(45))),horizonY + transformedRadius + transformedRadius*Math.sin(Angle.ofDeg(45)));
        ctx.fillText("O",horizonX,horizonY+transformedRadius);
        ctx.fillText("SO",horizonX+(transformedRadius-transformedRadius*Math.cos(Angle.ofDeg(45))),horizonY+(transformedRadius - transformedRadius*Math.sin(Angle.ofDeg(45))));


        /** some test code
        ctx.beginPath();
        ctx.moveTo(100,100);
        ctx.lineTo(200,200);
        ctx.moveTo(200,200);
        ctx.lineTo(300,500);
        ctx.closePath();
        ctx.setStroke(Color.BLUE);
        ctx.stroke();
         */
        //finally we draw asterisms
        Bounds canvasBound = canvas.getBoundsInLocal();

        for(Asterism a:sky.asterisms()){
            //Path path = new Path();
            ctx.beginPath();
            List<Integer> currentStarIndex = sky.starIndices(a);
            int j = 0;
            for(int i:currentStarIndex){
                // we move our ctxHead to wherever the current Star is positioned
                if(j == 0){
                    ctx.moveTo(this.transformedStarPos[i].getX(),this.transformedStarPos[i].getY());
                    j+=1;
                }
                else{
                    // we make sure here to not draw a line if both start are outside of the canvas
                    if(!canvasBound.contains(this.transformedStarPos[i-1].getX(),this.transformedStarPos[i-1].getY())&&!canvasBound.contains(this.transformedStarPos[i].getX(),this.transformedStarPos[i].getY())){
                        continue;
                    }
                    else{
                        // we then draw a line from current head to the next star
                        ctx.lineTo(this.transformedStarPos[i].getX(),this.transformedStarPos[i].getY());
                        // then we move pathHead to that star
                        ctx.moveTo(this.transformedStarPos[i].getX(),this.transformedStarPos[i].getY());
                    }
                }
            }
            ctx.closePath();
            ctx.setStroke(Color.BLUE);
            ctx.stroke();
        }
    }
}


















