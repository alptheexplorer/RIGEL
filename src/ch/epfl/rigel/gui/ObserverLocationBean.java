package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public class ObserverLocationBean {

    private SimpleDoubleProperty lonDeg = new SimpleDoubleProperty();
    private SimpleDoubleProperty latDeg = new SimpleDoubleProperty();
    // this object depends on lonDeg and latDeg and is observable
    private ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(()-> GeographicCoordinates.ofDeg(lonDeg.get(),latDeg.get()),lonDeg,latDeg);
    public ObserverLocationBean(){ }

    /**
     *
     * @return content of lonDeg
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     *
     * @return londeg property
     */
    public SimpleDoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     *
     * @param lonDeg
     */
    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     *
     * @return content of latDeg
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     *
     * @return latDeg property
     */
    public SimpleDoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     *
     * @param latDeg
     */
    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     *
     * @return content of geogeraphicCoordinates property
     */
    public SimpleObjectProperty<GeographicCoordinates> getCoordinates() {
        return new SimpleObjectProperty<>(coordinates.get());
    }

    /**
     *
     * @return geographicCoordinates property
     */
    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    public void setCoordinates(GeographicCoordinates coord){
        this.setLatDeg(coord.latDeg());
        this.setLonDeg(coord.lonDeg());
    }
}
