package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SunTest {

    EclipticCoordinates e = null;
    EquatorialCoordinates ef = null;
    @Test
    void constructorThrowsError(){
        assertThrows(NullPointerException.class, () -> {
            Sun sun = new Sun(e,ef,(float)1,(float)2);
        });
    }
}