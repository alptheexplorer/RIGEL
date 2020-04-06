package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AsterismLoaderTest {
    private static Map<Integer, List<Integer>> asterisms = Map.of(
            35904, List.of(35904, 35205, 34444, 33579),
            34444, List.of(34444, 33977, 32349, 30324),
            37279, List.of(37279, 36188));

    private static String asterismsString() {
        var b = new StringBuilder();
        for (var hipList : asterisms.values()) {
            var j = new StringJoiner(",", "", "\n");
            for (var hip : hipList)
                j.add(hip.toString());
            b.append(j.toString());
        }
        return b.toString();
    }

    private static InputStream streamWithAsciiString(String asciiStrings) {
        return new ByteArrayInputStream(asciiStrings.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    void loaderWorks() throws IOException {
        var stars = List.of(
                new Star(35904, "Aludra", EquatorialCoordinates.of(1.9377301884962992, -0.5114356374748288), 2.450f, -0.083f),
                new Star(35205, "XXX", EquatorialCoordinates.of(1.9049537265834606, -0.48661836778492), 4.660f, 1.589f),
                new Star(34444, "Wezen", EquatorialCoordinates.of(1.869210132341708, -0.46064823373741876), 1.830f, 0.671f),
                new Star(33579, "Adhara", EquatorialCoordinates.of(1.8265996485677483, -0.5056582518554612), 1.500f, -0.211f),
                new Star(33977, "YYY", EquatorialCoordinates.of(1.8457927748229326, -0.41596939743728123), 3.020f, -0.077f),
                new Star(32349, "Sirius", EquatorialCoordinates.of(1.7677953696021995, -0.291751258517685), -1.44f, 0.009f),
                new Star(30324, "Mirzam", EquatorialCoordinates.of(1.6698426956468702, -0.31338988393796), 1.980f, -0.240f),
                new Star(37279, "Procyon", EquatorialCoordinates.of(2.004083041242831, 0.09119333187796), 0.400f, 0.432f),
                new Star(36188, "Gomeisa", EquatorialCoordinates.of(1.9510630590622124, 0.144675846904975), 2.890f, -0.097f));

        var starMap = new HashMap<Integer, Star>();
        for (var star : stars)
            starMap.put(star.hipparcosId(), star);

        try (var stream = streamWithAsciiString(asterismsString())) {
            var b = new StarCatalogue.Builder();
            for (var star : stars)
                b.addStar(star);
            AsterismLoader.INSTANCE.load(stream, b);
            var c = b.build();
            assertEquals(asterisms.size(), c.asterisms().size());
            for (var asterism : c.asterisms()) {
                var actualStars = asterism.stars();
                var expectedHips = asterisms.get(actualStars.get(0).hipparcosId());
                var expectedStars = new ArrayList<Star>();
                for (var hip : expectedHips)
                    expectedStars.add(starMap.get(hip));
                assertEquals(expectedStars, actualStars);
            }
        }
    }

    @Test
    void asterismLoaderCorrectlyHandlesIOExceptions() {
        IOException fakeException = new IOException("fake IO exception");
        try {
            AsterismLoader.INSTANCE.load(new ThrowingInputStream(fakeException), new StarCatalogue.Builder());
            fail("no IO exception thrown");
        } catch (IOException e) {
            assertEquals(fakeException, e);
        }
    }

    private static final class ThrowingInputStream extends InputStream {
        private final IOException exceptionToThrow;

        public ThrowingInputStream(IOException exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

        @Override
        public int read() throws IOException {
            throw exceptionToThrow;
        }
    }
}
