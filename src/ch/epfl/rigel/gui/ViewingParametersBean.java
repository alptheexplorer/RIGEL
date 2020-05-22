package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public class ViewingParametersBean {

    private SimpleDoubleProperty fieldOfView = new SimpleDoubleProperty();
    private SimpleObjectProperty<HorizontalCoordinates> projectionCenter = new SimpleObjectProperty<>();

    public ViewingParametersBean(){ };

    /**
     *
     * @return contents of angle property
     */
    public double getFieldOfView() {
        return fieldOfView.get();
    }

    /**
     *
     * @return angleOfView property
     */
    public SimpleDoubleProperty fieldOfViewProperty() {
        return fieldOfView;
    }

    /**
     *
     * @param angleOfView in degrees
     */
    public void setFieldOfViewDeg(double angleOfView) {
        this.fieldOfView.set(angleOfView);
    }

    /**
     *
     * @return contents of projectionCenter property
     */
    public HorizontalCoordinates getProjectionCenter() {
        return projectionCenter.get();
    }

    /**
     *
     * @return projectionCenter property
     */
    public SimpleObjectProperty<HorizontalCoordinates> projectionCenterProperty() {
        return projectionCenter;
    }

    /**
     *
     * @param projectionCenter
     */
    public void setCenter(HorizontalCoordinates projectionCenter) {
        this.projectionCenter.set(projectionCenter);
    }

}
