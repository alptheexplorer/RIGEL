package ch.epfl.rigel.astronomy;

import javax.xml.catalog.Catalog;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Alp Ozen
 */
public final class StarCatalogue {

    //Catalogue object: key is Asterism, each of which has a list of integer ( the value )
    // representing the hipparcus number of the star. So each key(asterism) has a value(list). HashMap.
    private final HashMap<Asterism,List<Integer>> catalogue = new HashMap();
    // List to store StarList passed to constructor
    private final List<Star> starList;



    /**
    Our goal here is to return a StarCatalogue based on data from resources folder. We take the given asterism put it as a key and its value is the set of stars
    If an Asterism contains a star not in the star list, the constructor will throw an exception
     @Throws IllegalArgumentException
     //TODO we never put the throws like this before
     **/
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) throws IllegalArgumentException{
        this.starList = stars;
        // in this loop, we traverse through all asterisms and add a stars index to it if asterism contains star
        for(Asterism a: asterisms){
            List asterismValue = new ArrayList();
            for(Star s:stars){
                //temporary list object to work with
                List tempList = a.stars();
                if(tempList.contains(s) && !starList.contains(s)){
                    // we throw an IAexception if the asterism has a star not in the actual star list
                    throw new IllegalArgumentException();
                }
                else if(tempList.contains(s)){
                    // we add the index of the star to the List which is the value of the asterism
                    asterismValue.add(starList.indexOf(s));
                }
            }
            //finally we add the asterism-list pair to the catalogue
            this.catalogue.put(a,asterismValue);
        }
    }

    /**
     *
     * @return copy of starlist
     */
    public List<Star> Star(){
        // returns a copy of original starList
        return List.copyOf(starList);
    }

    /**
     *
     * @return set of asterisms in catalogue
     * TODO should we return a copy here too?
     */
    public Set<Asterism> asterisms(){

        return catalogue.keySet();
    }

    /**
     *
     * @param asterism
     * @throws IllegalArgumentException
     * @return List of indeces of the stars of the given @asterism as occuring in starList
     * TODO wrong, we want the indeces of the list of the asterism. The indeces in starList.
     * We need to:
     * check if asterism is in catalog
     * take the list of stars in asterism
     * find the indeces of those stars in the list starList
     * give back a list with those indeces, in the same order as they appeared in asterism
     *
     * Big difference: in asterism there's a list of hypparcus number. In starList we got stars,
     * stored in a certain position ( index) of this list. We want this index, not the hypparcus number.
     */
    public List<Integer> asterismIndices(Asterism asterism){
        if(asterisms().contains(asterism)){
            List indeces = new List<Integer>;
            List<Integer> starsHypNumber = catalogue.get(asterism);
            for (Integer i : starsHypNumber){
                indeces = starList
            }

        }
        else{
            throw new IllegalArgumentException();
        }
    }

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
         * @return global builder object
         */
        public Builder addStar(Star star){
            this.star_builder.add(star);
            return this;
        }

        /**
         *
         * @param asterism
         * @return global builder object
         */
        public Builder addAsterism(Asterism asterism){
            this.asterism_builder.add(asterism);
            return this;
        }

        /**
         *
         * @return currently built catalogue
         *
         */
        public StarCatalogue build(){
            return new StarCatalogue(star_builder,asterism_builder);
        }

        /**
         *
         * @return a mutable view of starlist
         */
        public List<Star> stars(){
            return this.star_builder;
        }

        /**
         *
         * @return a mutable view of asterism
         */
        public List<Asterism> asterisms(){
            return this.asterism_builder;
        }

        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            
        }
    }

    public interface Loader{
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;


    }


}