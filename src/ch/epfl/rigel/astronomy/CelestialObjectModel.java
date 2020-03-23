package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 *
 * Represents a model of a Celestial Object, i.e. a way to calculate
 * the characteristics of an object in a given instant
 *
 * It's a generic interface ( <O> ) where O is the type of objects
 * modelled by this model.
 * It has only one method.
 *
 * Theory- Position of celestial Objects:
 * A model is just a set of math equation used to determine the (approximate) position
 * of an object in a given instant.
 * Better precision means more complex mathematical equations, in the project we'll
 * trade off a bit of precision for the sake of simplicity.
 * This model though has to be exact enough to be perceived correctly at the naked eye.
 *
 * We will try to achieve a model precise  enough to be used for a temporal span of about
 * ten years  ( i.e. we are not recasting the model every frame a priori, we use it to predict
 * the movement of a celestial object in a span of 10years in the simulation )
 * (more precise = more complex = can be used for longer )
 *
 * We are allowed to adopt more precise models but only in the bonus part
 *
 * We base our model on the empiric observation (thus given as known, we'll store these I suppose)
 * of the position of the Object in a specific given epoch (here J2010), then use the formulas
 * to obtain estimated path of the object between the epoch and the observation instant to
 * have its position in that instant.
 *
 * Many celestial objects have an elliptical trajectory around another object that
 * occupies one of the foci ( a focus ) of the ellipse (ex. Solar System).
 * In this case: first we estimate the position as if it was a circular orbit (this position
 * is called Average/Mean: circular orbit, same average speed as real),
 * then adjust the result to consider that the orbit is elliptical ( Real/True ).
 * @param <O>
 *
 *
 */
public interface CelestialObjectModel<O> {

    /**
     *
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return the object modelled by the model for the number ( may be negative)
     * of days after the Epoch J2010 given (@daysSinceJ2010), using the @eclipticToEquatorialConversion
     * to obtain his equatorial coordinates starting from his ecliptic coordinates.
     *
     * Basically we need to have this method to calculate the position of the object at a given time
     * and make it ready to be displayed in equatorial coordinates. The time is given by
     * time after a known epoch (j2010) and thanks to the formulas we can find out the rest.
     *
     * I don't think we have any information about how to calculate a generic object here,
     * but we need to force the method at() in the objects that implement this interface
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}
