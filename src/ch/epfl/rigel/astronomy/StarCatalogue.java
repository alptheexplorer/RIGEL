package ch.epfl.rigel.astronomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Alp Ozen
 */
public final class StarCatalogue {

    //Catalogue object
    final HashMap<Asterism,List<Integer>> Catalogue = new HashMap();
    // List to store StarList passed to constructor
    private final List<Star> starList;


    /**
    Our goal here is to return a StarCatalogue based on data from resources folder. We take the given asterism put it as a key and its value is the set of stars
    If an Asterism contains a star not in the star list, the constructor will throw an exception
     @Throws IllegealArgumentException
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
            this.Catalogue.put(a,asterismValue);
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
     */
    public Set<Asterism> asterisms(){
        return Catalogue.keySet();
    }

    /**
     *
     * @param asterism
     * @return List containing index of each asterism as occuring in starList
     */
    public List<Integer> asterismIndices(Asterism asterism){
        return Catalogue.get(asterism);
    }

    public final static class Builder{
        Builder(){

        }
        public Builder addStar(Star star){

        }
    }


}
