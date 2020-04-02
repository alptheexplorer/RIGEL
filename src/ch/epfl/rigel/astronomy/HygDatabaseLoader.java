package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {


        // everything happens inside try with resource block

        try(InputStreamReader asciiDecodedStream = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(asciiDecodedStream)){

            // all temporary values declared here
            String line;
            int hipparcosId;
            String name;
            EquatorialCoordinates equatorialPos;
            float magnitude;
            float colorIndex;
            Star currentStar;

            // we consume first row because it contains only headers
            String headerLine = buffer.readLine();

            /**
             * we separate each line from its commas and store value in an array, we access only useful elements from array.
             */
            while((line = buffer.readLine())!=null){
                String[] starVal = line.split(",");


                // assigning hipparcosId found in position 1 of line
                if(!starVal[1].isEmpty()){
                    hipparcosId = Integer.parseInt(starVal[1]);
                }
                else{
                    hipparcosId = 0;
                }

                // assigning name found in position 6 of line
                if(!starVal[6].isEmpty()){
                    name = starVal[6];
                }
                else{
                    String bayer = starVal[27];
                    if(!bayer.isEmpty()){
                        name = bayer + " " + starVal[29];
                    }
                    else{
                        name = "?" + " " + starVal[29];
                    }
                }

                //assigning equatorial coordinates found in position 23 and 24 of line, never empty!
                equatorialPos = EquatorialCoordinates.of(Double.parseDouble(starVal[23]), Double.parseDouble(starVal[24]));

                // assigning magnitude found in position 13 of line
                if(!starVal[13].isEmpty()){

                     magnitude = (float) Double.parseDouble(starVal[13]);
                }
                else{
                    magnitude = 0;
                }

                // assigning colorIndex found in position 16 of line
                if(!starVal[16].isEmpty()){
                    colorIndex = (float)Double.parseDouble(starVal[16]);
                }
                else{
                    colorIndex = 0;
                }

                currentStar = new Star(hipparcosId,name,equatorialPos,magnitude,colorIndex);
                // finally we add star to the starList inside builder.
                builder.addStar(currentStar);

            }





        }




    }
}
