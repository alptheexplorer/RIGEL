package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HorizontalCoordinatesTestOurs {
    @Test
    void ofWorksOnInvalidAzimut(){
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(720,5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(7,5);
        });
    }

    @Test
    void validAzWorks(){
        HorizontalCoordinates test = HorizontalCoordinates.of(Math.PI, 1);
        assertEquals(Math.PI, test.az());
        assertEquals(180, test.azDeg());
    }

    @Test
    void azOctantWorks(){
        HorizontalCoordinates test = HorizontalCoordinates.ofDeg(23.6,66);
        assertEquals("NE", test.azOctantName("N", "E", "S", "W"));
    }

    @Test
    void angularDistWorks(){
        HorizontalCoordinates thiss = HorizontalCoordinates.ofDeg(45, 90);
        HorizontalCoordinates that = HorizontalCoordinates.ofDeg(60, 15);
        assertEquals(Angle.ofDeg(75), thiss.angularDistanceTo(that));
    }

    @Test
    void toStringWorks(){
        HorizontalCoordinates thiss = HorizontalCoordinates.ofDeg(45, 90);
        assertEquals("(az=45,0000°, alt=90,0000°)", thiss.toString());
    }

}