package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

//part of the MODEL of the MVC pattern
public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean toModifyTime;
    private SimpleObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private SimpleBooleanProperty running = new SimpleBooleanProperty();
    private long initialTime = System.nanoTime();
    private long timeBlock = System.nanoTime() - initialTime;
    // currentDuration was obtained by subtracting default l values passed by AnimationTimer
    private long currentDuration = (1161813778127600l - 1161813763450200l);
    private long duration;
    private long previousL;
    private long currentL;

    public TimeAnimator(DateTimeBean toModifyTime){
        this.toModifyTime = toModifyTime;
    }


    @Override
    public void handle(long l) {
        l = this.currentDuration;
        toModifyTime.setZonedDateTime(getAccelerator().adjust(toModifyTime.getZonedDateTime().get(),l));
    }

    @Override
    public void start(){
        super.start();
        this.running.set(true);
    }

    @Override
    public void stop(){
        super.stop();
        this.running.set(false);
    }

    public ReadOnlyBooleanProperty isRunning(){
        return running.readOnlyBooleanProperty(running);
    }

    public SimpleObjectProperty<TimeAccelerator> acceleratorProperty(){
        return this.accelerator;
    }

    public TimeAccelerator getAccelerator(){
        return this.accelerator.get();
    }

    public void setAccelerator(TimeAccelerator accelerator){
        this.accelerator.set(accelerator);
    }
}
