package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;

import java.io.InputStream;
import java.time.ZonedDateTime;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public class testCanvas extends Application {
    public static void main (String [] args)     {launch (args); }

    private InputStream resourceStream (String resourceName)  {
        return getClass().getResourceAsStream (resourceName);
    }

    @Override
    public void start (Stage primaryStage) throws Exception{

        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalog = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            ZonedDateTime when = ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
            GeographicCoordinates where = GeographicCoordinates.ofDeg(6.57, 46.52);
            HorizontalCoordinates projCenter = HorizontalCoordinates.ofDeg(265, 1);
            StereographicProjection projection = new StereographicProjection(projCenter);
            ObservedSky sky = new ObservedSky(when, where, projection, catalog);



            Canvas canvas = new Canvas(800, 600);
            SkyCanvasPainter painter = new SkyCanvasPainter (canvas);
            painter.clear();
            Transform planeToCanvas = Transform.affine(1000, 0, 0, -1000, 400, 300);
            GraphicsContext ctx = canvas.getGraphicsContext2D();
            painter.drawStars(sky, projection, planeToCanvas);
            painter.drawPlanets(sky, projection, planeToCanvas);
            painter.drawSun(sky, projection, planeToCanvas);
            painter.drawMoon(sky, projection, planeToCanvas);
            painter.drawHorizon( sky,projection, planeToCanvas);

            // White background
            //ctx.setFill(Color.BLACK);
            //ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


            primaryStage.setScene(new Scene(new BorderPane(canvas)));
            primaryStage.show();


             // Red EPFL text
             ctx.setFont (Font.font ( "Helvetica" , 300 ));
             ctx.setFill (Color.RED);
             ctx.setTextAlign (TextAlignment.CENTER);
             ctx.setTextBaseline (VPos.BASELINE);
             ctx.fillText ( "EPFL" , 100 , 500 );

            //Oval
            ctx.fillOval(-4, 0, 100, 50);

            /**
             // Holes in the E and the F
             ctx.setFill (Color.WHITE);
             ctx.fillRect ( 50 , 126 , 30 , 26 );
             ctx.fillRect ( 450 , 126 , 30 , 26 );
             **/
            primaryStage.setScene(new Scene(new BorderPane(canvas)));
            primaryStage.show();
        }
    }
}