package ch.epfl.rigel.astronomy;

import javax.xml.catalog.Catalog;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Alp Ozen
 */
public final class StarCatalogue {

    //Catalogue object: key is Asterism, each of which has a list of integers (the value)
    // representing the indices ( corresponding to the position of the star in starList) of the stars in asterism.
    private final HashMap<Asterism,List<Integer>> catalogue = new HashMap();
    // List to store StarList passed to constructor
    private final List<Star> starList;



    /**
    Our goal here is to return a StarCatalogue based on data from resources folder. We take the given asterism put it as a key and its value is the set of indeces
    of the corresponding stars in the starList
    If an Asterism contains a star not in the star list, the constructor will throw an exception
     @Throws IllegalArgumentException
     //TODO  check other classes to see if we need to add the throws, When should we write like this?
     **/
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) throws IllegalArgumentException{
        this.starList = stars;
        List indeces = new ArrayList();
        // in this loop, we traverse through all asterisms and add a stars index to it if asterism contains star
        for(Asterism a: asterisms){
            for(Star s:stars){
                //temporary list object to work with
                List asterismStars = a.stars();
                if(asterismStars.contains(s) && !starList.contains(s)){ //TODO check
                    // we throw an IAexception if the asterism has a star not in the actual star list
                    throw new IllegalArgumentException();
                }
                else if(asterismStars.contains(s)){
                    // we add the index of the star to the List which is the value of the asterism
                    indeces.add(starList.indexOf(s));
                }
            }
            //finally we add the asterism-list pair to the catalogue
            this.catalogue.put(a,indeces);
        }
    }

    /**
     * @return the whole star list ( immutable )
     */
    public List<Star> stars(){
        // returns a copy of original starList
        return List.copyOf(starList);
    }

    /**
     *
     * @return set of asterisms in catalogue (immutable)
     */
    public Set<Asterism> asterisms(){

        return Set.copyOf(catalogue.keySet());
    }

    /**
     * @param asterism
     * @throws IllegalArgumentException
     * @return List of indices of the stars of the given @asterism as occurring in starList
     */
    public List<Integer> asterismIndices(Asterism asterism){
        if(asterisms().contains(asterism)){
            return List.copyOf(catalogue.get(asterism));
        }
        else{
            throw new IllegalArgumentException();
        }
    }


    /**
     * Builder of star catalog
     */
    public final static class Builder{
        // Builder has same attributes as StarCatalogue
        private List<Star> star_builder;
        private List<Asterism> asterism_builder;


        // we initialize both lists as empty
        public Builder(){
            this.star_builder = new ArrayList<Star>();
            this.asterism_builder = new ArrayList<Asterism>();
        }

        /**
         *
         * @param star
         * @return adds star to the catalog in construction and returns Builder
         */
        public Builder addStar(Star star){
            this.star_builder.add(star);
            return this;
        }

        /**
         *
         * @param asterism
         * @return adds asterism to the catalog in construction and returns Builder
         */
        public Builder addAsterism(Asterism asterism){
            this.asterism_builder.add(asterism);
            return this;
        }

        /**
         * @return currently built catalog
         */
        public StarCatalogue build(){
            return new StarCatalogue(star_builder,asterism_builder);
        }

        /**
         *
         * @return a possibly mutable but not modifiable view of the star list
         */
        public List<Star> stars(){
            List<Star> view = Collections.unmodifiableList(star_builder);
            return view;
        }

        /**
         *
         * @return a possibly mutable but not modifiable view of the asterism list
         */
        public List<Asterism> asterisms(){
            List<Asterism> view = Collections.unmodifiableList(asterism_builder);
            return view;
        }


        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            loader.load(inputStream,this);
            return this;
        }
    }


    public interface Loader{

        /**
         *
         * @param inputStream
         * @param builder
         * @throws IOException
         * Charges the stars and/or asterisms of the input stream and adds them to the catalog
         * in construction, or throws an exception in case of error.
         *
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;


    }


}
