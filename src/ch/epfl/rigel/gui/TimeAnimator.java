package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

//part of the MODEL of the MVC pattern
public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean toModifyTime;
    private SimpleObjectProperty<TimeAccelerator> accelerator;
    private SimpleBooleanProperty running;
    private long timeElapsed;

    public TimeAnimator(DateTimeBean toModifyTime){
        this.toModifyTime = toModifyTime;
    }


    @Override
    public void handle(long l) {
        accelerator.get().adjust(toModifyTime.getZonedDateTime(),l);
    }

    @Override
    public void start(){
        super.start();
        this.running.set(true);
        this.handle(System.nanoTime());
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
