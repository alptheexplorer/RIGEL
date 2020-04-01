package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.PlanetModel.SATURN;

class PlanetModelTest {

    @Test
    void constCheck(){
        EclipticCoordinates expectedCoord = EclipticCoordinates.of(3,0);
        ZonedDateTime timeObj = ZonedDateTime.of(
                LocalDate.of(2010, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);
        EclipticToEquatorialConversion convObj = new EclipticToEquatorialConversion(timeObj);
        EquatorialCoordinates expectedEqCoord = convObj.apply(expectedCoord);
        SATURN.at(J2010.daysUntil(timeObj), convObj);
    }
}