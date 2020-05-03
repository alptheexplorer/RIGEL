package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class BlackBodyColor {

    //using a static method to read the txt file, we only read once to improve performance
    private static Map<Integer,String> BBRVALUES = readFromBbr();
    private BlackBodyColor(){ }

    //used to read data from bbr_color.txt and store it in a hashmap for later use
    private static Map<Integer,String> readFromBbr(){

        Map<Integer,String> bbrValues = new HashMap<>();

        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("bbr_color.txt");
             InputStreamReader asciiDecodedStream = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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
                     bbrValues.put(currentInteger,currentRGB);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return bbrValues;
    }



    /**
     * @param kelvin
     * @return a Color instance for the given temperature of blackbody in kelvin
     * @throws IllegalArgumentException if the temperature is not in the range [1000,40000]
     */
    public static Color colorForTemperature(double kelvin) throws IllegalArgumentException {
        ClosedInterval tempInterval = ClosedInterval.of(1000,40000);

        int filteredKelvin = (int) Math.round(
                Preconditions.checkInInterval(tempInterval,kelvin)/100.0
        ) * 100;
        String colArg = BBRVALUES.get(filteredKelvin);
        return Color.web(colArg);

    }

}
