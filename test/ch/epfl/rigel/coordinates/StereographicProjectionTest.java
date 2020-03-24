package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StereographicProjectionTest {

    @Test
    void circleForEquatorIsALineWhenCenterIsOnEquatorToo() {
        HorizontalCoordinates h00 = HorizontalCoordinates.of(0, 0);
        var p = new StereographicProjection(h00);
        var c = p.circleCenterForParallel(h00);
        var r = p.circleRadiusForParallel(h00);
        assertEquals(0, c.x());
        assertTrue(Double.isInfinite(c.y()));
        assertTrue(Double.isInfinite(r));
    }

    @Test
    void circlesForParallelsAreConcentricWhenCenterIsOnPole() {
        HorizontalCoordinates h = HorizontalCoordinates.ofDeg(0, 90);
        var p = new StereographicProjection(h);
        var prevRadius = -0d;
        for (int i = 0; i < 10; i++) {
            HorizontalCoordinates parallel = HorizontalCoordinates.ofDeg(0, 90d - i);
            var c = p.circleCenterForParallel(parallel);
            var r = p.circleRadiusForParallel(parallel);
            assertEquals(0, c.x());
            assertEquals(0, c.y(), 1e-8);
            assertTrue(prevRadius < r);
            prevRadius = r;
        }
    }

    @Test
    void circleCenterWorksOnKnownValues() {
        var c = HorizontalCoordinates.ofDeg(218, 47);
        var p = new StereographicProjection(c);

        var h1 = HorizontalCoordinates.ofDeg(0, 77.35);
        var h2 = HorizontalCoordinates.ofDeg(0, -59.92);
        var h3 = HorizontalCoordinates.ofDeg(0, 48.16);
        var h4 = HorizontalCoordinates.ofDeg(0, 67.60);
        var h5 = HorizontalCoordinates.ofDeg(0, 60.92);

        assertEquals(0.3995117214732776, p.circleCenterForParallel(h1).y(), 1e-8);
        assertEquals(-5.09057610324184, p.circleCenterForParallel(h2).y(), 1e-8);
        assertEquals(0.4619445280874631, p.circleCenterForParallel(h3).y(), 1e-8);
        assertEquals(0.41185969509630954, p.circleCenterForParallel(h4).y(), 1e-8);
        assertEquals(0.42484284228350655, p.circleCenterForParallel(h5).y(), 1e-8);
    }

    @Test
    void circleRadiusWorksOnKnownValues() {
        var c = HorizontalCoordinates.ofDeg(218, 47);
        var p = new StereographicProjection(c);

        var h1 = HorizontalCoordinates.ofDeg(0, 77.35);
        var h2 = HorizontalCoordinates.ofDeg(0, -59.92);
        var h3 = HorizontalCoordinates.ofDeg(0, 48.16);
        var h4 = HorizontalCoordinates.ofDeg(0, 67.60);
        var h5 = HorizontalCoordinates.ofDeg(0, 60.92);

        assertEquals(0.1282862205640954, p.circleRadiusForParallel(h1), 1e-8);
        assertEquals(-3.7411249651227934, p.circleRadiusForParallel(h2), 1e-8);
        assertEquals(0.45182127263573607, p.circleRadiusForParallel(h3), 1e-8);
        assertEquals(0.23012889503059386, p.circleRadiusForParallel(h4), 1e-8);
        assertEquals(0.30276687752311254, p.circleRadiusForParallel(h5), 1e-8);
    }

    @Test
    void applyWorksOnKnownValues() {
        var c = HorizontalCoordinates.ofDeg(7, -8);
        var p = new StereographicProjection(c);

        var h1 = HorizontalCoordinates.ofDeg(194.776, -1.3687);
        var h2 = HorizontalCoordinates.ofDeg(323.862, 57.8918);
        var h3 = HorizontalCoordinates.ofDeg(27.340, 15.3776);
        var h4 = HorizontalCoordinates.ofDeg(167.208, -14.0298);
        var h5 = HorizontalCoordinates.ofDeg(91.850, -49.2648);

        assertEquals(-6.027159362737581, p.apply(h1).x(), 1e-8);
        assertEquals(-7.196643283588719, p.apply(h1).y(), 1e-8);
        assertEquals(-0.2870261650172454, p.apply(h2).x(), 1e-8);
        assertEquals(0.7050904403871205, p.apply(h2).y(), 1e-8);
        assertEquals(0.18034386905858862, p.apply(h3).x(), 1e-8);
        assertEquals(0.20901167654970165, p.apply(h3).y(), 1e-8);
        assertEquals(2.5315796074175525, p.apply(h4).x(), 1e-8);
        assertEquals(-2.829098199043636, p.apply(h4).y(), 1e-8);
        assertEquals(0.5586168229780969, p.apply(h5).x(), 1e-8);
        assertEquals(-0.6379295511304205, p.apply(h5).y(), 1e-8);
    }

    @Test
    void inverseApplyWorksOnKnownValues() {
        var c = HorizontalCoordinates.ofDeg(7, -8);
        var p = new StereographicProjection(c);

        var c1 = CartesianCoordinates.of(1.3157, 2.2248);
        var c2 = CartesianCoordinates.of(-0.7715, 2.4744);
        var c3 = CartesianCoordinates.of(-2.9761, -0.9206);
        var c4 = CartesianCoordinates.of(-0.3114, -2.2065);
        var c5 = CartesianCoordinates.of(-2.9714, 1.4337);

        assertEquals(2.7798306016641288, p.inverseApply(c1).az(), 1e-8);
        assertEquals(0.7431497822670021, p.inverseApply(c1).alt(), 1e-8);
        assertEquals(3.564595263665816, p.inverseApply(c2).az(), 1e-8);
        assertEquals(0.8302218440696163, p.inverseApply(c2).alt(), 1e-8);
        assertEquals(3.85447206842368, p.inverseApply(c3).az(), 1e-8);
        assertEquals(-0.057186083723119924, p.inverseApply(c3).alt(), 1e-8);
        assertEquals(3.4000601082999165, p.inverseApply(c4).az(), 1e-8);
        assertEquals(-0.6945319987205353, p.inverseApply(c4).alt(), 1e-8);
        assertEquals(3.828042199469258, p.inverseApply(c5).az(), 1e-8);
        assertEquals(0.3625637559048031, p.inverseApply(c5).alt(), 1e-8);
    }

    @Test
    void centerIsProjectedToOrigin() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var centerAzDeg = rng.nextDouble(0, 360);
            var centerAltDeg = rng.nextDouble(-89.9999, 90);
            var center = HorizontalCoordinates.ofDeg(centerAzDeg, centerAltDeg);
            var proj = new StereographicProjection(center);
            var projCenter = proj.apply(center);
            assertEquals(0, projCenter.x(), 1e-9);
            assertEquals(0, projCenter.y(), 1e-9);
        }
    }

    @Test
    void applyAndInverseApplyAreInverses() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var centerAzDeg = rng.nextDouble(0, 360);
            var centerAltDeg = rng.nextDouble(-89.9999, 90);
            var center = HorizontalCoordinates.ofDeg(centerAzDeg, centerAltDeg);
            var proj = new StereographicProjection(center);
            for (int j = 0; j < TestRandomizer.RANDOM_ITERATIONS; j++) {
                var azDeg = rng.nextDouble(0, 360);
                var altDeg = rng.nextDouble(-90, 90);
                var hor = HorizontalCoordinates.ofDeg(azDeg, altDeg);
                var car = proj.apply(hor);
                var hor2 = proj.inverseApply(car);
                assertEquals(hor.azDeg(), hor2.azDeg(), 1e-8);
                assertEquals(hor.altDeg(), hor2.altDeg(), 1e-8);
            }
        }
    }

    @Test
    void applyToAngleWorksForAnglesOnTheHorizon() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var centerAzDeg = rng.nextDouble(60, 300);
            var center = HorizontalCoordinates.ofDeg(centerAzDeg, 0);
            var angleDeg = rng.nextDouble(60);
            var proj = new StereographicProjection(center);
            var l = HorizontalCoordinates.ofDeg(centerAzDeg - angleDeg / 2d, 0);
            var r = HorizontalCoordinates.ofDeg(centerAzDeg + angleDeg / 2d, 0);
            var pL = proj.apply(l);
            var pR = proj.apply(r);
            var pAngle = proj.applyToAngle(Math.toRadians(angleDeg));
            assertEquals(pR.x() - pL.x(), pAngle, 1e-8);
        }
    }

    @Test
    void spEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = new StereographicProjection(HorizontalCoordinates.of(0, 0));
            c.equals(c);
        });
    }

    @Test
    void spHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new StereographicProjection(HorizontalCoordinates.of(0, 0)).hashCode();
        });
    }
}
