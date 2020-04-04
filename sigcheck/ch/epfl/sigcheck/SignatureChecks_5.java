package ch.epfl.sigcheck;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.List;

final class SignatureChecks_5 {
    void checkStar() {
        int i = 0;
        String s = null;
        EquatorialCoordinates e = null;
        float f = 0;
        Star t = new Star(i, s, e, f, f);
        i = t.hipparcosId();
        i = t.colorTemperature();
    }

    void checkAsterism() {
        List<Star> l = null;
        Asterism a = new Asterism(l);
        l = a.stars();
    }

    void checkCelestialObjectModel() {
        CelestialObjectModel<Sun> s = null;
        double d = 0;
        EclipticToEquatorialConversion e = null;
        Sun t = s.at(d, e);
    }

    void checkSunModel() {
        Enum<SunModel> m1 = SunModel.SUN;
        CelestialObjectModel<Sun> m2 = SunModel.SUN;
    }

    void checkPlanetModel() {
        List<PlanetModel> a = PlanetModel.ALL;
        Enum<PlanetModel> mercury1 = PlanetModel.MERCURY;
        CelestialObjectModel<Planet> mercury2 = PlanetModel.MERCURY;
        Enum<PlanetModel> venus1 = PlanetModel.VENUS;
        CelestialObjectModel<Planet> venus2 = PlanetModel.VENUS;
        Enum<PlanetModel> earth1 = PlanetModel.EARTH;
        CelestialObjectModel<Planet> earth2 = PlanetModel.EARTH;
        Enum<PlanetModel> mars1 = PlanetModel.MARS;
        CelestialObjectModel<Planet> mars2 = PlanetModel.MARS;
        Enum<PlanetModel> jupiter1 = PlanetModel.JUPITER;
        CelestialObjectModel<Planet> jupiter2 = PlanetModel.JUPITER;
        Enum<PlanetModel> saturn1 = PlanetModel.SATURN;
        CelestialObjectModel<Planet> saturn2 = PlanetModel.SATURN;
        Enum<PlanetModel> uranus1 = PlanetModel.URANUS;
        CelestialObjectModel<Planet> uranus2 = PlanetModel.URANUS;
        Enum<PlanetModel> neptune1 = PlanetModel.NEPTUNE;
        CelestialObjectModel<Planet> neptune2 = PlanetModel.NEPTUNE;
    }
}
