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
 * Responsible of drawing the sky
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public class SkyCanvasManager {

    //constants defined here
    private static final double maxObjectClosestDistance = 10;
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0,360);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(5,90);

    // all properties defined here --> these are values that do not need to be observable 
    private static final  ObjectProperty<Canvas> CANVAS = new SimpleObjectProperty<>(new Canvas ( 800, 600 ));
    private static final ObjectProperty<Point2D> MOUSE_POSITION = new SimpleObjectProperty<>(new Point2D(100,120));
    private ViewingParametersBean viewParam;

    //initial value of fieldOfView is 100 degrees
    private static final ClosedInterval FIELD_OF_VIEW_RANGE = ClosedInterval.of(30,150);

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
                double dilation = (CANVAS.get().getWidth())/(this.projection.get().applyToAngle(Angle.ofDeg(this.viewParam.getFieldOfView())));
                return Transform.affine(dilation,0,0,-1*dilation, CANVAS.get().getWidth()/2.0, CANVAS.get().getHeight()/2);
                    },
                    CANVAS,viewParam.fieldOfViewProperty()
            );

        this.sky = Bindings.createObjectBinding(()-> new ObservedSky(when.getZonedDateTime().get(),observerLocation.getCoordinates().get(), this.projection.get(), catalogue),
                when.dateProperty(),when.zoneProperty(),when.timeProperty(),observerLocation.lonDegProperty(),observerLocation.latDegProperty(),this.projection,this.CANVAS.get().heightProperty(),this.CANVAS.get().widthProperty(),this.planeToCanvas);


        // transforming the arg to be passed to objectclosesto
        this.transformedMaxObjectClosestDistance = Bindings.createDoubleBinding(()->{
                    Point2D inverseVector = this.planeToCanvas.get().inverseTransform(10,0);
                    return EuclidianDistance.norm(inverseVector.getX(),inverseVector.getY());
        }
                , planeToCanvas);

            this.objectUnderMouse = Bindings.createObjectBinding(()->
                    this.sky.get().objectClosestTo(CartesianCoordinates.of(planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getX(), planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getY()),this.transformedMaxObjectClosestDistance.get()), MOUSE_POSITION,planeToCanvas,transformedMaxObjectClosestDistance);

            this.mouseHorizontalCoordinates = Bindings.createObjectBinding(()->{
                CartesianCoordinates inverseTransform = CartesianCoordinates.of(this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getX(),this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getY());
                return this.projection.get().inverseApply(inverseTransform);
            },planeToCanvas,projection, MOUSE_POSITION);

            this.painter =  Bindings.createObjectBinding(()-> new SkyCanvasPainter(this.CANVAS.get()),
                    this.CANVAS.get().heightProperty(),this.CANVAS.get().widthProperty(),this.sky,this.planeToCanvas,this.projection);

            this.mouseAltDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().altDeg()),mouseHorizontalCoordinates);
            this.mouseAzDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().azDeg()),mouseHorizontalCoordinates);


            // adding mouse related event listeners
            CANVAS.get().setOnMouseMoved(e -> this.setMousePosition(e));
            CANVAS.get().setOnScroll(e -> this.setFieldOfView(e));

            //adding keyboard related event listeners
            CANVAS.get().setOnKeyPressed(e -> this.setProjectionCenter(e));

            //main listener here
            sky.addListener((observable) -> this.paintSky());
    };

    // this method will be called whenever mouse moves to set a new mouse position
    private void setMousePosition(MouseEvent e){
        e.consume();
        MOUSE_POSITION.setValue(new Point2D(e.getX(),e.getY()));
    }

    // this method sets the field of view to a number within the given range and based on the mouse scroll
    private void setFieldOfView(ScrollEvent e){
        e.consume();
        if(Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())){
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaX();
            if(this.FIELD_OF_VIEW_RANGE.contains(newFieldOfView)){
                this.viewParam.setFieldOfViewDeg(newFieldOfView);
            }
        }
        else{
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaY();
            if(this.FIELD_OF_VIEW_RANGE.contains(newFieldOfView)){
                this.viewParam.setFieldOfViewDeg(newFieldOfView);
            }
        }
    }

    // updates projection center based on keyboard input
    private void setProjectionCenter(KeyEvent e){
        // consume event to avoid self interpretation
        e.consume();
        // call to focus
        CANVAS.get().requestFocus();
        double currentX = Angle.toDeg(viewParam.getProjectionCenter().az());
        double currentY = Angle.toDeg(viewParam.getProjectionCenter().alt());
        switch (e.getCode()){
            case LEFT:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(currentX-10),currentY));
                break;
            case RIGHT:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(currentX+10),currentY));
                break;
            case DOWN:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(currentX, ALT_INTERVAL.clip(currentY-5)));
                break;
            case UP:
                viewParam.setCenter(HorizontalCoordinates.ofDeg(currentX, ALT_INTERVAL.clip(currentY+5)));
                break;
        }

    }

    private void paintSky(){
        painter.get().drawStars(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawPlanets(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawSun(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawMoon(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawHorizon(sky.get(),projection.get(), planeToCanvas.get());
    }


    private double getMouseX(){
        return this.MOUSE_POSITION.get().getX();
    }

    private double getMouseY(){
        return this.MOUSE_POSITION.get().getY();
    }


    /**
     *
     * @return object under mouse
     */
    public ObservableObjectValue<CelestialObject> objectUnderMouseProperty(){
        return this.objectUnderMouse;
    }

    /**
     *
     * @return content of Canvas property
     */
    public Canvas canvas(){
        return this.CANVAS.get();
    }


    /**
     *
     * @return mouse az deg property
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }


    /**
     *
     * @return mouse alt deg property
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }



}
