package Models;

import smile.interpolation.*;

public class Temp {

    static double[] x1 = new double[5];
    static double[] x2 = new double[5];
    static double[][] y = new double[4][4];

    static BicubicInterpolation bicubicInterpolation = new BicubicInterpolation(x1,x2,y);

    public static void main(String[] args) {
        System.out.println(bicubicInterpolation.interpolate(0,0));
    }
}
