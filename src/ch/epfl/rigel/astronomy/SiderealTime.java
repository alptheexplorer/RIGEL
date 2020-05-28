package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * Sidereal time
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class SiderealTime {
    //independent of when or where, useful to have it ready for the formulas
    private static final Polynomial POLYNOMIAL = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private SiderealTime() { }

    /**
     * Finds corresponding sidereal time in Greenwich (i.e. Longitude = 0)
     * @param when
     * @return greenwich converted time in radians in normalized range
     */
    public static double greenwich(ZonedDateTime when) {
        //same instant in UTC time ( Greenwich )
        ZonedDateTime whenInGW = when.withZoneSameInstant(ZoneOffset.UTC);
        //Number of Julian Centuries between instantInGW and J2000
        double bigT =  J2000.julianCenturiesUntil(whenInGW.truncatedTo(ChronoUnit.DAYS));
        //Number of hours from beginning of the day
        double smallT = whenInGW.getHour() + whenInGW.getMinute()/60.0 + whenInGW.getSecond()/3600.0 + whenInGW.getNano()/(3600.0e9);
        double gwTime = POLYNOMIAL.at(bigT) + 1.002737909*smallT;
        //Result is in hours, we put it in radians and normalize
        return Angle.normalizePositive(Angle.ofHr(gwTime));
    }

    /**
     * @param when Needed to find the corresponding sidereal Greenwich time
     * @param where Needed to create an approximation of the actual local sidereal time
     * @return sidereal local time in radians
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }


}
