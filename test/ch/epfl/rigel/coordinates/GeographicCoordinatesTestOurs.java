package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeographicCoordinatesTestOurs {

    @Test
    void validLonDegWorks(){
        assertEquals(false, GeographicCoordinates.isValidLonDeg(181));
    }

    @Test
    void toStringWorks(){
        GeographicCoordinates test = GeographicCoordinates.ofDeg(45,50);
        assertEquals("(lon=45.0000°, lat=50.0000°)", test.toString());
    }

}