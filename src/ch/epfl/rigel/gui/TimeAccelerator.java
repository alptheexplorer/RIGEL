package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 * functional interface providing the method that outputs a new time based on acceleration factor and acceleration type
 */
@FunctionalInterface
public interface TimeAccelerator {
     ZonedDateTime adjust(ZonedDateTime initialTime, long actualTimeSinceStart);

    /**
     *
     * @param acceleration
     * @return the continuousAccelerator implementation of the functional interface
     */
     static TimeAccelerator continuous(int acceleration){
         return (initialTime, actualTimeSinceStart)->
             initialTime.plus(acceleration*actualTimeSinceStart, ChronoUnit.NANOS);
     }

    /**
     *
     * @param step
     * @param frequency
     * @return the discreteAccelerator implementation of the functional interface
     */
     static TimeAccelerator discrete(Duration step, long frequency){
            return (initialTime, actualTimeSinceStart) ->
                initialTime.plus(step.multipliedBy(frequency*actualTimeSinceStart/ 1_000_000_000L));
     }
}
