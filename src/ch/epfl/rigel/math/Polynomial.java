package ch.epfl.rigel.math;

public final class Polynomial {

// polynomial is represented with exponents in decreasing order
    private final double[] polynomials;
    private Polynomial(double coefficientN, double... coefficients){
        this.polynomials = new double[coefficients.length + 1];
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
        if(coefficientN == 0){
            throw new IllegalArgumentException();
        }
        else {
            return new Polynomial(coefficientN, coefficients);
        }
    }

    /**
     *
     * @param x
     * @return evaluate polynomial for given value
     */
    public double at(double x){
        int n = polynomials.length - 1;
        double y = polynomials[n];
        for (int i = n - 1; i >= 0; i--) {
            y = polynomials[i] + (x * y);
        }
        return y;
    }

    /**
    public String toString(){
        return
    }


    /**
     *
     * @return string representation of polynomial ignoring any 0 coefficients
     */

/**
    public String toString(){
        if(polynomials.length > 1){
            String out = polynomials[polynomials.length - 1] + "x^" + (polynomials.length - 1);
            for(int i = polynomials.length - 2; i>=2; i--){
                if(polynomials[i] != 0){
                    if(polynomials[i] > 0){
                        out = "+" + out + polynomials[i] + "x^" + (i-1);
                    }
                    else{
                        out = "-" + out + polynomials[i] + "x^" + (i-1);
                    }
                }
            }
            if(polynomials[1] > 0){
                out = "+" + out + polynomials[1] + "x";
            }
            else{
                out = "-" + out + polynomials[1] + "x";
            }
            if(polynomials[0] > 0){
                out = "+" + out + polynomials[0] ;
            }
            else{
                out = "-" + out + polynomials[0];
            }
            return out;

        }
        else{
            String out = Double.toString(polynomials[0]);
            return out;
        }

    }
**/
    /**
    public String toString(){
        // returns String rep ignoring any 0 coefficients
        if(polynomials.length > 1){
            String output = polynomials[0] + "x^" + (polynomials.length-1) ;
            for(int i = polynomials.length - 2; i>1; i--){
                if(polynomials[i] != 0){
                    output = "+" + output + polynomials[i] + "x^" + (i-1) + "+";
                }
            }
            output = output + " " + polynomials[polynomials.length -1];
            return output;
        }
        else{
            return Double.toString(polynomials[0]);
        }

    }
     **/

    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }



}

