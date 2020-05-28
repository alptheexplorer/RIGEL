package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.toDeg;

/**
 *  Mother class for spherical coordinates
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
abstract class SphericalCoordinates {
    private final double longitude, latitude;

    /**
     *
     * @param l longitude
     * @param la latitude
     */
    SphericalCoordinates(double l, double la) {
        this.longitude = l;
        this.latitude = la;
    }

    /**
     *
     * @return longitude
     */
    double lon() {
        return this.longitude;
    }

    /**
     *
     * @return longitude in degrees
     */
    double lonDeg() {
        return toDeg(this.longitude);
    }

    /**
     *
     * @return latitude
     */
    double lat() {
        return this.latitude;
    }

    /**
     *
     * @return latitude in degrees
     */
    double latDeg() {
        return toDeg(this.latitude);
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }


}
