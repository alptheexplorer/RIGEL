package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

public enum NamedTimeAccelerator {
    TIMES_1(TimeAccelerator.continuous(1)),
    TIMES_30(TimeAccelerator.continuous(30)),
    TIMES_300(TimeAccelerator.continuous(300)),
    TIMES_3000(TimeAccelerator.continuous(3000)),
    DAY(TimeAccelerator.discrete(Duration.ofHours(24), (long)(60*Math.pow(10,9)))),
    SIDEREAL_DAY(TimeAccelerator.discrete(Duration.ofSeconds(ZonedDateTime.)));

    TimeAccelerator accelerator;

    NamedTimeAccelerator(TimeAccelerator accelerator){
        this.accelerator = accelerator;
    }

}
