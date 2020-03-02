package ch.epfl.rigel.astronomy;
import java.time.;

// represents an astronomical epoch
public enum Epoch {
    //J2000 =  1er janvier 2000 à 12h00 UTC (GMT)

    //J2010 = 31 décembre 2009 (!) à 0h00 UTC (GMT)

    /**
     *
     * @param when
     * @return number of days between the instance's (this) epoch and @when
     * negative values corresponds to anterior dates, positive to posterior
     */
    double daysUntil(ZonedDateTime when){

    }

    /**
     *
     * @param when
     * @return number of centuries between the instance's (this) epoch and @when
     * negative values corresponds to anterior dates, positive to posterior
     */
    double julianCenturiesUntil(ZonedDateTime when){

    }
}

