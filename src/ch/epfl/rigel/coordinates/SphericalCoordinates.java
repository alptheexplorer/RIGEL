package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.toDeg;

/**
 * @author Alp Ozen
 */
abstract class SphericalCoordinates {
     private double longitude, latitude;

     SphericalCoordinates(double l, double la){
         this.longitude = l;
         this.latitude = la;
     }

     double lon(){
         return this.longitude;
     }

     double lonDeg(){
         return toDeg(this.longitude);
     }

     double lat(){
         return this.latitude;
     }

     double latDeg(){
         return toDeg(this.latitude);
     }

    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }




}
