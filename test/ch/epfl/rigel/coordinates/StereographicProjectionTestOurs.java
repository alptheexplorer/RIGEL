package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StereographicProjectionTestOurs {



    @Test
    void circleCenterWorks(){
        HorizontalCoordinates centerObject = HorizontalCoordinates.of((Math.PI/3),(Math.PI/2));
        HorizontalCoordinates toTransformObject = HorizontalCoordinates.of((Math.PI/12), (Math.PI/8));
        StereographicProjection transformer = new StereographicProjection(centerObject);
        CartesianCoordinates transformedObject = transformer.circleCenterForParallel(toTransformObject);
        //System.out.println(transformer.COS_PHI_CENTER);
        System.out.println(Math.cos(Math.PI/2));
        assertEquals(0,transformedObject.y(),1e-7);
        assertEquals(0.668178, transformer.circleRadiusForParallel(toTransformObject));
    }

    @Test
    void inverseWorks(){
        HorizontalCoordinates centerObject = HorizontalCoordinates.of((Math.PI/3),(Math.PI/2));
        StereographicProjection transformer = new StereographicProjection(centerObject);
        CartesianCoordinates toTransformObject = CartesianCoordinates.of(2,3);
        double x = transformer.inverseApply(toTransformObject).lon();
        assertEquals(0.459194,x);
    }

    @Test
    void stringWorks(){
        HorizontalCoordinates centerObject = HorizontalCoordinates.of((Math.PI/3),(Math.PI/2));
        StereographicProjection transformer = new StereographicProjection(centerObject);
        CartesianCoordinates toTransformObject = CartesianCoordinates.of(2,3);
        double x = transformer.inverseApply(toTransformObject).lon();
        System.out.println(transformer.toString());
    }

}