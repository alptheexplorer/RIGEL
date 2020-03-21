package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoonTest {

    @Test
    void toStringWorks(){
        Moon testVar = new Moon(EquatorialCoordinates.of(1,2), (float)1.0,(float)2.0,(float)0.3567);
        assertEquals("Lune 35.7%",testVar.toString());
    }
}