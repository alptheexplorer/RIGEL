package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.*;

public class ObservedSky {

    private final Map<String,CartesianCoordinates> entityPos = new HashMap();
    private final Sun currentSun;
    private final Moon currentMoon;
    // we store planets and stars in an arraylist
    private final double[] planetCoordinates = new double[14];
    private final double[] starCoordinates;


    /**
     * Insantitates ObservedSky object containing each object in sky as hashmap with key being name as string and value being projected cartesian coordinates
     * @param when
     * @param where
     * @param projection
     * @param catalogue
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection projection, StarCatalogue catalogue){

        this.starCoordinates = new double[catalogue.stars().size()];
        // temporary variables
        EquatorialCoordinates tempEquatorial;
        HorizontalCoordinates tempHorizontal;

        // using provided information, calculate projected position in the plane of all celestial objects, namely: the Sun, the Moon, the planets of the system solar - except the Earth - and the stars of the catalog.
        /**
         * Objects for which calculations made:
         * SUN
         * MOON
         * ALL PLANETS \ EARTH
         */
        // Calculation for sun
        this.currentSun = SunModel.SUN.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
        tempEquatorial = new EclipticToEquatorialConversion(when).apply(this.currentSun.eclipticPos());
        tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(tempEquatorial);
        entityPos.put("Sun",projection.apply(tempHorizontal));

        //Calculation for moon
        this.currentMoon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
        tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(this.currentMoon.equatorialPos());
        entityPos.put("Moon",projection.apply(tempHorizontal));

        int i = 0;
        //Calculation for all planets except for earth, we loop through all planetModel instances
        for(PlanetModel P: PlanetModel.values()){
            // iterate through all enums and add their projection to the hashmap
            if(!P.name().equals("Terre")){
                final Planet currentPlanet; // we make sure that once currentPlanet is passed into list, list members are immutable
                currentPlanet = P.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
                tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(currentPlanet.equatorialPos());
                entityPos.put(currentPlanet.name(),projection.apply(tempHorizontal));
                currentPlanets.add(currentPlanet);
                planetCoordinates[i] = projection.apply(tempHorizontal).x();
                planetCoordinates[i+1] = projection.apply(tempHorizontal).y();
                ++i;
            }
        }

        int j = 0;
        //Calculation for stars of catalogue
        for(Star s:catalogue.stars()){
            final Star currentStar; // we make sure that once currentPlanet is passed into list, list members are immutable
            currentStar = s;
            tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(s.equatorialPos());
            entityPos.put(currentStar.name(),projection.apply(tempHorizontal));
            currentStars.add(currentStar);
            starCoordinates[j] = projection.apply(tempHorizontal).x();
            starCoordinates[j+1] = projection.apply(tempHorizontal).y();
            ++j;
        }

    }

    //returns the sun object in sky
    public Sun sun(){
        return currentSun;
    }

    //returns cartesian coordinates of sun
    public CartesianCoordinates sunPosition(){
        return entityPos.get("Sun");
    }

    //returns the sun object in sky
    public Moon moon(){
        return currentMoon;
    }

    //returns cartesian coordinates of sun
    public CartesianCoordinates moonPosition(){
        return entityPos.get("Moon");
    }

    public double[] planet(){
        return planetCoordinates;
    }






}
