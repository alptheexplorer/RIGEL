package ch.epfl.rigel.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class BackgroundRgbBean {
    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public SimpleObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    private SimpleObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>();
}
