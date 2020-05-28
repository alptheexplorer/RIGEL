package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Epoch, J2000 and J2010, widely used in astronomy
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.NOON,
            ZoneOffset.UTC)),
    J2010(ZonedDateTime.of((LocalDate.of(2010, Month.JANUARY, 1).minusDays(1)),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC));


    private final ZonedDateTime timeObject;
    private final static double MIL_TO_DAY = 1000 * 60 * 60 * 24;
    private final static double MIL_TO_JULCENTURY = 36525;

    Epoch(ZonedDateTime d) {
        this.timeObject = d;
    }


    /**
     * @param when
     * @return days between the astronomical unit and argument ZonedDateTime object
     */
    public double daysUntil(ZonedDateTime when) {
        return timeObject.until(when, ChronoUnit.MILLIS) / MIL_TO_DAY;
    }

    /**
     * @param when
     * @return julianCenturies between the astronomical unit and argument ZonedDateTime object
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return (daysUntil(when) / MIL_TO_JULCENTURY);
    }
}
