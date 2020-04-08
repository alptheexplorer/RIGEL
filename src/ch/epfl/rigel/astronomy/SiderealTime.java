package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * @author Alp-Ozen
 */
public final class SiderealTime {
    private SiderealTime() {

    }

    /**
     * @param when
     * @return greenwich converted time in radians in normalized range
     */
    public static double greenwich(ZonedDateTime when) {
        double bigT;
        double smallT;
        double S_0;
        double S_1;
        double result;
        RightOpenInterval day = RightOpenInterval.of(0, 24);
        Polynomial poly = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
        ZonedDateTime convertedToGW = when.withZoneSameInstant(ZoneId.of("UTC+00:00"));
        bigT = J2000.julianCenturiesUntil(convertedToGW.truncatedTo(ChronoUnit.DAYS));
        smallT = convertedToGW.getHour() + ((double) convertedToGW.getMinute() / 60.0) + (double) convertedToGW.getSecond() / 3600.0
                + (double) convertedToGW.getNano() / (1e9 * 3600.0);

        double S = 0.000025862 * bigT * bigT + 2400.051336 * bigT + 6.697374558;
        S_0 = day.reduce(poly.at(bigT));
        S_1 = day.reduce(1.002737909 * smallT);

        result = S + S_1;
        return Angle.normalizePositive(Angle.ofHr(day.reduce(result)));
    }

    /**
     * @param when
     * @param where
     * @return sidereal local time in radians
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }


}
