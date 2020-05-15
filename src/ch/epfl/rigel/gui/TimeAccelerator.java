package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;
//part of the MODEL of the MVC pattern
@FunctionalInterface
public interface TimeAccelerator {
    /**
     *
     * @param initialTime
     * @param actualTimeSinceStart in nanoseconds
     * @return simulated time T in ZoneDateTime form
     */
     ZonedDateTime adjust(ZonedDateTime initialTime, long actualTimeSinceStart);

    /**
     *
     * @param accelerationFactor
     * @return the continuousAccelerator implementation of the functional interface
     */
     static TimeAccelerator continuous(int accelerationFactor){
         return (initialTime, actualTimeSinceStart)->
                 initialTime.plusNanos((long)accelerationFactor*actualTimeSinceStart);
     }

    /**
     *
     * @param step
     * @param frequency in nanoseconds
     * @return the discreteAccelerator implementation of the functional interface
     */
     static TimeAccelerator discrete(Duration step, long frequency){
            return (initialTime, actualTimeSinceStart) ->
                    initialTime.plusNanos((long)(step.toNanos()*Math.floor(frequency*actualTimeSinceStart)));
     }
}
