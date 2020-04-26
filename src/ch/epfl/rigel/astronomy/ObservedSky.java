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

        this.currentCatalogue = catalogue;
        this.stars = new Star[catalogue.stars().size()];
        //starCoordinates must have double size of actual number of stars
        this.starCoordinates = new double[catalogue.stars().size()*2];
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
        this.sunCoordinates = projection.apply(tempHorizontal);


        //Calculation for moon
        this.currentMoon = MoonModel.MOON.at(Epoch.J2010.daysUntil(when),new EclipticToEquatorialConversion(when));
        tempHorizontal = new EquatorialToHorizontalConversion(when,where).apply(this.currentMoon.equatorialPos());
        this.moonCoordinates = projection.apply(tempHorizontal);


        int i = 0;
        int j = 0;
        //Calculation for all planets except for earth, we loop through all planetModel instances
        for(PlanetModel P: PlanetModel.values()){
            // iterate through all enums and add their projection to the hashmap
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
    public double[] planetCoordinates(){
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
    public double[] starCoordinates(){
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

    //TODO fix implementation to avoid duplicates
    /**
     *
     * @param xy
     * @param maxDist
     * @return closest celestial object to given position and within maxDist
     */
    public CelestialObject objectClosestTo(CartesianCoordinates xy, double maxDist){
            // contains each nearby celestial object with distance as key and object as value
            Map<Double, CelestialObject> nearbyDistances = new HashMap<>();

            for(int i = 0; i<planetCoordinates.length;i+=2){
                double currentX = planetCoordinates[i];
                double currentY = planetCoordinates[i+1];
                double currentDistance = EuclidianDistance.distanceTo(currentX,currentY,xy.x(),xy.y());
                // the distance at index i in planetDistances is the planet at index i in planets
                if(currentDistance<=maxDist){
                    nearbyDistances.put(currentDistance,planets()[i]);
                }
            }

            for(int i = 0; i<this.starCoordinates().length;i+=2){
                double currentX = starCoordinates[i];
                double currentY = starCoordinates[i+1];
                // the distance at index i in starDistances is the star at index i in stars
                double currentDistance = EuclidianDistance.distanceTo(currentX,currentY,xy.x(),xy.y());
                if(currentDistance<=maxDist){
                    nearbyDistances.put(currentDistance,stars()[i]);
                }
            }

            double sunDistance = EuclidianDistance.distanceTo(sunPosition().x(),sunPosition().y(),xy.x(),xy.y());
            if(sunDistance <= maxDist){
                nearbyDistances.put(sunDistance,sun());
            }

            double moonDistance = EuclidianDistance.distanceTo(moonPosition().x(),moonPosition().y(),xy.x(),xy.y());
            if(moonDistance <= maxDist){
                nearbyDistances.put(moonDistance,moon());
            }

            if(nearbyDistances.isEmpty()){
                return null;
            }
            else{
                List<Double> distances = new ArrayList<>();
                for(double key:nearbyDistances.keySet()){
                    distances.add(key);
                }
                double minVal = Collections.min(distances);
                //TODO handle case for same distance
                return nearbyDistances.get(minVal);
            }

    }




}
