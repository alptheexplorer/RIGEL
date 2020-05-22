package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class CartesianCoordinates {

    private final double ordY;
    private final double absX;

    private CartesianCoordinates(double x, double y) {
        this.absX = x;
        this.ordY = y;
    }

    /**
     * @param x
     * @param y
     * @return Cartesian coordinates(x,y)
     */
    public static CartesianCoordinates of(double x, double y) {
        CartesianCoordinates coordinates = new CartesianCoordinates(x, y);

        return coordinates;
    }

    /**
     *
     * @return absX
     */
    public double x() {
        return this.absX;
    }

    /**
     *
     * @return ordY
     */
    public double y() {
        return this.ordY;
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return text rapresentation of Coordinates, (free format)
     */
    public String toString() {
        return String.format(Locale.ROOT,
                "(%f,%f)",
                absX,
                ordY);
    }

}
