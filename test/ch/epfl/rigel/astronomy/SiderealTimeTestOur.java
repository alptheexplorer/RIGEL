package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SiderealTimeTestOur {
    ZonedDateTime date1 = ZonedDateTime.of(LocalDate.of(1980,Month.APRIL,22), LocalTime.of(14,36,51,670000000), ZoneOffset.UTC);

    @Test
    void greenwhichWorks(){
        assertEquals(Angle.normalizePositive(Angle.ofHr(4.668120)), SiderealTime.greenwich(date1));
    }



}