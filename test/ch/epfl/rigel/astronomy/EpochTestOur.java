package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;
import ch.epfl.test.TestRandomizer;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class EpochTestOur {
    Epoch j2000 = Epoch.J2000;
    Epoch j2010 = Epoch.J2010;
    ZonedDateTime date1 = ZonedDateTime.of(2000,1,1,13,0,0,0, ZoneId.of("GMT+00:00"));
    ZonedDateTime date2 = ZonedDateTime.of(2000,1,1,13,0,0,0, ZoneId.of("UTC+00:00"));
    ZonedDateTime date3 = ZonedDateTime.of(2000,1,1,13,1,0,0, ZoneId.of("UTC+00:00"));
    ZonedDateTime date4 = ZonedDateTime.of(2000,1,3,18,1,0,0, ZoneId.of("UTC+00:00"));
    ZonedDateTime date5 = ZonedDateTime.of(LocalDate.of(2000,Month.JANUARY,3), LocalTime.of(18,0), ZoneOffset.UTC);
    ZonedDateTime date6 = ZonedDateTime.of(2000,1,3,18,1,1,0, ZoneId.of("UTC+00:00"));
    ZonedDateTime date7 = ZonedDateTime.of(2000,1,3,18,1,0,1, ZoneId.of("UTC+00:00"));

    //ZonedDateTime date8 = ZonedDateTime.of(LocalDate.of(2010,Month.JANUARY,0), LocalTime.of(0,0), ZoneOffset.UTC);
    //ZonedDateTime date9 = ZonedDateTime.of(LocalDate.of(2010,Month.JANUARY,-1), LocalTime.of(0,0), ZoneOffset.UTC);
    ZonedDateTime date10 = ZonedDateTime.of(LocalDate.of(2009,Month.DECEMBER,31), LocalTime.of(0,0), ZoneOffset.UTC);
    //ZonedDateTime date11 = ZonedDateTime.of(LocalDate.of(2011,Month.JANUARY,0), LocalTime.of(0,0), ZoneOffset.UTC);







    @Test
    void daysUntil() {
        assertEquals(2.25, j2000.daysUntil(date5));
        assertEquals(1, j2000.daysUntil(null));
        assertEquals(2.25, j2000.daysUntil(date2));
        assertEquals(2.25, j2000.daysUntil(date3));
        assertEquals(2.25, j2000.daysUntil(date4));
        assertEquals(2.25, j2000.daysUntil(date6));
        assertEquals(2.25, j2000.daysUntil(date7));

    }

    @Test
    void julianCenturiesUntil() {
    }
}