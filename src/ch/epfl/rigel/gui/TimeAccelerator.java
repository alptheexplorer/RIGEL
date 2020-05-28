package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

//part of the MODEL of the MVC pattern
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
