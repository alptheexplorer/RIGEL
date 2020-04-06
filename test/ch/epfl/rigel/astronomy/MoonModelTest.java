package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.MoonModel.MOON;
import static java.lang.Math.toRadians;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoonModelTest {
    // Convert an angle given in HMS to hours.
    private static double hmsToHr(int h, int m, double s) {
        return h + (m / 60d) + (s / 3600d);
    }

    // Same, but when using degrees (name is clearer).
    private static double dmsToDeg(int d, int m, double s) {
        return d + (m / 60d) + (s / 3600d);
    }

    private static final ZonedDateTime SEP_1_2003_MIDNIGHT = ZonedDateTime.of(
            LocalDate.of(2003, Month.SEPTEMBER, 1),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC);

    @Test
    void moonEquatorialPosIsCorrectForBookExample() {
        // PACS4, §65 (p. 166)
        var when = SEP_1_2003_MIDNIGHT;

        var eclToEqu = new EclipticToEquatorialConversion(when);
        var expectedEclPos = EclipticCoordinates.of(toRadians(214.862515), toRadians(1.716257));
        var expectedEquPos = eclToEqu.apply(expectedEclPos);

        var daysSinceJ2010 = Epoch.J2010.daysUntil(when);
        var moonEquPos = MOON.at(daysSinceJ2010, eclToEqu).equatorialPos();

        assertEquals(expectedEquPos.raHr(), moonEquPos.raHr(), hmsToHr(0, 0, 0.5));
        assertEquals(expectedEquPos.decDeg(), moonEquPos.decDeg(), dmsToDeg(0, 0, 0.5));
    }

    // No test to check the angular size given in PACS4 at page 176, as it seems wrong.


    @Test
    void moonEclipticPosIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var equ1 = MOON.at(d1, eclToEqu1).equatorialPos();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var equ2 = MOON.at(d2, eclToEqu2).equatorialPos();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var equ3 = MOON.at(d3, eclToEqu3).equatorialPos();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var equ4 = MOON.at(d4, eclToEqu4).equatorialPos();

        assertEquals(5.419711367666448, equ1.ra(), 1e-8);
        assertEquals(1.929262186255375, equ2.ra(), 1e-8);
        assertEquals(4.1776103058779235, equ3.ra(), 1e-8);
        assertEquals(0.7290495225161937, equ4.ra(), 1e-8);

        assertEquals(-0.37042768810935156, equ1.dec(), 1e-8);
        assertEquals(0.3970859160598725, equ2.dec(), 1e-8);
        assertEquals(-0.40335054470294085, equ3.dec(), 1e-8);
        assertEquals(0.20848557856348404, equ4.dec(), 1e-8);
    }

    @Test
    void moonAngularSizeIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var angSz1 = MOON.at(d1, eclToEqu1).angularSize();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var angSz2 = MOON.at(d2, eclToEqu2).angularSize();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var angSz3 = MOON.at(d3, eclToEqu3).angularSize();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var angSz4 = MOON.at(d4, eclToEqu4).angularSize();

        assertEquals(0.009165297262370586, angSz1, 1e-8);
        assertEquals(0.009557198733091354, angSz2, 1e-8);
        assertEquals(0.009228141978383064, angSz3, 1e-8);
        assertEquals(0.008661667816340923, angSz4, 1e-8);
    }

    @Test
    void moonPhaseIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var info1 = MOON.at(d1, eclToEqu1).info();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var info2 = MOON.at(d2, eclToEqu2).info();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var info3 = MOON.at(d3, eclToEqu3).info();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var info4 = MOON.at(d4, eclToEqu4).info();

        assertTrue(info1.contains("80"));
        assertTrue(info2.contains("49"));
        assertTrue(info3.contains("10"));
        assertTrue(info4.contains("9"));
    }
}
