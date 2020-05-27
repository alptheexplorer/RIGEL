package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Star Catalog: map Asterism -->Indeces of its stars
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class StarCatalogue {

    // Catalogue object: key is Asterism, each of which has a list of integers (the value)
    // representing the indices ( corresponding to the position of the star in starList) of the stars in asterism.
    private final HashMap<Asterism, List<Integer>> catalogue = new HashMap();
    // List to store StarList passed to constructor
    private final List<Star> starList;
    // star and corresponding index on starList
    private final HashMap<Star, Integer> starIndex = new HashMap<>();


    /**
     * Our goal here is to return a StarCatalogue based on data from resources folder. We take the given asterism put it as a key and its value is the set of indeces
     * of the corresponding stars in the starList
     * If an Asterism contains a star not in the star list, the constructor will throw an exception
     * @param stars
     * @param  asterisms
     * @throws IllegalArgumentException (trough Preconditions, if asterism contains some stars that are not starList)
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        //saves a unmodifiable (copy) list here so we can just return the attribute in the getter
        this.starList = List.copyOf(stars);
        // List Star --> index of that star(Integer)
        for(Star s: starList){
            starIndex.put(s, starList.indexOf(s));
        }
        //Put for each star of each asterism the corresponding index of that star
        //so we create catalog  Asterism --> List of indeces of the stars of that asterism
        for(Asterism a: asterisms){
            //temporary list objects to work with
            List<Integer> indices = new ArrayList();
            for(Star s: a.stars()){
                Preconditions.checkArgument(starList.contains(s));
                indices.add(starIndex.get(s));
                catalogue.put(a, indices);
            }

        }
    }

    /**
     * @return the whole star list ( immutable )
     */
    public List<Star> stars() {
        return starList;
    }

    /**
     * @return set of asterisms in catalogue (immutable)
     */
    public Set<Asterism> asterisms() {
        return Set.copyOf(catalogue.keySet());
    }

    /**
     * @param asterism
     * @return List of indices of the stars of the given @asterism as occurring in starList
     * @throws IllegalArgumentException ( from Preconditions, if asterism is not on the catalog)
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(this.asterisms().contains(asterism));
        return List.copyOf(catalogue.get(asterism));
    }


    /**
     * Builder of star catalog
     */
    public final static class Builder {
        // Builder has same attributes as StarCatalogue
        private final List<Star> star_builder;
        private final List<Asterism> asterism_builder;


        // we initialize both lists as empty
        public Builder() {
            this.star_builder = new ArrayList<Star>();
            this.asterism_builder = new ArrayList<Asterism>();
        }

        /**
         * @param star
         * @return adds star to the catalog in construction and returns Builder
         */
        public Builder addStar(Star star) {
            this.star_builder.add(star);
            return this;
        }

        /**
         * @param asterism
         * @return adds asterism to the catalog in construction and returns Builder
         */
        public Builder addAsterism(Asterism asterism) {
            this.asterism_builder.add(asterism);
            return this;
        }

        /**
         * @return currently built catalog
         */
        public StarCatalogue build() {
            return new StarCatalogue(star_builder, asterism_builder);
        }

        /**
         * @return a possibly mutable but not modifiable view of the star list
         */
        public List<Star> stars() {
            List<Star> view = Collections.unmodifiableList(star_builder);
            return view;
        }

        /**
         * @return a possibly mutable but not modifiable view of the asterism list
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterism_builder);
        }


        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }
    }


    public interface Loader {

        /**
         * Charges the stars and/or asterisms of the input stream and adds them to the catalog
         * in construction, or throws an exception in case of error.
         * @param inputStream
         * @param builder
         * @throws IOException
         *
         */
        void load(InputStream inputStream, Builder builder) throws IOException;


    }


}
