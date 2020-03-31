package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.time.*;

import static java.lang.Math.toDegrees;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SunModelTest {
    // Convert an angle given in HMS to hours.
    private static double hmsToHr(int h, int m, double s) {
        return h + (m / 60d) + (s / 3600d);
    }

    // Same, but when using degrees (name is clearer).
    private static double dmsToDeg(int d, int m, double s) {
        return d + (m / 60d) + (s / 3600d);
    }

    @Test
    void sunEquatorialPosIsCorrectForBookExample() {
        // PACS4, §46 (p. 105)
        var when = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC);
        var eclToEqu = new EclipticToEquatorialConversion(when);
        var daysSinceJ2010 = Epoch.J2010.daysUntil(when);

        var sunEclPos = SunModel.SUN.at(daysSinceJ2010, eclToEqu).eclipticPos();
        assertEquals(123.580_601, sunEclPos.lonDeg(), 0.000_001d / 2d);
        assertEquals(0, sunEclPos.latDeg());
    }

    @Test
    void sunAngularSizeIsCorrectForBookExample() {
        // PACS4, §48 (p. 110)
        // Beware, it seems this example uses the more precise computation of the Sun's orbit given in §47.
        // (still, the angular size is the same).
        var when = ZonedDateTime.of(
                LocalDate.of(1988, Month.JULY, 27),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC);
        var daysSinceJ2010 = Epoch.J2010.daysUntil(when);
        var eclToEqu = new EclipticToEquatorialConversion(when);

        var sunAngularSize = SunModel.SUN.at(daysSinceJ2010, eclToEqu).angularSize();
        assertEquals(
                dmsToDeg(0, 31, 30),
                toDegrees(sunAngularSize),
                dmsToDeg(0, 0, 0.5));
    }


    @Test
    void sunEclipticPosIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var ecl1 = SunModel.SUN.at(d1, eclToEqu1).eclipticPos();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var ecl2 = SunModel.SUN.at(d2, eclToEqu2).eclipticPos();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var ecl3 = SunModel.SUN.at(d3, eclToEqu3).eclipticPos();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var ecl4 = SunModel.SUN.at(d4, eclToEqu4).eclipticPos();

        assertEquals(1.3004346539729514, ecl1.lon(), 1e-8);
        assertEquals(3.4599344864383497, ecl2.lon(), 1e-8);
        assertEquals(4.894296774140312, ecl3.lon(), 1e-8);
        assertEquals(0.12962120393768117, ecl4.lon(), 1e-8);

        assertEquals(0, ecl1.lat());
        assertEquals(0, ecl2.lat());
        assertEquals(0, ecl3.lat());
        assertEquals(0, ecl4.lat());
    }

    @Test
    void sunAngularSizeIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var angSz1 = SunModel.SUN.at(d1, eclToEqu1).angularSize();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var angSz2 = SunModel.SUN.at(d2, eclToEqu2).angularSize();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var angSz3 = SunModel.SUN.at(d3, eclToEqu3).angularSize();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var angSz4 = SunModel.SUN.at(d4, eclToEqu4).angularSize();

        assertEquals(0.009170930832624435, angSz1, 1e-8);
        assertEquals(0.009321331046521664, angSz2, 1e-8);
        assertEquals(0.009462745860219002, angSz3, 1e-8);
        assertEquals(0.00932283978909254, angSz4, 1e-8);
    }
}
