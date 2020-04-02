package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HygDatabaseLoaderTestOurs {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    private Star s1,s2;

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    /**
    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel")){
                    rigel = s;
                }
                if(s.hipparcosId() == 21861){
                    s1 = s;
                }
                if(s.hipparcosId() == 21770){
                    s2 = s;
                }

            }
            assertNotNull(rigel);
        }
    }
     **/

    @Test
    void asterismDataBaseContainsRigel() throws IOException{
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME);
             InputStream astStream = getClass()
                     .getResourceAsStream(ASTERISM_CATALOGUE_NAME)  ) {



                StarCatalogue catalogue = new StarCatalogue.Builder()
                        .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(astStream, AsterismLoader.INSTANCE)
                        .build();

           for(Star s: catalogue.stars()){
               if(s.hipparcosId() == 21861){
                   s1 = s;
               }
               if(s.hipparcosId() == 21770){
                   s2 = s;
               }
           }

                List<Star> astBuilder = new ArrayList<>();
                astBuilder.add(s1);
                astBuilder.add(s2);
                Asterism ast = new Asterism(astBuilder);



        }

    }

}