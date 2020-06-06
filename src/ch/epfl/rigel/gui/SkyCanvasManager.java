package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.*;
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
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 * responsible of drawing the sky
 */
public class SkyCanvasManager {

    //constants and simple objects defined here
    private static final double MAX_OBJECT_CLOSEST_DISTANCE = 10;
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0,360);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(5,90);
    private final Canvas canvas = new Canvas ( 800, 600 );

    private static final Polynomial MAGNITUDE_CURVE = Polynomial.of(0.000005707,-0.00125, -0.02349, 16);
    private static final Polynomial RGBBLUE_CURVE = Polynomial.of(-0.00023,0.0647, -3.06,40 );
    private static final Polynomial RGBGREEN_CURVE = Polynomial.of(-0.000008384,0.00227894, -0.09159, 4.438179);
    private static final ClosedInterval RGBRANGE = ClosedInterval.of(0,255);

    // all properties defined here --> these are values that do not need to be observable
    private ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(new Point2D(100,120));
    private ViewingParametersBean viewParam;

    //initial value of fieldOfView is 100 degrees
    private ClosedInterval fieldOfViewRange = ClosedInterval.of(25,150);

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
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean when, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters, SkyModeBean skyMode, MagnitudeBean magnitude, BackgroundRgbBean backgroundRgb){
        this.viewParam = viewingParameters;
        canvas.requestFocus();
        magnitude.setMagnitude(MAGNITUDE_CURVE.at(viewingParameters.getFieldOfView()));


        // we define all of our bindings here
        this.projection = Bindings.createObjectBinding(
                ()-> new StereographicProjection(viewingParameters.getProjectionCenter()),
                viewingParameters.projectionCenterProperty());

        this.planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilation = (canvas.getWidth())/(this.projection.get().applyToAngle(Angle.ofDeg(this.viewParam.getFieldOfView())));
                    return Transform.affine(dilation,0,0,-1*dilation,canvas.getWidth()/2.0,canvas.getHeight()/2);
                },
                canvas.getProperties(),viewParam.fieldOfViewProperty()
        );

        this.sky = Bindings.createObjectBinding(()-> new ObservedSky(when.getZonedDateTime().get(),observerLocation.getCoordinates().get(), this.projection.get(), catalogue),
                when.dateProperty(),when.zoneProperty(),when.timeProperty(),observerLocation.lonDegProperty(),observerLocation.latDegProperty(),this.projection,this.canvas.heightProperty(),this.canvas.widthProperty(),this.planeToCanvas, skyMode.modeProperty(), backgroundRgb.backgroundColorProperty(), magnitude.magnitudeProperty());


        // transforming the arg to be passed to objectclosesto
        this.transformedMaxObjectClosestDistance = Bindings.createDoubleBinding(()->{
                    Point2D inverseVector = this.planeToCanvas.get().inverseTransform(MAX_OBJECT_CLOSEST_DISTANCE,0);
                    return EuclidianDistance.norm(inverseVector.getX(),inverseVector.getY());
                }
                , planeToCanvas);

        this.objectUnderMouse = Bindings.createObjectBinding(()->
                this.sky.get().objectClosestTo(CartesianCoordinates.of(planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getX(), planeToCanvas.get().inverseTransform(this.getMouseX(),this.getMouseY()).getY()),this.transformedMaxObjectClosestDistance.get()),mousePosition,planeToCanvas,transformedMaxObjectClosestDistance);

        this.mouseHorizontalCoordinates = Bindings.createObjectBinding(()->{
            CartesianCoordinates inverseTransform = CartesianCoordinates.of(this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getX(),this.planeToCanvas.get().inverseTransform(getMouseX(),getMouseY()).getY());
            return this.projection.get().inverseApply(inverseTransform);
        },planeToCanvas,projection,mousePosition);

        this.painter =  Bindings.createObjectBinding(()-> new SkyCanvasPainter(this.canvas),
                this.canvas.heightProperty(),this.canvas.widthProperty(),this.sky,this.planeToCanvas,this.projection);

        this.mouseAltDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().altDeg()),mouseHorizontalCoordinates);
        this.mouseAzDeg = Bindings.createDoubleBinding(()->(mouseHorizontalCoordinates.get().azDeg()),mouseHorizontalCoordinates);


        // adding mouse related event listeners
        canvas.setOnMouseMoved(e -> this.setMousePosition(e));
        canvas.setOnScroll(e -> {
            this.setFieldOfView(e);
            this.setStarMagAndRgb(e, magnitude, backgroundRgb);
        });

        //adding keyboard related event listeners
        canvas.setOnMousePressed(e -> this.setFocus(e,canvas)); // ENSURES THAT ONLY RIGHT CLICK GRANTS FOCUS
        canvas.setOnKeyPressed(e -> this.setProjectionCenter(e));

        //main listener here
        sky.addListener((observable) -> this.paintSky(skyMode, magnitude, backgroundRgb));
    };

    private void setFocus(MouseEvent e, Canvas canvas){
        e.consume();
        if(e.isPrimaryButtonDown()){
            canvas.requestFocus();
        }
    }

    // this method will be called whenever mouse moves to set a new mouse position
    private void setMousePosition(MouseEvent e){
        e.consume();
        mousePosition.setValue(new Point2D(e.getX(),e.getY()));
    }

    //sets a new background RGB and a new maximum stat magnitude upon user scroll
    private void setStarMagAndRgb(ScrollEvent e, MagnitudeBean magnitude, BackgroundRgbBean backgroundRgb){
        e.consume();
        magnitude.setMagnitude(MAGNITUDE_CURVE.at(viewParam.getFieldOfView()));
        int green = (int)Math.floor(RGBRANGE.clip(RGBGREEN_CURVE.at(viewParam.getFieldOfView())));
        int blue = (int)Math.floor(RGBRANGE.clip(viewParam.getFieldOfView()/1.5));
        int red = 0;
        System.out.println(blue);
        backgroundRgb.setBackgroundColor(Color.rgb(red,green,blue));

    }


    // this method sets the field of view to a number within the given range and based on the mouse scroll
    private void setFieldOfView(ScrollEvent e){
        e.consume();
        if(Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())){
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaX()/10.0;
            if(this.fieldOfViewRange.contains(newFieldOfView)){
                this.viewParam.setFieldOfViewDeg(newFieldOfView);
            }
        }
        else{
            double newFieldOfView = viewParam.getFieldOfView() + e.getDeltaY()/10.0;
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
        canvas.requestFocus();
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


    private void paintSky(SkyModeBean skyMode, MagnitudeBean magnitude, BackgroundRgbBean color){

        painter.get().clear(color);
        if(!skyMode.isIsNonAsterism()){
            painter.get().drawAsterisms(sky.get(), projection.get(), planeToCanvas.get());
        }
        painter.get().drawStars(sky.get(), projection.get(), planeToCanvas.get(), magnitude.getMagnitude());
        if(!skyMode.isIsNonPlanet()){
            painter.get().drawPlanets(sky.get(), projection.get(), planeToCanvas.get());
        }
        painter.get().drawSun(sky.get(), projection.get(), planeToCanvas.get());
        painter.get().drawMoon(sky.get(), projection.get(), planeToCanvas.get());
        if(!skyMode.isIsNonHorizon()){
            painter.get().drawHorizon(sky.get(),projection.get(), planeToCanvas.get());
        }



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
        return this.canvas;
    }


    /**
     *
     * @return mouseAzDegProperty
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }


    /**
     *
     * @return mouseAltDegProperty
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }



}
