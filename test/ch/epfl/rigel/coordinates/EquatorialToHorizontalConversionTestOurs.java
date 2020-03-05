package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

class EquatorialToHorizontalConversionTestOurs {
    ZonedDateTime date5 = ZonedDateTime.of(LocalDate.of(2000,Month.JANUARY,3), LocalTime.of(5,51,44), ZoneOffset.UTC);
    EquatorialCoordinates equa1 = EquatorialCoordinates.of(5.862222,Angle.ofDeg(Angle.ofDMS(23,13,10)));
    EquatorialToHorizontalConversion equRoHor1 = new EquatorialToHorizontalConversion(date5 ,GeographicCoordinates.ofDeg(0,52));

    @Test
    void apply() {
        //Assertions.assertEquals(HorizontalCoordinates.ofDeg(283.271027,19.334345).az(), equRoHor1.apply(equa1).az());
        Assertions.assertEquals(HorizontalCoordinates.ofDeg(283.271027,19.334345).lon(), equRoHor1.apply(equa1).lon());
    }


    @Test
    void testEquals() {
    }
}