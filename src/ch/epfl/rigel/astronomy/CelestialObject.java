package ch.epfl.rigel.astronomy;
import ch.epfl.rigel.coordinates.*;

import java.util.Objects;

/**
 * mother class to all celestial object classes (Planets->Sun, Moon etc..)
 */
public abstract class CelestialObject {

    String name;
    EquatorialCoordinates equatorialpos;
    float angularSize;
    float magnitude;
    /**
     * package private ( by default )
     * The only constructor
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
       // TODO: check requiredNonNull is used correctly or use the if
        /*if (name== null || equatorialPos== null){
            throw new NullPointerException();
        }
        else {
            this.name = name;
            this.equatorialpos = equatorialPos;
        }
         */

        if ( angularSize<0) throw new IllegalArgumentException();
        else this.angularSize = angularSize;

        this.name = Objects.requireNonNull(name);
        this.equatorialpos = Objects.requireNonNull(equatorialPos);

        this.magnitude = magnitude;
        Objects.requireNonNull(name);
    }

    /**
     *
     * @return name of Celestial Object
     */
    public String name(){
        return this.name;
    }

    /**
     *
     * @return angular Size of celestial object  in double even though it was
     * constructed in float
     */
    public double angularSize(){
        return (double)this.angularSize;
    }

    /**
     *
     * @return magnitude of celestial object in double even though it was
     * constructed in float
     */
    public double magnitude(){
        return (double)this.magnitude;
    }

    /**
     *
     * @return
     */
    public EquatorialCoordinates equatorialPos(){
        return this.equatorialpos;
    }

    /**
     *
     * @return a brief info text on the object, destined to the user
     * by default it will return name as name()
     */
    String info(){
        return this.name;
    }

    /**
     *
     * @return redefined toString so it returns the same as info()
     */
    public String toString(){
        return info();
    }


}
