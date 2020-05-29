package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads from bbr_color database to obtain color corresponding to blackbody, access to color provided by colorForTemperature method
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */

public class BlackBodyColor {

    //using a static method to read the txt file, we only read once to improve performance
    private static Map<Integer,String> BBRVALUES = readFromBbr();
    // non-instaniable
    private BlackBodyColor(){ }

    private static InputStream resourceStream (String resourceName)  {
        return BlackBodyColor.class.getResourceAsStream(resourceName);
    }


    //used to read data from bbr_color.txt and store it in a hashmap for later use
    private static Map<Integer,String> readFromBbr(){

        Map<Integer,String> BBRVALUES = new HashMap<>();



        try (InputStream hs = resourceStream ( "/bbr_color.txt" );
                InputStreamReader asciiDecodedStream = new InputStreamReader(hs);
             BufferedReader buffer = new BufferedReader(asciiDecodedStream)) {

            String line;
            int currentInteger;
            String currentRGB;
            while ((line = buffer.readLine()) != null) {
                // we ignore the # and 2deg cases
                if((line.charAt(0)!='#') && (line.charAt(11)!='2')){
                    // returns integer at [1,6] cutting off space at beginning if there is any
                     currentInteger = Integer.parseInt(line.substring(1,6).trim());
                     currentRGB = line.substring(80,87);
                     BBRVALUES.put(currentInteger,currentRGB);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return BBRVALUES;
    }


    /**
     *returns a Color instance for the given temperature of blackbody in Kelvin
     * @param Kelvin
     * @return
     * @throws  IllegalArgumentException if temp is not in [1000, 40000]
     */
    public static Color colorForTemperature(double Kelvin)  {

        ClosedInterval tempInterval = ClosedInterval.of(1000,40000);
        Preconditions.checkInInterval(tempInterval,Kelvin);
        int filteredKelvin = (int)(Math.round( Kelvin / 100.0) * 100);
        String colArg = BBRVALUES.get(filteredKelvin);
        return Color.web(colArg);

    }

}
