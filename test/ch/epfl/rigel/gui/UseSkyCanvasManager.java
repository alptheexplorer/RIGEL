package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;

public  final  class UseSkyCanvasManager extends Application {
    public static void main (String [] args)     {launch (args); }

    private InputStream resourceStream (String resourceName)  {
        return getClass (). getResourceAsStream (resourceName);
    }

    @Override
    public void start (Stage primaryStage) throws IOException {
        try (InputStream hs = resourceStream ( "/hygdata_v3.csv" );
                InputStream as = resourceStream ( "/asterisms.txt" ) ) {
            StarCatalogue catalog = new StarCatalogue.Builder ()
                    .loadFrom (hs, HygDatabaseLoader.INSTANCE).loadFrom(as, AsterismLoader.INSTANCE).build();

            ZonedDateTime when =
                    ZonedDateTime.parse ( "2020-02-17T20:15:00+01:00" );
            DateTimeBean dateTimeBean = new DateTimeBean ();
            dateTimeBean.setZonedDateTime (when);

            ObserverLocationBean observeLocationBean =
                    new ObserverLocationBean ();
            observeLocationBean.setCoordinates (
                    GeographicCoordinates.ofDeg ( 6.57 , 46.52 ));

            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean ();
            viewingParametersBean.setCenter (
                    HorizontalCoordinates.ofDeg ( 180 , 42 ));
            viewingParametersBean.setFieldOfViewDeg ( 70 );

            SkyCanvasManager canvasManager = new SkyCanvasManager (
                    catalog,
                    dateTimeBean,
                    observeLocationBean,
                    viewingParametersBean);

            canvasManager.objectUnderMouseProperty (). addListener (
                    (p, o, n) -> { if (n!= null ) {System.out.println (n);}
                    else{
                        System.out.println("n");
                    };});

            Canvas sky = canvasManager.canvas();
            BorderPane root = new BorderPane (sky);

            /** this line causes error for some weird reason
            sky.widthProperty (). bind (root.widthProperty ());
            sky.heightProperty (). bind (root.heightProperty ());
             */

            primaryStage.setMinWidth ( 800 );
            primaryStage.setMinHeight ( 600 );

            primaryStage.setY ( 100 );

            primaryStage.setScene ( new Scene(root));
            primaryStage.show ();

            sky.requestFocus ();
        }
    }
}