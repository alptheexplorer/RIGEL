package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;

import java.util.Objects;

/**
 * mother class to all celestial object classes (Planets->Sun, Moon etc..)
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialpos;
    private final float angularSize;
    private final float magnitude;

    /**
     * package private ( by default )
     * The only constructor
     *
     * @param name          non null
     * @param equatorialPos non null
     * @param angularSize   not negative
     * @param magnitude
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {


        Preconditions.checkArgument(angularSize >= 0);
        this.angularSize = angularSize;

        this.name = Objects.requireNonNull(name);
        this.equatorialpos = Objects.requireNonNull(equatorialPos);

        this.magnitude = magnitude;
    }

    /**
     * @return name of Celestial Object
     */
    public String name() {
        return this.name;
    }

    /**
     * @return angular Size of celestial object  in double even though it was
     * constructed in float
     */
    public double angularSize() {
        return this.angularSize;
    }

    /**
     * @return magnitude of celestial object in double even though it was
     * constructed in float
     */
    public double magnitude() {
        return this.magnitude;
    }

    /**
     * @return
     */
    public EquatorialCoordinates equatorialPos() {
        return this.equatorialpos;
    }

    /**
     * @return a brief info text on the object, destined to the user
     * by default it will return name as name()
     */
    public String info() {
        return this.name;
    }

    /**
     * @return redefined toString so it returns the same as info()
     */
    public String toString() {
        return info();
    }


}
