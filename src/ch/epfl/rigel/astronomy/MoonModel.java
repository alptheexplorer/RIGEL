package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    @Override
    /**
     * @return Moon object
     */
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {   //neccesary constants
        double longMoy = Angle.ofDeg(91.929336); // l0
        double longMoyPer = Angle.ofDeg(130.143076); //P0
        double longNAs = Angle.ofDeg(291.682547); //N0
        double incOrb = Angle.ofDeg(5.145396); //i
        double exenOrb = 0.0549;//e

        double sunMeanAnom = ((Angle.TAU / 365.242191)) * daysSinceJ2010 + Angle.ofDeg(279.557208) - Angle.ofDeg(283.112438);//ES
        double sunLongEcl = Angle.normalizePositive(sunMeanAnom + 2 * 0.016705 * Math.sin(sunMeanAnom) + Angle.ofDeg(283.112438));//LAMBDAS
        double orbLongAv = Angle.ofDeg(13.1763966) * daysSinceJ2010 + longMoy;//L
        double anomAv = orbLongAv - (Angle.ofDeg(0.1114041) * daysSinceJ2010) - longMoyPer;//M
        double evec = Angle.ofDeg(1.2739) * Math.sin(2 * (orbLongAv - sunLongEcl) - anomAv);//EV
        double eqAnCorrec = Angle.ofDeg(0.1858) * Math.sin(sunMeanAnom);//AE
        double cor3 = Angle.ofDeg(0.37) * Math.sin(sunMeanAnom);//A3
        double anomCor = anomAv + evec - eqAnCorrec - cor3;//Mp
        double corCent = Angle.ofDeg(6.2886) * Math.sin(anomCor);//EC
        double cor4 = Angle.ofDeg(0.214) * Math.sin(2 * anomCor);//A4
        double orbLongCor = orbLongAv + evec + corCent - eqAnCorrec + cor4;//Lp
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (orbLongCor - sunLongEcl)); //V
        double orbLongCorV = orbLongCor + variation; //Lpp

        double longMoyN = longNAs - (Angle.ofDeg(0.0529539) * daysSinceJ2010); //N
        double longCorN = longMoyN - (Angle.ofDeg(0.16) * Math.sin(sunMeanAnom)); //Np
        double lambda = Angle.normalizePositive(Math.atan2(Math.sin(orbLongCorV - longCorN) * Math.cos(incOrb), Math.cos(orbLongCorV - longCorN)) + longCorN);
        double beta = Math.asin(Math.sin(orbLongCorV - longCorN) * Math.sin(incOrb));
        double phase = (1 + Math.cos(orbLongCorV - sunLongEcl)) / 2;
        double rho = (1 - (exenOrb * exenOrb)) / (1 + (exenOrb * Math.cos(anomCor + corCent)));
        double ang = Angle.ofDeg(0.5181) / rho;

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Moon(equatorialCoordinates, (float) ang, (float) 0, (float) phase);
    }
}


