package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

public final class TimeAnimator extends AnimationTimer {

    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty running =  new SimpleBooleanProperty();
    private final DateTimeBean dateTimeBean;
    private ZonedDateTime currentTime;
    private long initialTime;

    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
        running.set(false);
    }

    @Override
    public void handle(long now) {
        if(currentTime != null) {
            if(running.get()) {
                long deltaTime = now - initialTime;
                dateTimeBean.setZonedDateTime(accelerator.get().adjust(currentTime, deltaTime));
            }
        }
        else {
            currentTime = dateTimeBean.getZonedDateTime().get();
            initialTime = now;
        }
    }

    @Override
    public void start() {
        initialTime = 0;
        currentTime = null;
        running.set(true);
        super.start();
    }
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    public ReadOnlyBooleanProperty isRunning() {
        return running;
    }
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    public TimeAccelerator getAccelerator() {
        return accelerator.getValue();
    }

    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

}
