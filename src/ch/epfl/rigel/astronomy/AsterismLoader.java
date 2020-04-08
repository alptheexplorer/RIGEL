package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public enum AsterismLoader implements StarCatalogue.Loader {

    // only instance
    INSTANCE;


    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (InputStreamReader asciiDecodedStream = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(asciiDecodedStream)) {

            // all temporary values declared here

            Asterism loadedAsterism;
            String line;


            /**
             * we separate each line from its commas and store value in an array, we access only useful elements from array.
             */
            while ((line = buffer.readLine()) != null) {
                List<Star> stars = new ArrayList();
                // we first separate the txt file read by line into an array separated by the comma
                String[] currentLine = line.split(",");
                int i = 0;
                // here we iterate through all members of the string array
                do {
                    // we now need to access every star already added to the starlist in the builder
                    for (Star s : builder.stars()) {
                        // if some star has the same hipparcosid as the currentLine member we add it to stars
                        if (Integer.parseInt(currentLine[i]) == s.hipparcosId()) {
                            stars.add(s);
                        }
                    }
                    ++i;
                } while (i < currentLine.length);
                // once all valid stars are added to stars, we create a new asterism using stars
                loadedAsterism = new Asterism(stars);
                // we add the asterism to the builder
                builder.addAsterism(loadedAsterism);
            }

        }


    }
}
