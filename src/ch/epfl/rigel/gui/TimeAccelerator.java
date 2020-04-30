package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;

@FunctionalInterface
public interface TimeAccelerator {
     ZonedDateTime adjust(ZonedDateTime initialTime, long actualTimeSinceStart);

    /**
     *
     * @param acceleration
     * @return the continuousAccelerator implementation of the functional interface
     */
     static TimeAccelerator continuous(double acceleration){
         return (ZonedDateTime initialTime, long actualTimeSinceStart)->{
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
            return (ZonedDateTime initialTime, long actualTimeSinceStart) ->{
                return initialTime.plusSeconds((long)(step.toSeconds()*Math.floor(frequency*actualTimeSinceStart))) ;
            };
     }
}
