package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Polynomial, exponents in decreasing order
 * @author Alp Ozen (314542)
 * @author Jacopo Ferro (299301)
 */
public final class Polynomial {

    //polynomial is represented with exponents in decreasing order
    private final double[] polynomials;

    private Polynomial(double coefficientN, double[] coefficients){
        this.polynomials = new double[coefficients.length + 1];
        this.polynomials[0] = coefficientN;
            for(int i = 1; i<=coefficients.length; i++){
                this.polynomials[i] = coefficients[i-1];
            }
    }

    /**
     * @param coefficientN
     * @param coefficients
     * @return a new polynomial with exponents in decreasing order
     * @throws IllegalArgumentException if first coeff == 0
     */
    public static Polynomial of(double coefficientN, double... coefficients){
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     *
     * @param x
     * @return evaluate polynomial at given value
     */
    public double at(double x){
     int n = polynomials.length ;
     double result = polynomials[0];

     for (int i = 1; i<n; i++) {
             result = polynomials[i] + x*result;
         }
         return result;
     }

    /**
     *
     * @return string representation of polynomial without superfluous coefficients or exponents
     */
    public String toString(){
        String out = new String() ;
        StringBuilder builder = new StringBuilder( out);
        int exp = polynomials.length-1;
        for(double coef : polynomials ){
            if(polynomials.length ==1 )  builder.append(coef);
            else if (polynomials.length ==2){
                if(exp==1) builder.append(coef + "x");
                else if(coef >0) builder.append("+"+ coef);
                else if( coef<0) builder.append(coef);
            }
            else {
                if(coef>0){
                    if (coef!=1){
                        if (exp == polynomials.length - 1) builder.append(coef + "x^" + exp);
                        else if (exp == 1) builder.append("+" + coef + "x");
                        else if (exp == 0) builder.append("+"+coef);
                        else builder.append("+" + coef + "x^" + exp);
                    }
                    else{
                        if (exp == polynomials.length - 1) builder.append("x^" + exp);
                        else if (exp == 1) builder.append("+x");
                        else if (exp == 0) builder.append(coef);
                        else builder.append("+x^" + exp);
                    }
                }
                else if(coef<0){
                    if(coef!=-1){
                        if(exp==polynomials.length-1 && exp!= 1) builder.append(coef + "x^"+exp);
                        else if (exp==1) builder.append(coef + "x");
                        else if(exp==0) builder.append(coef);
                        else builder.append( coef + "x^"+ exp);
                    }
                    else{
                        if(exp==polynomials.length-1 && exp!= 1) builder.append("-x^"+exp);
                        else if (exp==1) builder.append("-x");
                        else if(exp==0) builder.append(coef);
                        else builder.append("-x^"+ exp);
                    }
                }
                else if(coef==0){}//nothing
            }
            exp -= 1;
        }
        out = builder.toString();
        return out;
    }


    @Override
    public int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }



}

