package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.EuclidianDistance;

import java.time.ZonedDateTime;
import java.util.*;

public class ObservedSky {

    private final StarCatalogue currentCatalogue;
    private final Sun currentSun;
    private final Moon currentMoon;
    private final CartesianCoordinates sunCoordinates, moonCoordinates;
    private final Planet[] planets = new Planet[7];
    private final Star[] stars;
    // we store planets and stars in an array
    private final double[] planetCoordinates = new double[14];
    private final double[] starCoordinates;



    /**
     * Instantiate ObservedSky object containing each object in sky as hashmap
     * with key being name as string and value being projected cartesian coordinates
     * @param when
     * @param where
     * @param projection
     * @param catalogue
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where,
                       StereographicProjection projection, StarCatalogue catalogue){

        this.currentCatalogue = catalogue;
        this.stars = new Star[catalogue.stars().size()];
        //starCoordinates must have double size of actual number of stars
        this.starCoordinates = new double[catalogue.stars().size()*2];
        // temporary variables
        EquatorialCoordinates tempEquatorial;
        HorizontalCoordinates tempHorizontal;

        // using provided information, calculate projected position in the plane of all celestial objects, namely:
        // the Sun, the Moon, the planets of the system solar - except the Earth - and the stars of the catalog.
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
        this.sunCoordinates = projection.apply(tempHorizontal);


        //Calculation for moon
        this.currentMoon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
        tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(this.currentMoon.equatorialPos());
        this.moonCoordinates = projection.apply(tempHorizontal);


        int i = 0;
        int j = 0;
        //Calculation for all planets except for earth, we loop through all planetModel instances
        for(PlanetModel P: PlanetModel.values()){
            // iterate through all enums and add their projection to the array
            if(!P.getFrenchName().equals("Terre")){
                final Planet currentPlanet; // we make sure that once currentPlanet is passed into list, list members are immutable
                CartesianCoordinates projectedCoordinate;
                currentPlanet = P.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
                tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(currentPlanet.equatorialPos());
                projectedCoordinate = projection.apply(tempHorizontal);
                planets[j] = currentPlanet;
                planetCoordinates[i] = projectedCoordinate.x();
                planetCoordinates[i+1] = projectedCoordinate.y();
                ++j;
                i+=2;
            }
        }

        int a = 0;
        int b = 0;
        //Calculation for stars of catalogue
        for(Star s:catalogue.stars()){
            Star currentStar = s;
            CartesianCoordinates projectedCoordinate;
            tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(s.equatorialPos());
            projectedCoordinate = projection.apply(tempHorizontal);
            stars[a] = currentStar;
            starCoordinates[b] = projectedCoordinate.x();
            starCoordinates[b+1] = projectedCoordinate.y();
            ++a;
            b+=2;
        }

    }

    /**
     *
     * @return immutable sun instance
     */
    public Sun sun(){
        return currentSun;
    }

    /**
     *
     * @return cartesianCoordinates of sun instance
     */
    public CartesianCoordinates sunPosition(){
        return sunCoordinates;
    }

    /**
     *
     * @return immutable moon instance
     */
    public Moon moon(){
        return currentMoon;
    }

    /**
     *
     * @return cartesianCoordinates of moon instance
     */
    public CartesianCoordinates moonPosition(){
        return moonCoordinates;
    }

    /**
     *
     * @return immutable array of planet instances excluding earth
     */
    public Planet[] planets(){
        return Arrays.copyOf((planets),planets.length);
    }

    /**
     *  @return planet coordinates, odd index being x coordinate of the ith planet and even index being ith coordinate
     */
    public double[] planetPositions(){
        return Arrays.copyOf(planetCoordinates,planetCoordinates.length);
    }

    /**
     *
     * @return immutable array of star instances
     */
    public Star[] stars(){
        return Arrays.copyOf(stars,stars.length);
    }

    /**
     *
     * @return returns star coordinates, odd index being x coordinate of the ith star and even index being ith coordinate
     */
    public double[] starPositions(){
        return Arrays.copyOf(starCoordinates,starCoordinates.length);
    }


    /**
     *
     * @return set of asterisms in catalogue
     */
    public Set<Asterism> asterisms(){
        return currentCatalogue.asterisms();
    }

    /**
     *
     * @param asterism
     * @return indeces of stars in given asterism
     */
    public List<Integer> starIndices(Asterism asterism){
        return currentCatalogue.asterismIndices(asterism);
    }


    /**
     * We cycle through the arrays and add to an Hashmap only the celestial objects in
     * the desired radius. The we find the minimum in that HashMap.
     * We handle the case of collision of more than one objectClosestTo in an arbitrary manner
     * @param xy
     * @param maxDist
     * @return closest celestial object to given position and within maxDist
     */
    public CelestialObject objectClosestTo(CartesianCoordinates xy, double maxDist){

        // contains each nearby celestial object with key the object and value the distance
        Map<CelestialObject, Double> nearbyDistances = new HashMap<>();

        // cycles trough the planets
        for(int i = 0; i<planetCoordinates.length; i+=2){
            double currentX = planetCoordinates[i];
            double currentY = planetCoordinates[i+1];
            double currentDistance = EuclidianDistance.distance(currentX,currentY,xy.x(),xy.y());
            //insert the planets inside the circle centered in xy of radius maxDist

            if(currentDistance < maxDist){
                nearbyDistances.put(planets()[i/2], currentDistance);
            }
        }

        // cycles trough the stars
        for(int i = 0; i<this.starPositions().length; i+=2){
            double currentX = starCoordinates[i];
            double currentY = starCoordinates[i+1];
            //insert the stars inside the circle centered in xy of radius maxDist
            double currentDistance = EuclidianDistance.distance(currentX,currentY,xy.x(),xy.y());
            if(currentDistance < maxDist){
                nearbyDistances.put( stars()[i/2], currentDistance);
            }
        }

        // checks Sun and Moon
        double sunDistance = EuclidianDistance.distance(sunPosition().x(),sunPosition().y(),xy.x(),xy.y());
        if(sunDistance < maxDist){
            nearbyDistances.put(sun(), sunDistance);
        }

        double moonDistance = EuclidianDistance.distance(moonPosition().x(),moonPosition().y(),xy.x(),xy.y());
        if(moonDistance < maxDist){
            nearbyDistances.put(moon(), moonDistance);
        }

        if(nearbyDistances.isEmpty()){
            return null;
        }
        else{
            //create a list in case there's more than one with the same closest distance
            CelestialObject[] closestObjects = new CelestialObject[nearbyDistances.size()];
            int i = 0;
            //find minimal distance
            double minDist = Collections.min(nearbyDistances.values());
            //find Celestial Objects corresponding to that minimal distance
            for( CelestialObject obj : nearbyDistances.keySet()) {
                if (minDist == nearbyDistances.get(obj)) {
                    closestObjects[i] = obj;
                    ++i;
                }
            }
            // to handle conflicting cases, they should be very few so the performance should stay the same
            // here we decide to take the first of the competing possible Celestial Objects
            return closestObjects[0];
        }


    }




}