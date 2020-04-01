package ch.epfl.rigel.astronomy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum HygDataBaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        InputStream buffer = new BufferedInputStream(inputStream);

    }
}
