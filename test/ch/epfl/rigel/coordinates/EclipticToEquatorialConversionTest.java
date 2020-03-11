package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EclipticToEquatorialConversionTest {

    @Test
    void conversionWorksOnCorrect(){
        ZonedDateTime testObject = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6),
                LocalTime.of(12, 0),
                ZoneOffset.UTC);

        EclipticCoordinates testObject2 = EclipticCoordinates.of(Angle.ofDeg(139.6861111), Angle.ofDeg(4.875278));
        EclipticToEquatorialConversion converter = new EclipticToEquatorialConversion(testObject);
        EquatorialCoordinates comparedObject = converter.apply(testObject2);
        EquatorialCoordinates compareObject = EquatorialCoordinates.of(Angle.ofDeg(143.722173), Angle.ofDeg(19.535003));
        System.out.println(J2000.julianCenturiesUntil(testObject));
        assertEquals(compareObject.lat(), comparedObject.lat(),1e-8);
        //0.095 099 247
        //0.0951129363449692
    }


}