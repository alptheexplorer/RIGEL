package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;
//part of the MODEL of the MVC pattern
@FunctionalInterface
public interface TimeAccelerator {
     ZonedDateTime adjust(ZonedDateTime initialTime, long actualTimeSinceStart);

    /**
     *
     * @param acceleration
     * @return the continuousAccelerator implementation of the functional interface
     */
     static TimeAccelerator continuous(double acceleration){
         return (initialTime, actualTimeSinceStart)->{
             return initialTime.plusNanos((long)acceleration*actualTimeSinceStart);
         };
     }

    /**
     *
     * @param step
     * @param frequency
     * @return the discreteAccelerator implementation of the functional interface
     */
     static TimeAccelerator discrete(Duration step, long frequency){
            return (initialTime, actualTimeSinceStart) ->{
                return initialTime.plusNanos((long)(step.toNanos()*Math.floor(frequency*actualTimeSinceStart))) ;
            };
     }
}
