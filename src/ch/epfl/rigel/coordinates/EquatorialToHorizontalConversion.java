package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Equatorial to horizontal coordinates conversion.
 *<p>
 *     Instantiation of the conversion object allows for faster calculation in actual
 *     conversion done in method apply()
 *</p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double cosPhi, sinPhi;
    private final double sidTime;

    /**
     * Useful object to instantiate and speed up calculations for apply()
     * @param when
     * @param where
     * @return conversion object
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.sidTime = SiderealTime.local(when, where);
        this.cosPhi = cos(where.lat());
        this.sinPhi = sin(where.lat());
    }


    /**
     * Actual conversion, returns the corresponding Horizontal Coordinates
     * @param equatorialCoordinates
     * @return Horizontal coordinates converted from equatorial coordinates
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        // H is in Hours--> converted to rad
        double H = sidTime - equatorialCoordinates.ra();

        double sinEqDec = sin(equatorialCoordinates.dec());
        double cosEqDec = cos(equatorialCoordinates.dec());

        double h = asin(
                sinEqDec * sinPhi + cosEqDec * cosPhi * cos(H)
        );
        double A = atan2(
                -cosEqDec * cosPhi * sin(H),
                sinEqDec - sinPhi * sin(h)
        );
        //need to normalize before putting into Horizontal!
        double A_NORM = Angle.normalizePositive(A);

        return HorizontalCoordinates.of(A_NORM, h);
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

}
