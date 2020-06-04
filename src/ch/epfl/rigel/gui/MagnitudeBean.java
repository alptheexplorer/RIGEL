package ch.epfl.rigel.gui;

import javafx.beans.property.SimpleDoubleProperty;

public class MagnitudeBean {
    public double getMagnitude() {
        return magnitude.get();
    }

    public SimpleDoubleProperty magnitudeProperty() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude.set(magnitude);
    }

    // we start of with seeing the smallest possible star
    private SimpleDoubleProperty magnitude = new SimpleDoubleProperty(60.0);



}
