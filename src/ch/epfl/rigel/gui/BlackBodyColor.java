package ch.epfl.rigel.gui;

import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class BlackBodyColor {

    //using a static method to read the txt file, we only read once to improve performance
    private static Map<Integer,String> BBRVALUES = readFromBbr();
    private BlackBodyColor(){ }

    //used to read data from bbr_color.txt and store it in a hashmap for later use
    private static Map<Integer,String> readFromBbr(){

        Map<Integer,String> BBRVALUES = new HashMap<>();
        File file = new File("C:\\Users\\alpoz\\Desktop\\projectBA2\\resources\\bbr_color.txt");


        try (InputStream inputStream = new FileInputStream(file);
                InputStreamReader asciiDecodedStream = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(asciiDecodedStream)) {

            String line;
            int currentInteger;
            String currentRGB;
            while ((line = buffer.readLine()) != null) {
                // we ignore the # and 2deg cases
                if((line.charAt(0)!='#') && (line.charAt(11)!='2')){
                    // returns integer at [1,6] cutting off space at beginning if there is any
                     currentInteger = Integer.parseInt(line.substring(1,6).trim());
                     System.out.println(currentInteger);
                     currentRGB = line.substring(80,87);
                     BBRVALUES.put(currentInteger,currentRGB);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return BBRVALUES;
    }

    //returns a Color instance for the given temperature of blackbody in Kelvin
    public static Color colorForTemperature(double Kelvin) throws IllegalArgumentException {

        // we check that argument is valid
        ClosedInterval tempInterval = ClosedInterval.of(1000,40000);
        if(!tempInterval.contains(Kelvin)){
            // IA exception if argument not in range
            throw new IllegalArgumentException();
        }
        else{
            // we then round kelvin to the nearest 100th
            int filteredKelvin = (int)(Math.round( Kelvin / 100.0) * 100);
            String colArg = BBRVALUES.get(filteredKelvin);
            return Color.web(colArg);
        }

    }

}
