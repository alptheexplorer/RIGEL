package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static ch.epfl.rigel.astronomy.PlanetModel.*;
import static java.lang.Math.toRadians;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanetModelTest {
    private static final ZonedDateTime ZDT_2003_11_22_MIDNIGHT_UTC = ZonedDateTime.of(
            LocalDate.of(2003, Month.NOVEMBER, 22),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC);

    private static Planet jupiterOn2003_11_22() {
        var when = ZDT_2003_11_22_MIDNIGHT_UTC;
        return JUPITER.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
    }

    private static Planet mercuryOn2003_11_22() {
        var when = ZDT_2003_11_22_MIDNIGHT_UTC;
        return MERCURY.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));
    }

    private static EquatorialCoordinates equatorialPosOn2003_11_22(double eclipticLonDeg, double eclipticLatDeg) {
        var eclPos = EclipticCoordinates.of(toRadians(eclipticLonDeg), toRadians(eclipticLatDeg));
        var eclToEqu = new EclipticToEquatorialConversion(ZDT_2003_11_22_MIDNIGHT_UTC);
        return eclToEqu.apply(eclPos);
    }

    // Convert an angle given in HMS to hours.
    private static double hmsToHr(int h, int m, double s) {
        return h + (m / 60d) + (s / 3600d);
    }

    // Same, but when using degrees (name is clearer).
    private static double dmsToDeg(int d, int m, double s) {
        return d + (m / 60d) + (s / 3600d);
    }

    @Test
    void jupiterEquatorialPosIsCorrectOnBookExample() {
        // PACS4, §54 (p. 127)
        var expectedEquPos = equatorialPosOn2003_11_22(166.310_510, 1.036_466);
        var jupiterEquPos = jupiterOn2003_11_22().equatorialPos();

        assertEquals(expectedEquPos.raHr(), jupiterEquPos.raHr(), hmsToHr(0, 0, 0.5));
        assertEquals(expectedEquPos.decDeg(), jupiterEquPos.decDeg(), dmsToDeg(0, 0, 0.5));
    }

    @Test
    void jupiterAngularSizeIsCorrectOnBookExample() {
        var jupiter = jupiterOn2003_11_22();
        assertEquals(Angle.ofArcsec(35.1), jupiter.angularSize(), Angle.ofArcsec(0.05));
    }

    @Test
    void jupiterMagnitudeIsCorrectOnBookExample() {
        var jupiter = jupiterOn2003_11_22();
        assertEquals(-2, jupiter.magnitude(), 0.5);
    }

    @Test
    void mercuryPositionIsCorrectOnBookExample() {
        var expectedEquPos = equatorialPosOn2003_11_22(253.929_758, -2.044_057);
        var mercuryEquPos = mercuryOn2003_11_22().equatorialPos();

        assertEquals(expectedEquPos.raHr(), mercuryEquPos.raHr(), hmsToHr(0, 0, 0.5));
        assertEquals(expectedEquPos.decDeg(), mercuryEquPos.decDeg(), dmsToDeg(0, 0, 0.5));
    }


    @Test
    void planetEclipticPosIsCorrectForKnownValues() {
        var planetModelIt = List.of(MERCURY, VENUS, MARS, JUPITER).iterator();

        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var equ1 = planetModelIt.next().at(d1, eclToEqu1).equatorialPos();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var equ2 = planetModelIt.next().at(d2, eclToEqu2).equatorialPos();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var equ3 = planetModelIt.next().at(d3, eclToEqu3).equatorialPos();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var equ4 = planetModelIt.next().at(d4, eclToEqu4).equatorialPos();

        assertEquals(1.7087783986800247, equ1.ra(), 1e-8);
        assertEquals(3.082020783633003, equ2.ra(), 1e-8);
        assertEquals(5.072603104992766, equ3.ra(), 1e-8);
        assertEquals(5.16048834903315, equ4.ra(), 1e-8);

        assertEquals(0.42497725587415336, equ1.dec(), 1e-8);
        assertEquals(0.05364294413026959, equ2.dec(), 1e-8);
        assertEquals(-0.402992531277535, equ3.dec(), 1e-8);
        assertEquals(-0.3734650388335377, equ4.dec(), 1e-8);
    }

    @Test
    void planetAngularSizeIsCorrectForKnownValues() {
        var planetModelIt = List.of(MERCURY, VENUS, SATURN, NEPTUNE).iterator();

        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var angSz1 = planetModelIt.next().at(d1, eclToEqu1).angularSize();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var angSz2 = planetModelIt.next().at(d2, eclToEqu2).angularSize();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var angSz3 = planetModelIt.next().at(d3, eclToEqu3).angularSize();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var angSz4 = planetModelIt.next().at(d4, eclToEqu4).angularSize();

        assertEquals(4.352875839686021E-5, angSz1, 1e-8);
        assertEquals(5.366084951674566E-5, angSz2, 1e-8);
        assertEquals(8.326074021169916E-5, angSz3, 1e-8);
        assertEquals(9.764655260369182E-6, angSz4, 1e-8);
    }

    @Test
    void planetMagnitudeIsCorrectForKnownValues() {
        var planetModelIt = List.of(MERCURY, VENUS, MARS, JUPITER).iterator();

        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var mag1 = planetModelIt.next().at(d1, eclToEqu1).magnitude();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var mag2 = planetModelIt.next().at(d2, eclToEqu2).magnitude();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var mag3 = planetModelIt.next().at(d3, eclToEqu3).magnitude();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var mag4 = planetModelIt.next().at(d4, eclToEqu4).magnitude();

        assertEquals(-1.497975468635559, mag1, 1e-8);
        assertEquals(-4.108942985534668, mag2, 1e-8);
        assertEquals(1.110831618309021, mag3, 1e-8);
        assertEquals(-2.154392957687378, mag4, 1e-8);
    }
}
