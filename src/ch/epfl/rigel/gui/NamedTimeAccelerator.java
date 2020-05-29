package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * Contains Accelerator objects, enum strings are self-explanatory of the type of acceleration provided
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
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

    /**
     *
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return  time accelerator
     */
    public TimeAccelerator getAccelerator(){
        return this.accelerator;
    }

    /**
     *
     * @return name
     */
    @Override
    public String toString(){
        return this.name;
    }



}
