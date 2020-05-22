package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import static javafx.beans.binding.Bindings.when;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private InputStream resourceStream (String resourceName)  {
        return getClass (). getResourceAsStream (resourceName);
    }

    @Override
    public void start (Stage primaryStage) throws IOException {
        try (InputStream hs = resourceStream ( "/hygdata_v3.csv" );
             InputStream as = resourceStream ( "/asterisms.txt" ) ) {
            StarCatalogue catalog = new StarCatalogue.Builder ()
                    .loadFrom (hs, HygDatabaseLoader.INSTANCE).loadFrom(as, AsterismLoader.INSTANCE).build();

            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            //creating neccessary beans
            DateTimeBean dateTimeBean = new DateTimeBean ();
            ObserverLocationBean observeLocationBean = new ObserverLocationBean();

            // initializing
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());
            dateTimeBean.setDate(LocalDate.now());
            observeLocationBean.setLonDeg(6.57);
            observeLocationBean.setLatDeg(46.52);
            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);


            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean ();
            viewingParametersBean.setCenter (
                    HorizontalCoordinates.ofDeg ( 180.000000000001 , 15 ));
            viewingParametersBean.setFieldOfViewDeg ( 100 );

            SkyCanvasManager canvasManager = new SkyCanvasManager (
                    catalog,
                    dateTimeBean,
                    observeLocationBean,
                    viewingParametersBean);

            Canvas sky = canvasManager.canvas();
            sky.setOnMouseClicked(e -> requestFocus(e,sky));
            Pane skyPane = new Pane();
            BorderPane root = new BorderPane ();
            sky.widthProperty().bind(root.widthProperty());
            sky.heightProperty().bind(root.heightProperty());

            skyPane.getChildren().add(sky);
            root.setTop(setController(observeLocationBean, dateTimeBean, timeAnimator));
            root.setCenter(skyPane);
            root.setBottom(setInformationPane());

            primaryStage.setMinWidth ( 800 );
            primaryStage.setMinHeight ( 600 );
            primaryStage.setScene (new Scene(root));
            primaryStage.show ();

            sky.requestFocus();
        }
    }

    private void requestFocus(Event e, Canvas sky){
        sky.requestFocus();
    }


    // root method which constructs upper hboxes
    private HBox setController(ObserverLocationBean observerBean, DateTimeBean dateTimeBean, TimeAnimator timeAnimator) throws IOException {
        HBox controlBar = new HBox();
        controlBar.setStyle ( "-fx-spacing: 4; -fx-padding: 4;" );
        setLeftBar(controlBar,observerBean);
        setMiddleBar(controlBar,dateTimeBean,timeAnimator);
        setRightBar(controlBar,timeAnimator,dateTimeBean);
        return controlBar;
    }

    //sets the observationPosition boxes
    private void setLeftBar(HBox observationControl, ObserverLocationBean observerBean ){
        HBox observationPosition = new HBox();
        // css attributes
        observationPosition.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label longLabel = new Label("Longitude (°):");
        // textfield for longitude
        TextField longEntry = new TextField();
        longEntry.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");


        Label latLabel = new Label("Latitude (°):");
        // textfield for latitude
        TextField latEntry = new TextField();
        latEntry.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");


        // setting textFormatter for both textfields
        NumberStringConverter stringConverter = new NumberStringConverter ( "#0.00" );
        TextFormatter<Number> lonTextFormatter = new TextFormatter<>(stringConverter,0,getFilter(false,stringConverter));
        lonTextFormatter.valueProperty().bindBidirectional(observerBean.lonDegProperty());
        observerBean.lonDegProperty().bind(lonTextFormatter.valueProperty());
        longEntry.setTextFormatter(lonTextFormatter);
        TextFormatter<Number> latTextFormatter = new TextFormatter<>(stringConverter,0,getFilter(true,stringConverter));
        latTextFormatter.valueProperty().bindBidirectional(observerBean.latDegProperty());
        latEntry.setTextFormatter(latTextFormatter);

        observationPosition.getChildren().addAll(longLabel, longEntry,latLabel,latEntry);
        Separator separator = new Separator(Orientation.VERTICAL);
        observationControl.getChildren().addAll(observationPosition,separator);
    }


    // returns a latFilter if first argument true else a longFilter
    private UnaryOperator<TextFormatter.Change> getFilter(Boolean isLat, NumberStringConverter stringConverter){

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText ();
                if(isLat){
                    double arg =
                            stringConverter.fromString(newText).doubleValue();
                    return GeographicCoordinates.isValidLatDeg (arg)
                            ? change
                            : null ;
                }
                else{
                    double arg =
                            stringConverter.fromString(newText).doubleValue();
                    return GeographicCoordinates.isValidLonDeg (arg)
                            ? change
                            : null ;
                }

            } catch (Exception e) {
                return  null ;
            }
        });

        return filter;
    }

    private void setMiddleBar(HBox observationControl, DateTimeBean dateTime, TimeAnimator timeAnimator){
        HBox controlBar = new HBox();
        controlBar.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //date panel
        Label dateLabel = new Label("Date");
        DatePicker datePick = new DatePicker();
        datePick.setStyle("-fx-pref-width: 120;");
        datePick.valueProperty().bindBidirectional(dateTime.dateProperty());

        //hour panel
        Label hourLabel = new Label("Heure:");
        TextField hourEntry = new TextField();
        hourEntry.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern ("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter (hmsFormatter, hmsFormatter);
        TextFormatter <LocalTime> timeFormatter = new TextFormatter <> (stringConverter);
        hourEntry.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTime.timeProperty());

        //timeZone panel
        ObservableList<ZoneId> zoneIDSorted = FXCollections.observableArrayList();
        for(String zoneIdName : ZoneId.getAvailableZoneIds())
            zoneIDSorted.add(ZoneId.of(zoneIdName));
        ComboBox<ZoneId> timeZone = new ComboBox<>(zoneIDSorted.sorted());
        timeZone.setStyle("-fx-pref-width: 180;");
        timeZone.valueProperty().bindBidirectional(dateTime.zoneProperty());


        controlBar.getChildren().addAll(dateLabel,datePick,hourLabel,hourEntry,timeZone);
        controlBar.disableProperty().bind(timeAnimator.isRunning());
        Separator separator = new Separator(Orientation.VERTICAL);
        observationControl.getChildren().addAll(controlBar,separator);
    }

    private void setRightBar(HBox observationControl, TimeAnimator timeAnimator, DateTimeBean dateTime) throws IOException{
        HBox timePassage = new HBox();
        timePassage.setStyle("-fx-spacing: inherit;");
        ChoiceBox<NamedTimeAccelerator> acceleratorChoice = new ChoiceBox<>();
        ObservableList<NamedTimeAccelerator> acceleratorValues = FXCollections.observableArrayList();
        for (NamedTimeAccelerator accelerator: NamedTimeAccelerator.values()){
            acceleratorValues.add(accelerator);
        }
        acceleratorChoice.setItems(acceleratorValues);
        observationControl.getChildren().add(timePassage);
        acceleratorChoice.disableProperty().bind(timeAnimator.isRunning());
        timeAnimator.acceleratorProperty().bind(Bindings.select(acceleratorChoice.valueProperty(), "accelerator"));

        // creating Buttons
        InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
            Font fontAwesome = Font.loadFont(fontStream, 15);
            fontStream.close();
            Button resetButton = new Button("\uf0e2");
            resetButton.setFont(fontAwesome);
            resetButton.disableProperty().bind(timeAnimator.isRunning());
            resetButton.setOnMouseClicked(e -> reset(e,dateTime,timeAnimator,resetButton));


            String play = "\uf04b";
            String pause = "\uf04c";
            Button animationButton = new Button();
            animationButton.setFont(fontAwesome);
            animationButton.textProperty().bind(when(timeAnimator.isRunning()).then(pause).otherwise(play));
            animationButton.setOnMouseClicked(e->animate(e,timeAnimator));

        timePassage.getChildren().addAll(acceleratorChoice,resetButton,animationButton);
    }

    // this reacts to clicks on the animation button
    private void animate(Event e, TimeAnimator timeAnimator){
        e.consume();
        if(timeAnimator.isRunning().get()){
            timeAnimator.stop();
        }else{
            timeAnimator.start();
        }
    }

    private void reset(Event e, DateTimeBean dateTime, TimeAnimator timeAnimator, Button resetButton){
        dateTime.setZonedDateTime(ZonedDateTime.now());
        resetButton.disableProperty().bind(timeAnimator.isRunning());
    }


    private BorderPane setInformationPane(){
        BorderPane infoPane = new BorderPane();
        infoPane.setStyle("-fx-padding: 4; -fx-background-color: white;");
        TextField fov = new TextField();
        Label fovLabel = new Label("Champ de vue :");





        return infoPane;
    }





}