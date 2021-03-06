package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Planet, offers only a constructor:
 * <p>
 * note that since it is static and its equatorial position
 * is passed at construction, one of its instances can only
 * represent a planet at a given location.
 * We will therefore have to respawn instances of Planet for each image
 * of an animation in a different location ( to make it move on the screen )
 * </p>
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Planet extends CelestialObject {
    /**
     * The only constructor
     *
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
