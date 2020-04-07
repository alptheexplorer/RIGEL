package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class HygDatabaseLoaderTest {
    private static final String HEADER_LINE = "id,hip,hd,hr,gl,bf,proper,ra,dec,dist,pmra,pmdec,rv,mag,absmag,spect,ci,x,y,z,vx,vy,vz,rarad,decrad,pmrarad,pmdecrad,bayer,flam,con,comp,comp_primary,base,lum,var,var_min,var_max";
    private static final String RIGEL_LINE = "24378,24436,34085,1713,,19Bet Ori,Rigel,5.242298,-8.201640,264.5503,1.87,-0.56,21.0,0.180,-6.933,B8Ia,-0.030,51.601106,256.709905,-37.740051,0.00000182,0.00002121,-0.00000377,1.3724303693276385,-0.143145630755865,0.00000000906601582638889,-0.000000002714956,Bet,19,Ori,1,24378,,51665.42425669497,,,";
    private static final String CONSTELLATION_AND_BAYER_LINE = "122,122,224889,9084,,The Oct,,0.026616,-77.065724,66.5779,-56.52,-176.95,24.0,4.780,0.663,K2III,1.254,14.902031,0.103716,-64.888859,-0.00005004,-0.00001859,-0.00003671,0.0069680833539428125,-1.3450506304308112,-0.00000027401669224999995,-0.000000857877807,The,,Oct,1,122,,47.293341491865164,,,";
    private static final String CONSTELLATION_NO_BAYER_LINE = "107,107,224865,9082,,,,0.022255,-50.337373,166.3894,7.34,10.59,2.0,5.530,-0.576,M2III,1.615,106.198875,0.618768,-128.089244,0.00000785,0.00000597,0.00000388,0.005826258371586336,-0.87855289975953,0.00000003558532415277778,0.000000051341768,,,Phe,1,107,,148.04713248976265,,,";
    private static final String NO_HIP_LINE = "118164,,,,Gl 105.4B,,,2.659463,-11.871068,27.0343,141.88,-229.72,16.8,5.600,3.440,F5 V,,20.299230,16.967967,-5.561404,-0.00000377,0.00002108,-0.00003300,0.6962457546824986,-0.2071892152145,0.0000006878536499722223,-0.00000111370429,,,Cet,2,12357,Gl 105.4,3.664375746478332,,,";
    private static final String NO_CI_LINE = "32523,32609,48766,2486,,,,6.803407,55.704195,44.2674,57.71,-104.75,9.0,5.540,2.310,F5V:+...,,-5.207790,24.393487,36.571117,-0.00001707,0.00002065,-0.00000506,1.7811278431272817,0.9722216083796125,0.0000002797859750486111,-0.00000050784233,,,Lyn,1,32523,,10.37528415818012,,,";

    private static InputStream streamWithAsciiLines(String... asciiStrings) {
        var joined = String.join("\n", asciiStrings);
        return new ByteArrayInputStream(joined.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    void headerLineIsIgnored() throws IOException {
        try (var stream = streamWithAsciiLines(HEADER_LINE)) {
            var b = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(stream, b);
            var c = b.build();
            assertTrue(c.stars().isEmpty());
            assertTrue(c.asterisms().isEmpty());
        }
    }

    @Test
    void rigelIsCorrectlyLoaded() throws IOException {
        try (var stream = streamWithAsciiLines(HEADER_LINE, RIGEL_LINE)) {
            var b = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(stream, b);
            var c = b.build();

            assertEquals(1, c.stars().size());
            assertTrue(c.asterisms().isEmpty());

            var rigel = c.stars().get(0);
            assertEquals("Rigel", rigel.name());
            assertEquals(1.3724303693276385, rigel.equatorialPos().ra(), 1e-12);
            assertEquals(-0.143145630755865, rigel.equatorialPos().dec(), 1e-12);
            assertEquals(0.180, rigel.magnitude(), 0.5e-3);
            assertEquals(24436, rigel.hipparcosId());
            assertEquals(10515, rigel.colorTemperature());
        }
    }

    @Test
    void hipparcosNumberIs0ByDefault() throws IOException {
        try (var stream = streamWithAsciiLines(HEADER_LINE, NO_HIP_LINE)) {
            var b = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(stream, b);
            assertEquals(0, b.build().stars().get(0).hipparcosId());
        }
    }

    @Test
    void colorIndexIs0ByDefault() throws IOException {
        try (var stream = streamWithAsciiLines(HEADER_LINE, NO_CI_LINE)) {
            var b = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(stream, b);
            assertEquals(10125, b.build().stars().get(0).colorTemperature());
        }
    }

    @Test
    void nameIsBayerAndConstellation() throws IOException {
        try (var stream = streamWithAsciiLines(HEADER_LINE, CONSTELLATION_NO_BAYER_LINE, CONSTELLATION_AND_BAYER_LINE)) {
            var b = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(stream, b);
            var stars = new ArrayList<>(b.build().stars());
            stars.sort(Comparator.comparingInt(Star::hipparcosId));
            assertEquals("? Phe", stars.get(0).name());
            assertEquals("The Oct", stars.get(1).name());
        }
    }

    @Test
    void hygLoaderCorrectlyHandlesIOExceptions() {
        IOException fakeException = new IOException("fake IO exception");
        try {
            HygDatabaseLoader.INSTANCE.load(new ThrowingInputStream(fakeException), new StarCatalogue.Builder());
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