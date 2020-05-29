package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * Responsible of running the animation, most important method is handle which passes in an exponential evolving deltatime to adjust
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */

public final class TimeAnimator extends AnimationTimer {

    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty running =  new SimpleBooleanProperty();
    private final DateTimeBean dateTimeBean;
    private ZonedDateTime currentTime;
    private long initialTime;

    /**
     *
     * @param dateTimeBean
     */
    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
        running.set(false);
    }

    /**
     *
     * @param now
     */
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

    /**
     * start
     */
    @Override
    public void start() {
        initialTime = 0;
        currentTime = null;
        running.set(true);
        super.start();
    }

    /**
     * stop
     */
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    /**
     *
     * @return true if it's runnig
     */
    public ReadOnlyBooleanProperty isRunning() {
        return running;
    }

    /**
     * set accelerator
     * @param accelerator
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }


    /**
     *
     * @return time accelerator
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

}
