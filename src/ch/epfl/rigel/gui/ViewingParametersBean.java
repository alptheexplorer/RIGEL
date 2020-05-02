package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ViewingParametersBean {

    private SimpleDoubleProperty angleOfView = new SimpleDoubleProperty();
    private SimpleObjectProperty<HorizontalCoordinates> projectionCenter = new SimpleObjectProperty<>();

    public ViewingParametersBean(){ };

    /**
     *
     * @return contents of angle property
     */
    public double getAngleOfView() {
        return angleOfView.get();
    }

    /**
     *
     * @return angleOfView property
     */
    public SimpleDoubleProperty angleOfViewProperty() {
        return angleOfView;
    }

    /**
     *
     * @param angleOfView
     */
    public void setAngleOfView(double angleOfView) {
        this.angleOfView.set(angleOfView);
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
    public void setProjectionCenter(HorizontalCoordinates projectionCenter) {
        this.projectionCenter.set(projectionCenter);
    }
}
