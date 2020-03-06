package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;

import javax.swing.*;

import java.time.*;

import static ch.epfl.rigel.coordinates.HorizontalCoordinates.*;
import static org.junit.jupiter.api.Assertions.*;

class EquatorialToHorizontalConversionTestOurs {
    ZonedDateTime date1 = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2000,Month.AUGUST,26), LocalTime.of(0,5,18,(int) 2.35e7)), ZoneOffset.UTC);
    //EquatorialCoordinates equa1 = EquatorialCoordinates.of(Angle.ofDeg(1),Angle.ofDMS(23,13,10));
    EquatorialToHorizontalConversion equRoHor1 = new EquatorialToHorizontalConversion(date1 ,GeographicCoordinates.ofDeg(1,52));
    EquatorialCoordinates equa2 = EquatorialCoordinates.ofDeg(1,23.219444);


    @Test
    void apply() {

       Assertions.assertEquals(283.271027, equRoHor1.apply(equa2).azDeg(), 1e-6);
        Assertions.assertEquals(19.334345, equRoHor1.apply(equa2).altDeg(), 1e-6);

    }


    @Test
    void testEquals() {
    }
}