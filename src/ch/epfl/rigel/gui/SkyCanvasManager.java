package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
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


public class SkyCanvasManager {

    //constants defined here
    private final double maxObjectDist = 10;
    private final RightOpenInterval azInterval = RightOpenInterval.of(0,360);
    private final ClosedInterval altInterval = ClosedInterval.symmetric(180);

    // all properties defined here
    private ObjectProperty<Canvas> canvas = new SimpleObjectProperty<>(new Canvas ( 800, 600 ));
    private ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(new Point2D(100,120));
    private ViewingParametersBean viewParam;

    //initial value of fieldOfView is 100 degrees
    private ClosedInterval fieldOfViewRange = ClosedInterval.of(30,150);

    //below are all observable and binded objects
    private ObservableObjectValue<Transform> planeToCanvas;
    private ObservableObjectValue<SkyCanvasPainter> painter;
    private ObservableObjectValue<ObservedSky> sky;
    private ObservableObjectValue<StereographicProjection> projection;
    private ObservableObjectValue<HorizontalCoordinates> mouseHorizontalCoordinates;

    //three properties below are made public through getters
    private ObservableObjectValue<CelestialObject> objectUnderMouse;
    private ObservableDoubleValue mouseAltDeg;
    private ObservableDoubleValue mouseAzDeg;

    // the sole function of the constructor is to define bindings and add listeners
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean when, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters){
            this.viewParam = viewingParameters;
            this.viewParam.setFieldOfViewDeg(90);


            // we define all of our bindings here
            this.projection = Bindings.createObjectBinding(
                    ()-> new StereographicProjection(viewingParameters.getProjectionCenter()),
                    viewingParameters.projectionCenterProperty());

            this.sky = Bindings.createObjectBinding(()-> new ObservedSky(when.getZonedDateTime().get(),observerLocation.getCoordinates().get(), this.projection.get(), catalogue),
                    when.getZonedDateTime(),observerLocation.getCoordinates(),this.projection);

            this.planeToCanvas = Bindings.createObjectBinding(() -> {
                double dilation = (canvas.get().getWidth())/(this.projection.get().applyToAngle(Angle.ofDeg(this.viewParam.getFieldOfView())));
                return Transform.affine(dilation,0,0,-1*dilation,canvas.get().getWidth()/2.0,canvas.get().getHeight()/2);
                    },
                    canvas,viewParam.fieldOfViewProperty()
            );

            this.objectUnderMouse = Bindings.createObjectBinding(()->
                    this.sky.get().objectClosestTo(CartesianCoordinates.of(this.getMouseX(),this.getMouseY()),this.maxObjectDist),mousePosition);

            this.mouseHorizontalCoordinates = Bindings.createObjectBinding(()->{
                CartesianCoordinates inverseTransform = CartesianCoordinates.of(this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getX(),this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getY());
                return this.projection.get().inverseApply(inverseTransform);
            },planeToCanvas,projection,mousePosition);

        this.painter =  Bindings.createObjectBinding(()-> new SkyCanvasPainter(this.canvas.get()),
                this.canvas,this.sky,this.planeToCanvas,this.projection);

            this.mouseAltDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().altDeg()),mouseHorizontalCoordinates);
            this.mouseAzDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().azDeg()),mouseHorizontalCoordinates);


            // adding mouse related event listeners
            canvas.get().setOnMouseMoved(e -> this.setMousePosition(e));
            canvas.get().setOnScroll(e -> this.setFieldOfView(e));

            //adding keyboard related event listeners
            canvas.get().setOnKeyPressed(e -> this.setProjectionCenter(e));

            //painterListener here
            this.paintSky();
            painter.addListener((observable)->this.paintSky());
    };

    // this method will be called whenever mouse moves to set a new mouse position
    private void setMousePosition(MouseEvent e){
        mousePosition.setValue(new Point2D(e.getX(),e.getY()));
    }

    // this method sets the field of view to a number within the given range and based on the mouse scroll
    private void setFieldOfView(ScrollEvent e){
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
                System.out.println(currentX);
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
        painter.get().drawStars(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawHorizon( sky.get(),projection.get(), planeToCanvas.get());
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
}
