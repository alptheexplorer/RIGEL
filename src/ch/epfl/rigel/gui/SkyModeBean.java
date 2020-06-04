package ch.epfl.rigel.gui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SkyModeBean {

    public boolean isIsNonAsterism() {
        if(!modeProperty().get().equals("asterism-excluded")){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean isIsNonHorizon() {
        if(!modeProperty().get().equals("horizon-excluded")){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean isIsNonPlanet() {
        if(!modeProperty().get().equals("planet-excluded")){
            return false;
        }
        else{
            return true;
        }
    }


    private SimpleBooleanProperty isNonAsterism, isNonHorizon, isNonPlanet;

    public String getMode() {
        return mode.get();
    }

    public SimpleStringProperty modeProperty() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    private SimpleStringProperty mode = new SimpleStringProperty();


}
