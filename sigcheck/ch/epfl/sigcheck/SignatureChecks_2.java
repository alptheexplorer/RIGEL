package ch.epfl.sigcheck;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;

final class SignatureChecks_2 {
    void checkGeographicCoordinates() {
        boolean b;
        double d = 0;
        GeographicCoordinates g;
        b = GeographicCoordinates.isValidLonDeg(d);
        b = GeographicCoordinates.isValidLatDeg(d);
        g = GeographicCoordinates.ofDeg(d, d);
        d = g.lon();
        d = g.lonDeg();
        d = g.lat();
        d = g.latDeg();
    }

    void checkHorizontalCoordinates() {
        double d = 0;
        String s = "";
        HorizontalCoordinates h;
        h = HorizontalCoordinates.of(d, d);
        h = HorizontalCoordinates.ofDeg(d, d);
        d = h.az();
        d = h.azDeg();
        d = h.alt();
        d = h.altDeg();
        s = h.azOctantName(s, s, s, s);
        d = h.angularDistanceTo(h);
    }

    void checkEquatorialCoordinates() {
        double d = 0;
        EquatorialCoordinates e;
        e = EquatorialCoordinates.of(d, d);
        d = e.ra();
        d = e.raDeg();
        d = e.raHr();
        d = e.dec();
        d = e.decDeg();
    }

    void checkEclipticCoordinates() {
        double d = 0;
        EclipticCoordinates e;
        e = EclipticCoordinates.of(d, d);
        d = e.lon();
        d = e.lonDeg();
        d = e.lat();
        d = e.latDeg();
    }
}
