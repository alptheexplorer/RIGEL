package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AsterismTest {
    private static List<Star> newModifiableStarList(String... starNames) {
        var eqPos = EquatorialCoordinates.of(0, 0);
        var stars = new ArrayList<Star>();
        for (var starName : starNames) {
            stars.add(new Star(stars.size() + 1, starName, eqPos, 0, 0));
        }
        return stars;
    }

    @Test
    void constructorFailsWhenStarListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Asterism(List.of());
        });
    }

    @Test
    void constructorCopiesStarList() {
        var l = newModifiableStarList("Rigel");
        var s = l.get(0);
        var a = new Asterism(l);
        l.clear();
        assertEquals(1, a.stars().size());
        assertEquals(s, a.stars().get(0));
    }

    @Test
    void accessorDoesNotAllowEncapsulationViolation() {
        var a = new Asterism(newModifiableStarList("Rigel", "Aldebaran", "Sirius"));
        try {
            a.stars().clear();
        } catch (UnsupportedOperationException e) {
            // If UOE is thrown, the list is unmodifiable, which is correct.
        }
        assertEquals(3, a.stars().size());
    }
}
