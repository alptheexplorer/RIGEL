package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StarCatalogueTest {
    private static String randomStarName(SplittableRandom rng) {
        var vowels = "aeiouy";
        var consonants = "bcdfghjklmnpqrstvwxz";
        var nameLen = rng.nextInt(3, 10);
        var nameBuilder = new StringBuilder(nameLen);
        for (int i = 0; i < nameLen; i++) {
            var actualAlphabet = ((i % 2) == 0) ? consonants : vowels;
            if (i == 0)
                actualAlphabet = actualAlphabet.toUpperCase();
            nameBuilder.append(actualAlphabet.charAt(rng.nextInt(actualAlphabet.length())));
        }
        return nameBuilder.toString();
    }

    private static EquatorialCoordinates randomEquatorialCoordinates(SplittableRandom rng) {
        var ra = rng.nextDouble(0, 2d * Math.PI);
        var dec = rng.nextDouble(-Math.PI / 2d, Math.PI / 2d);
        return EquatorialCoordinates.of(ra, dec);
    }

    private static Star randomStar(SplittableRandom rng, int hipparcosId) {
        var name = randomStarName(rng);
        var equatorialPos = randomEquatorialCoordinates(rng);
        var magnitude = (float) rng.nextDouble(-30, 30);
        var colorIndex = (float) rng.nextDouble(-0.5, 5.5);
        return new Star(hipparcosId, name, equatorialPos, magnitude, colorIndex);
    }

    private static List<Star> randomStars(SplittableRandom rng, int count) {
        var stars = new ArrayList<Star>(count);
        while (stars.size() != count) {
            int hipparcosId = stars.size() + 1;
            stars.add(randomStar(rng, hipparcosId));
        }
        return Collections.unmodifiableList(stars);
    }

    private static List<Asterism> randomAsterisms(SplittableRandom rng, List<Star> stars) {
        var shuffledStars = new ArrayList<>(stars);
        var asterisms = new ArrayList<Asterism>();
        var starsToAdd = (stars.size() * 3) / 4;
        while (starsToAdd > 0) {
            Collections.shuffle(shuffledStars, new Random(rng.nextLong()));
            var i1 = rng.nextInt(shuffledStars.size());
            var i2 = rng.nextInt(shuffledStars.size());
            var startIndex = Math.min(i1, i2);
            var endIndex = Math.max(i1, i2) + 1;
            var asterismStars = shuffledStars.subList(startIndex, endIndex);
            asterisms.add(new Asterism(asterismStars));
            starsToAdd -= asterismStars.size();
        }
        return Collections.unmodifiableList(asterisms);
    }

    @Test
    void starCatConstructorFailsForInvalidAsterism() {
        var rng = TestRandomizer.newRandom();
        var random = new Random(rng.nextLong());
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS / 4; i++) {
            var starCount = rng.nextInt(1, 500);
            var stars = randomStars(rng, starCount);

            var shuffledStars = new ArrayList<>(stars);
            Collections.shuffle(shuffledStars, random);
            var fakeAsterismSize = rng.nextInt(shuffledStars.size()) + 1;
            var fakeAsterismStars = new ArrayList<>(shuffledStars.subList(0, fakeAsterismSize));
            fakeAsterismStars.add(randomStar(rng, stars.size() + 1));
            Collections.shuffle(fakeAsterismStars, random);

            var asterisms = new ArrayList<>(randomAsterisms(rng, stars));
            asterisms.add(new Asterism(fakeAsterismStars));
            Collections.shuffle(asterisms, random);

            assertThrows(IllegalArgumentException.class, () -> {
                new StarCatalogue(stars, asterisms);
            });
        }
    }

    @Test
    void starCatConstructorWorksForValidAsterism() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var starCount = rng.nextInt(1, 500);

            var stars = randomStars(rng, starCount);
            var starSet = new HashSet<>(stars);

            var asterisms = randomAsterisms(rng, stars);
            var asterismsSet = new HashSet<>(asterisms);

            var catalogue = new StarCatalogue(stars, asterisms);

            assertEquals(starSet, new HashSet<>(catalogue.stars()));
            assertEquals(asterismsSet, catalogue.asterisms());
        }
    }

    @Test
    void starCatConstructorAllowsEmptyLists() {
        var rng = TestRandomizer.newRandom();
        new StarCatalogue(List.of(), List.of());
        new StarCatalogue(randomStars(rng, 1), List.of());
    }

    @Test
    void starCatConstructorCopiesStarList() {
        var rng = TestRandomizer.newRandom();
        var immutableStars = randomStars(rng, 20);
        var stars = new ArrayList<>(immutableStars);
        var asterisms = randomAsterisms(rng, immutableStars);
        var c = new StarCatalogue(stars, asterisms);
        stars.clear();
        assertEquals(immutableStars.size(), c.stars().size());
    }

    @Test
    void starsDoesNotAllowEncapsulationViolation() {
        var rng = TestRandomizer.newRandom();
        var stars = randomStars(rng, 20);
        var asterisms = randomAsterisms(rng, stars);
        var c = new StarCatalogue(stars, asterisms);
        try {
            c.stars().clear();
        } catch (UnsupportedOperationException e) {
            // If UOE is thrown, the list is unmodifiable, which is correct.
        }
        assertEquals(stars.size(), c.stars().size());
    }

    @Test
    void asterismsDoesNotAllowEncapsulationViolation() {
        var rng = TestRandomizer.newRandom();
        var stars = randomStars(rng, 20);
        var asterisms = randomAsterisms(rng, stars);
        var c = new StarCatalogue(stars, asterisms);
        try {
            c.asterisms().clear();
        } catch (UnsupportedOperationException e) {
            // If UOE is thrown, the set is unmodifiable, which is correct.
        }
        assertEquals(asterisms.size(), c.asterisms().size());
    }

    @Test
    void asterismIndicesDoesNotAllowEncapsulationViolation() {
        var rng = TestRandomizer.newRandom();
        var stars = randomStars(rng, 20);
        var asterisms = randomAsterisms(rng, stars);
        var c = new StarCatalogue(stars, asterisms);
        for (var asterism : asterisms) {
            try {
                c.asterismIndices(asterism).clear();
            } catch (UnsupportedOperationException e) {
                // If UOE is thrown, the list is unmodifiable, which is correct.
            }
        }
        for (var asterism : asterisms) {
            var indexIt = c.asterismIndices(asterism).iterator();
            for (var star : asterism.stars())
                assertEquals(star, stars.get(indexIt.next()));
            assertFalse(indexIt.hasNext());
        }
    }

    @Test
    void asterismIndicesWorks() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var starCount = rng.nextInt(1, 500);

            var stars = randomStars(rng, starCount);
            var asterisms = randomAsterisms(rng, stars);
            var catalogue = new StarCatalogue(stars, asterisms);

            for (var asterism : asterisms) {
                var indexIt = catalogue.asterismIndices(asterism).iterator();
                for (var star : asterism.stars())
                    assertEquals(star, stars.get(indexIt.next()));
                assertFalse(indexIt.hasNext());
            }
        }
    }

    @Test
    void starCatBuilderStartsEmpty() {
        var c = new StarCatalogue.Builder()
                .build();
        assertTrue(c.stars().isEmpty());
        assertTrue(c.asterisms().isEmpty());
    }

    @Test
    void starCatBuilderStarsIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new StarCatalogue.Builder()
                    .stars()
                    .add(randomStar(TestRandomizer.newRandom(), 1));
        });
    }

    @Test
    void starCatBuilderAsterismsIsUnmodifiable() {
        var rng = TestRandomizer.newRandom();
        var stars = randomStars(rng, 20);
        var asterisms = randomAsterisms(rng, stars);
        var asterism = asterisms.get(0);
        assertThrows(UnsupportedOperationException.class, () -> {
            new StarCatalogue.Builder()
                    .asterisms()
                    .add(asterism);
        });
    }

    @Test
    void starCatBuilderWorks() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var starsCount = rng.nextInt(1, 200);
            var stars = randomStars(rng, starsCount);
            var starSet = new HashSet<>(stars);

            var asterisms = randomAsterisms(rng, stars);
            var asterismSet = new HashSet<>(asterisms);

            var catalogueBuilder = new StarCatalogue.Builder();
            for (var star : stars)
                catalogueBuilder.addStar(star);
            for (var asterism : asterisms)
                catalogueBuilder.addAsterism(asterism);
            var catalogue = catalogueBuilder.build();

            assertEquals(starSet, new HashSet<>(catalogue.stars()));
            assertEquals(asterismSet, catalogue.asterisms());
        }
    }

    @Test
    void starCatBuilderLoadFromWorks() throws IOException {
        var loader = new FakeLoader();
        var inputStream = new ByteArrayInputStream(new byte[0]);
        var catBuilder = new StarCatalogue.Builder();
        catBuilder
                .loadFrom(inputStream, loader)
                .loadFrom(inputStream, loader)
                .loadFrom(inputStream, loader)
                .loadFrom(inputStream, loader);
        assertEquals(4, loader.invokeCount);
        assertEquals(inputStream, loader.inputStream);
        assertEquals(catBuilder, loader.builder);
    }

    @Test
    void starCatBuilderLoadFromCorrectlyForwardsIOExceptions() {
        IOException exception = new IOException("fake IO exception");
        var loader = new FakeLoader(exception);
        var inputStream = new ByteArrayInputStream(new byte[0]);
        StarCatalogue.Builder catBuilder = new StarCatalogue.Builder();
        try {
            catBuilder
                    .loadFrom(inputStream, loader)
                    .loadFrom(inputStream, loader)
                    .loadFrom(inputStream, loader)
                    .loadFrom(inputStream, loader);
            fail("No exception thrown!");
        } catch (IOException e) {
            assertEquals(exception, e);
            assertEquals(1, loader.invokeCount);
            assertEquals(inputStream, loader.inputStream);
            assertEquals(catBuilder, loader.builder);
        }
    }

    private static class FakeLoader implements StarCatalogue.Loader {
        int invokeCount;
        InputStream inputStream;
        StarCatalogue.Builder builder;
        IOException exceptionToThrow;

        public FakeLoader() {
            this(null);
        }

        public FakeLoader(IOException exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

        @Override
        public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
            this.inputStream = inputStream;
            this.builder = builder;
            this.invokeCount += 1;

            if (exceptionToThrow != null)
                throw exceptionToThrow;
        }
    }
}
