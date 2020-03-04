package ch.epfl.rigel.coordinates;

import java.time.*;

class EclipticToEquatorialConversionTest {
    ZonedDateTime testTime = ZonedDateTime.of((LocalDate.of(2009, Month.JULY, 6),
            LocalTime.of(12, 0),
            ZoneOffset.UTC));

    EclipticCoordinates testObject = EclipticCoordinates.of(139.)

}