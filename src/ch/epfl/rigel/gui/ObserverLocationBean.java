package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

/**
 * Mutable version of the time, this bean is modified by control panes and is observable in the skycanvasmanager
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 * ! certain getters were excluded because they were not used
 */
public class ObserverLocationBean {

    private SimpleDoubleProperty lonDeg = new SimpleDoubleProperty();
    private SimpleDoubleProperty latDeg = new SimpleDoubleProperty();
    // this object depends on lonDeg and latDeg and is observable
    private ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(()-> GeographicCoordinates.ofDeg(lonDeg.get(),latDeg.get()),lonDeg,latDeg);
    public ObserverLocationBean(){ }


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


    public void setCoordinates(GeographicCoordinates coord){
        this.setLatDeg(coord.latDeg());
        this.setLonDeg(coord.lonDeg());
    }
}
