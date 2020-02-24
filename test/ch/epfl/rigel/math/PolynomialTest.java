// Rigel stage 1

package ch.epfl.rigel.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PolynomialTest {
    @Test
    void ofFailsWithFirstCoefficient0() {
        assertThrows(IllegalArgumentException.class, () -> {
            Polynomial.of(0, 2, 45, -1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Polynomial.of(0, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Polynomial.of(0);
        });
    }

    @Test
    void ofFailsWithNullArray() {
        assertThrows(NullPointerException.class, () -> {
            Polynomial.of(1, null);
        });
    }

    @Test
    void atWorksOnPolynomialOfDegree0() {
        var polynomial = Polynomial.of(5);
        assertEquals(5, polynomial.at(12));
        assertEquals(5, polynomial.at(0));
    }

    @Test
    void atWorksOnPolynomialOfDegree1() {
        var polynomial = Polynomial.of(2, 5);
        assertEquals(5, polynomial.at(0));
        assertEquals(9, polynomial.at(2));
        assertEquals(-2, polynomial.at(-3.5));
    }

    @Test
    void atWorksOnPolynomialOfDegree2() {
        var poly = Polynomial.of(1, -7, 10); // (x - 5) * (x - 2)
        assertEquals(0, poly.at(5));
        assertEquals(0, poly.at(2));
        assertEquals(-2, poly.at(3));
        assertEquals(40, poly.at(-3));
        assertEquals(10, poly.at(0));
        assertEquals(1.75, poly.at(1.5));
    }

    @Test
    void atWorksOnPolynomialOfDegree3() {
        var poly = Polynomial.of(1, -6, 3, 10); // (x - 5) * (x - 2) * (x + 1)
        assertEquals(poly.at(5), 0);
        assertEquals(poly.at(2), 0);
        assertEquals(poly.at(-1), 0);
        assertEquals(poly.at(4), -10);
        assertEquals(poly.at(3), -8);
        assertEquals(poly.at(0), 10);
        assertEquals(poly.at(7), 80);
    }

    @Test
    void toStringWorksOnKnownPolynomials() {
        var polynomial1 = Polynomial.of(5.999999);
        assertEquals("5.999999", polynomial1.toString());
        var polynomial2 = Polynomial.of(-3.14);
        assertEquals("-3.14", polynomial2.toString());
        var polynomial3 = Polynomial.of(2, 5);
        assertEquals("2.0x+5.0", polynomial3.toString());
        var polynomial4 = Polynomial.of(3, -2.5);
        assertEquals("3.0x-2.5", polynomial4.toString());
        var polynomial5 = Polynomial.of(-1, -7, 10);
        assertEquals("-x^2-7.0x+10.0", polynomial5.toString());
        var polynomial6 = Polynomial.of(-2, 4, 6);
        assertEquals("-2.0x^2+4.0x+6.0", polynomial6.toString());
        var polynomial7 = Polynomial.of(1, -6, 0, -10);
        assertEquals("x^3-6.0x^2-10.0", polynomial7.toString());
        var polynomial8 = Polynomial.of(-2, 6, 2, -6);
        assertEquals("-2.0x^3+6.0x^2+2.0x-6.0", polynomial8.toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var polynomial = Polynomial.of(1);
            polynomial.equals(polynomial);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Polynomial.of(1).hashCode();
        });
    }
}
