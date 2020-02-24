package ch.epfl.rigel.math;

import java.util.Locale;

public final class Polynomial {
    private final double[] polynomials;
    private Polynomial(double coefficientN, double... coefficients){
        this.polynomials = new double[coefficients.length];
        this.polynomials[polynomials.length-1] = coefficientN;
        for(double coefficient: coefficients){
            for(int i = 0; i<=polynomials.length - 2; i++){
                this.polynomials[i] = coefficient;
            }
        }

    }


    /**
     *
     * @param coefficientN
     * @param coefficients
     * @return a new polynomial with exponents in increasing order
     */
    public static Polynomial of(double coefficientN, double... coefficients){
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     *
     * @param x
     * @return evaluate polynomial for given value
     */
    public double at(double x){
        double result = 0;
        for (int i = polynomials.length - 1; i >= 0; i--) {
            result = polynomials[i] + (x * result);
        }
        return result;
    }

    /**
     *
     * @return string representation of polynomial ignoring any 0 coefficients
     */
    public String toString(){
        // returns String rep ignoring any 0 coefficients
        String output = "";
        for(int i =0; i< polynomials.length; i++){
            if(polynomials[i] != 0){
                output = output + " " + polynomials[i] + "x^" + i + " ";
            }
        }
        return output;
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    public int hashCode(){
        throw new UnsupportedOperationException();
    }


    /**
     *
     * @throws UnsupportedOperationException
     */
    public boolean equals(){
        throw new UnsupportedOperationException();
    }


}

