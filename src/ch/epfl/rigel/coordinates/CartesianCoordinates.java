package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates {

    private static double absX;
    private static double ordY;

    private CartesianCoordinates(double x,double y) {
        absX = x;
        ordY = y;
    }

    /**
     *
     * @param x
     * @param y
     * @return Cartesian coordinates(x,y)
     */
    public static CartesianCoordinates of(double x, double y){
        CartesianCoordinates coordinates = new CartesianCoordinates(x,y);

        return coordinates;
    }

    public double x(){
        return absX;
    }

    public double y(){
        return ordY;
    }

    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return text rapresentation of Coordinates, (free format)
     */
    public String toString(){
        return String.format(Locale.ROOT,
                "(%f,%f)",
                absX,
                ordY);
    }

}
