package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Asterism are referred to as Constellation in normal language, even though
 * it's not strictly correct.
 * They are a group of particularly bright stars and are helpful to visualize better
 * the sky
 *
 * Public, final and immutable class, represents an Asterism, which in this
 * project is just a list of stars, so this class is very basic
 */
public final class Asterism {

    private final  List<Star> asterism;

    /**
     * Construct the asterism ( a list of stars), throws exception if null or empty
     * @param stars
     */
    public Asterism(List<Star> stars){

        Objects.requireNonNull(stars);
        Preconditions.checkArgument(!stars.isEmpty());
        this.asterism = List.copyOf(stars);

    }

    /**
     *
     * @return list of stars of the asterism
     */
    public List<Star> stars(){

        return List.copyOf(asterism);
    }
}
