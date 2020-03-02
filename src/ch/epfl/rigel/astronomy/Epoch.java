package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author Alp Ozen
 */
public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 0),
            LocalTime.of(12, 0),
            ZoneOffset.UTC)),
    J2010(ZonedDateTime.of((LocalDate.of(2010, Month.JANUARY, 1).minusDays(1)),
            LocalTime.of(0, 0),
            ZoneOffset.UTC));


    private final ZonedDateTime timeObject;
    private final double MIL_TO_DAY = 1000*60*60*24;
    private final double MIL_TO_JULCENTURY = 36525;
    private Epoch(ZonedDateTime d){
        this.timeObject = d;
    }

    /**
     *
     * @return returns ZonedDateTime object inside enum
     */
    public ZonedDateTime getTimeObject(){
        return this.timeObject;
    }

    /**
     *
     * @param when
     * @return daysUntil between the astronomical unit and argument ZonedDateTime object
     */
    public double daysUntil(ZonedDateTime when){
        return (this.getTimeObject().until(when, ChronoUnit.MILLIS)/MIL_TO_DAY);
    }

    /**
     *
     * @param when
     * @return julianCenturies between the astronomical unit and argument ZonedDateTime object
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return (daysUntil(when)/MIL_TO_JULCENTURY);
    }
}
