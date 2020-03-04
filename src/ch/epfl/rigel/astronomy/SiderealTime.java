package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * @author Alp-Ozen
 */
public final class SiderealTime {
    private SiderealTime(){ }

    /**
     *
     * @param when
     * @return
     */

    public static double greenwich(ZonedDateTime when){
        double bigT;
        double smallT;
        double S_0;
        double S_1;
        double result;
        ZonedDateTime convertedToGW = when.withZoneSameInstant(ZoneId.of("UTC+00:00"));
        bigT = J2000.julianCenturiesUntil(convertedToGW.truncatedTo(ChronoUnit.DAYS));
        smallT = when.getHour() +((double)when.getMinute()/60.0) +(double)when.getSecond()/3600.0
                + (double)when.getNano()/(1e9 * 3600.0);

        S_0 = 0.000025862*bigT*bigT + 2400.051336*bigT + 6.697374558;
        S_1 = 1.002737909*smallT;
        result = S_0 + S_1;
        //double resNormHr = RightOpenInterval.of(0,24).reduce(result);

        return Angle.normalizePositive(Angle.ofHr(result));
    }

    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return (greenwich(when) + where.lon());
    }


}
