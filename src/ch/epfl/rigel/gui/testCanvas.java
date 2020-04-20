package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class testCanvas extends Application {
    public static void main (String [] args)     {launch (args); }

    @Override
    public void start (Stage primaryStage)    {
        Canvas canvas = new Canvas ( 800 , 300 );
        GraphicsContext ctx = canvas.getGraphicsContext2D ();

        // White background
        ctx.setFill (Color.WHITE);
        ctx.fillRect ( 0 , 0 , canvas.getWidth (), canvas.getHeight ());

        // Red EPFL text
        ctx.setFont (Font.font ( "Helvetica" , 300 ));
        ctx.setFill (Color.RED);
        ctx.setTextAlign (TextAlignment.CENTER);
        ctx.setTextBaseline (VPos.BASELINE);
        ctx.fillText ( "EPFL" , 400 , 250 );

        // Holes in the E and the F
        ctx.setFill (Color.WHITE);
        ctx.fillRect ( 50 , 126 , 30 , 26 );
        ctx.fillRect ( 450 , 126 , 30 , 26 );

        primaryStage.setScene ( new Scene( new BorderPane(canvas)));
        primaryStage.show ();
    }
}