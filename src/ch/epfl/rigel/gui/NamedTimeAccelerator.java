package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

//part of the MODEL of the MVC pattern
public enum NamedTimeAccelerator {
    TIMES_1(TimeAccelerator.continuous(1), "1x"),
    TIMES_30(TimeAccelerator.continuous(30), "30x"),
    TIMES_300(TimeAccelerator.continuous(300), "300x"),
    TIMES_3000(TimeAccelerator.continuous(3000), "3000x"),
    DAY(TimeAccelerator.discrete(Duration.of(24, ChronoUnit.HOURS), 60L), "jour"),
    SIDEREAL_DAY(TimeAccelerator.discrete(Duration.of(86164, ChronoUnit.SECONDS),60L), "jour sidéral");

    TimeAccelerator accelerator;
    String name;

    NamedTimeAccelerator(TimeAccelerator accelerator, String name){
        this.accelerator = accelerator;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public TimeAccelerator getAccelerator(){
        return this.accelerator;
    }

    @Override
    public String toString(){
        return this.name;
    }



}
