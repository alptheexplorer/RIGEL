package ch.epfl.rigel.math;

public final class Polynomial {

    // polynomial is represented with exponents in decreasing order
    private final double[] polynomials;
    private Polynomial(double coefficientN, double[] coefficients){
        this.polynomials = new double[coefficients.length + 1];
        this.polynomials[0] = coefficientN;
        for(int i = 1; i<=coefficients.length; i++){
            this.polynomials[i] = coefficients[i-1];
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
            //copy the values of the ellipse in  proper array
            double[] coeff = new double[coefficients.length];
            for(int i = 0; i<coefficients.length; i++){
                coeff[i] = coefficients[i];
            }
            return new Polynomial(coefficientN, coeff);
        }
    }

    /**
     *
     * @param x
     * @return evaluate polynomial for given value
     */
    /**   public double at(double x){
     int n = polynomials.length - 1;
     double y = polynomials[n];
     for (int i = n - 1; i >= 0; i--) {
     y = polynomials[i] + (x * y);
     }
     return y;
     }
     **/
    /**
     *
     * @param x
     * @return evaluate polynomial for given value
     */
    public double at(double x){
        int n = polynomials.length ;
        //initialize result: we are sure that there is at least one element in polynomials so n=0 as a minimum
        double result = polynomials[0];

        for (int i = 1; i<n; i++) {
            result = polynomials[i] + x*result;
        }
        return result;
    }
    /**  public String toString(){
     }
     **/
    /**
     *
     * @return string representation of polynomial ignoring any 0 coefficients
     */


/*    public String toString(){
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
*/
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

/*
    public String toString(){
        String out = new String() ;
        StringBuilder builder = new StringBuilder( out);
        int exp = polynomials.length-1;
        for(double coef : polynomials ){
            if(coef==polynomials[0] && exp>1) {
                if( coef ==1)  builder.append( "x^"+ exp);
                else if (coef == -1) builder.append( "-x^"+ exp);
                else builder.append(coef +"x^"+ exp);
            }
            else if( coef > 0 && coef !=1 &&  coef!=polynomials[0]){
                if(exp!= 1 && exp!=0 && coef!=1 && coef!=-1) builder.append("+" +coef +"x^"+ exp);
                else if( exp == 1 && coef!=1 && coef!=-1)builder.append("+"+coef +"x");
                else if(exp == 0 ) builder.append("+"+coef);
                else if ( coef ==1)  builder.append( "x^"+ exp);
                else if (coef == -1) builder.append( "-x^"+ exp);
            }
            else if( coef < 0 && coef != 1 && coef!=polynomials[0]){
                if(exp!= 1 && exp!=0  ) builder.append(coef +"x^"+ exp);
                else if( exp == 1 && coef!=1 && coef!=-1) builder.append(coef +"x");
                else if(exp == 0 ) builder.append(coef);
                else if ( coef ==1)  builder.append( "x^"+ exp);
                else if (coef == -1) builder.append( "-x^"+ exp);
            }
            else if(exp ==1) builder.append(coef + "x");
            else builder.append(coef);
             exp -= 1;
        }
        out = builder.toString();
        return out;
    }
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
                else if(coef==0){
                    //niente
                }
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
