package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;

public class SkyCanvasManager {
    // all properties defined here
    private ObjectProperty<Canvas> canvas;
    private ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();


    //below are all observable and binded objects
    private ObservableObjectValue<Transform> planeToScene;
    private ObservableObjectValue<SkyCanvasPainter> painter;
    private ObservableObjectValue<ObservedSky> sky;
    private ObservableObjectValue<StereographicProjection> projection;

    // the sole function of the constructor is to define bindings and add listeners
    public SkyCanvasManager(StarCatalogue catalogue, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters, DateTimeBean when){
            this.canvas = new SimpleObjectProperty<>(new Canvas ( 800, 600 ));
            // eventListener to set new mouse position
            canvas.get().setOnMouseMoved(e -> this.setMousePosition(e));


            // we define all of our bindings here
            this.painter =  Bindings.createObjectBinding(()-> new SkyCanvasPainter(this.canvas.get()),
                    this.canvas);

            this.projection = Bindings.createObjectBinding(
                    ()-> new StereographicProjection(viewingParameters.getProjectionCenter()),
                    viewingParameters.projectionCenterProperty());

            this.sky = Bindings.createObjectBinding(()-> new ObservedSky(when.getZonedDateTime().get(),observerLocation.getCoordinates().get(), this.projection.get(), catalogue),
                    when.getZonedDateTime(),observerLocation.getCoordinates(),this.projection);

    };

    // this method will be called whenever mouse moves to set a new mouse position
    private void setMousePosition(MouseEvent e){
        mousePosition.setValue(new Point2D(e.getX(),e.getY()));
    }




}
