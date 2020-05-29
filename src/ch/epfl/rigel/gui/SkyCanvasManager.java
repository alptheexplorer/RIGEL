package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.EuclidianDistance;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Transform;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 * responsible of drawing the sky
 */
public class SkyCanvasManager {

    //constants defined here
    private final double maxObjectClosestDistance = 10;
    private final RightOpenInterval azInterval = RightOpenInterval.of(0,360);
    private final ClosedInterval altInterval = ClosedInterval.of(5,90);

    // all properties defined here --> these are values that do not need to be observable 
    private ObjectProperty<Canvas> canvas = new SimpleObjectProperty<>(new Canvas ( 800, 600 ));
    private ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(new Point2D(100,120));
    private ViewingParametersBean viewParam;

    //initial value of fieldOfView is 100 degrees
    private ClosedInterval fieldOfViewRange = ClosedInterval.of(30,150);

    private ObservableObjectValue<Transform> planeToCanvas;
    private ObservableObjectValue<SkyCanvasPainter> painter;
    private ObservableObjectValue<ObservedSky> sky;
    private ObservableObjectValue<StereographicProjection> projection;
    private ObservableObjectValue<HorizontalCoordinates> mouseHorizontalCoordinates;
    private ObservableObjectValue<CelestialObject> objectUnderMouse;
    private ObservableDoubleValue mouseAltDeg;
    private ObservableDoubleValue mouseAzDeg;
    private ObservableDoubleValue transformedMaxObjectClosestDistance;

    //constructor defines bindings and adds listener to draw sky
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean when, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters){
            this.viewParam = viewingParameters;
            this.viewParam.setFieldOfViewDeg(90);

            // we define all of our bindings here
            this.projection = Bindings.createObjectBinding(
                    ()-> new StereographicProjection(viewingParameters.getProjectionCenter()),
                    viewingParameters.projectionCenterProperty());

            this.planeToCanvas = Bindings.createObjectBinding(() -> {
                double dilation = (canvas.get().getWidth())/(this.projection.get().applyToAngle(Angle.ofDeg(this.viewParam.getFieldOfView())));
                return Transform.affine(dilation,0,0,-1*dilation,canvas.get().getWidth()/2.0,canvas.get().getHeight()/2);
                    },
                    canvas,viewParam.fieldOfViewProperty()
            );

        this.sky = Bindings.createObjectBinding(()-> new ObservedSky(when.getZonedDateTime().get(),observerLocation.getCoordinates().get(), this.projection.get(), catalogue),
                when.dateProperty(),when.zoneProperty(),when.timeProperty(),observerLocation.lonDegProperty(),observerLocation.latDegProperty(),this.projection,this.canvas.get().heightProperty(),this.canvas.get().widthProperty(),this.planeToCanvas);


        // transforming the arg to be passed to objectclosesto
        this.transformedMaxObjectClosestDistance = Bindings.createDoubleBinding(()->{
                    Point2D inverseVector = this.planeToCanvas.get().inverseTransform(10,0);
                    return EuclidianDistance.norm(inverseVector.getX(),inverseVector.getY());
        }
                , planeToCanvas);

            this.objectUnderMouse = Bindings.createObjectBinding(()->
                    this.sky.get().objectClosestTo(CartesianCoordinates.of(planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getX(), planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getY()),this.transformedMaxObjectClosestDistance.get()),mousePosition,planeToCanvas,transformedMaxObjectClosestDistance);

            this.mouseHorizontalCoordinates = Bindings.createObjectBinding(()->{
                CartesianCoordinates inverseTransform = CartesianCoordinates.of(this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getX(),this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getY());
                return this.projection.get().inverseApply(inverseTransform);
            },planeToCanvas,projection,mousePosition);

            this.painter =  Bindings.createObjectBinding(()-> new SkyCanvasPainter(this.canvas.get()),
                    this.canvas.get().heightProperty(),this.canvas.get().widthProperty(),this.sky,this.planeToCanvas,this.projection);

            this.mouseAltDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().altDeg()),mouseHorizontalCoordinates);
            this.mouseAzDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().azDeg()),mouseHorizontalCoordinates);


            // adding mouse related event listeners
            canvas.get().setOnMouseMoved(e -> this.setMousePosition(e));
            canvas.get().setOnScroll(e -> this.setFieldOfView(e));

            //adding keyboard related event listeners
            canvas.get().setOnKeyPressed(e -> this.setProjectionCenter(e));

            //main listener here
            sky.addListener((observable) -> this.paintSky());
    };

    // this method will be called whenever mouse moves to set a new mouse position
    private void setMousePosition(MouseEvent e){
        e.consume();
        mousePosition.setValue(new Point2D(e.getX(),e.getY()));
    }

    // this method sets the field of view to a number within the given range and based on the mouse scroll
    private void setFieldOfView(ScrollEvent e){
        e.consume();
        if(Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())){
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaX();
            if(this.fieldOfViewRange.contains(newFieldOfView)){
                this.viewParam.setFieldOfViewDeg(newFieldOfView);
            }
        }
        else{
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaY();
            if(this.fieldOfViewRange.contains(newFieldOfView)){
                this.viewParam.setFieldOfViewDeg(newFieldOfView);
            }
        }
    }

    // updates projection center based on keyboard input
    private void setProjectionCenter(KeyEvent e){
        // consume event to avoid self interpretation
        e.consume();
        // call to focus
        canvas.get().requestFocus();
        double currentX = Angle.toDeg(viewParam.getProjectionCenter().az());
        double currentY = Angle.toDeg(viewParam.getProjectionCenter().alt());
        switch (e.getCode()){
            case LEFT:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(azInterval.reduce(currentX-10),currentY));
                break;
            case RIGHT:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(azInterval.reduce(currentX+10),currentY));
                break;
            case DOWN:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(currentX,altInterval.reduce(currentY-5)));
                break;
            case UP:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(currentX,altInterval.reduce(currentY+5)));
                break;
        }

    }

    private void paintSky(){
        painter.get().drawHorizon(sky.get(),projection.get(), planeToCanvas.get());
        painter.get().drawStars(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawPlanets(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawSun(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawMoon(sky.get(), projection.get(), planeToCanvas.get());
    }

    /**
     *
     * @return canvas property
     */
    public ObjectProperty<Canvas> canvasProperty() {
        return canvas;
    }

    private double getMouseX(){
        return this.mousePosition.get().getX();
    }

    private double getMouseY(){
        return this.mousePosition.get().getY();
    }


    public ObservableObjectValue<CelestialObject> objectUnderMouseProperty(){
        return this.objectUnderMouse;
    }

    /**
     *
     * @return content of Canvas property
     */
    public Canvas canvas(){
        return this.canvas.get();
    }


    public Number getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    public Number getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }



}
