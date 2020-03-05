package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static org.junit.jupiter.api.Assertions.assertEquals;


class EpochTestOur {

    ZonedDateTime testTime = ZonedDateTime.of((LocalDate.of(2009, Month.JUNE, 19)),
            LocalTime.of(18, 0),
            ZoneOffset.UTC);






    @Test
    void daysUntilWorks() {
        assertEquals(-194.25,J2010.daysUntil(testTime));

    }

    @Test
    void julianCenturiesUntilWorks() {
    }
}