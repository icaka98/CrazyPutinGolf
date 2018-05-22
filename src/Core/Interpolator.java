package Core;

public class Interpolator {
    private double[][] course = new double[6][6];
    private static MatrixXL[][] coefficientMatrixXL;

    public static MatrixXL[][] designCourse(int a, int b) {
        MatrixXL courseXL = new MatrixXL();

        double[][] LeftFactor = courseXL.getLeftFactor();
        double[][] RightFactor = courseXL.getRightFactor();

        double[][] DMatrix = new double [4][4];
        double[][] EMatrix = new double [4][4];

        coefficientMatrixXL = new MatrixXL[5][5];
        double[][] course = courseXL.getPointMatrix();

        courseXL.setValue(0,0, course[a][b]);
        courseXL.setValue(0,1, course[a][b+1]);
        courseXL.setValue(1,0, course[a+1][b]);
        courseXL.setValue(1,1, course[a+1][b+1]);

        fillX(courseXL, course, a, b);
        fillY(courseXL, course, a, b);
        fillXY(courseXL, course, a, b);
        zeros(DMatrix);
        zeros(EMatrix);

        multiply(courseXL.getB(),RightFactor,DMatrix);
        multiply(LeftFactor,DMatrix,EMatrix);
        setCoefficient(EMatrix, courseXL);
        coefficientMatrixXL[a][b]= courseXL;
        
        return finalize();
    }

    public static void multiply (double[][] A, double[][] B, double[][] C) {
        int i,j,k;
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                for (k=0;k<4;k++) {
                    C[i][j] = C[i][j] + (A[i][k]*B[k][j]);
                }
            }
        }
    }

    public static void fillX (MatrixXL mx, double[][] function, int i, int j){
        if (i==0) {
            mx.setValue(2, 0, (function[i + 1][j] - function[i][j]));
            mx.setValue(2, 1, (function[i + 2][j] - function[i][j]));
            mx.setValue(3, 0, (function[i + 1][j+1] - function[i][j+1]));
            mx.setValue(3, 1, (function[i + 2][j+1] - function[i][j+1]));
        }
        else if (i==4) {
            mx.setValue(2, 0, (function[i + 1][j] - function[i-1][j]));
            mx.setValue(2, 1, (function[i + 1][j] - function[i][j]));
            mx.setValue(3, 0, (function[i + 1][j+1] - function[i-1][j+1]));
            mx.setValue(3, 1, (function[i + 1][j+1] - function[i][j+1]));
        }
        else {
            mx.setValue(2, 0, (function[i + 1][j] - function[i-1][j]));
            mx.setValue(2, 1, (function[i + 2][j] - function[i][j]));
            mx.setValue(3, 0, (function[i + 1][j+1] - function[i-1][j+1]));
            mx.setValue(3, 1, (function[i + 2][j+1] - function[i][j+1]));
        }
    }

    public static void fillY (MatrixXL mx, double[][] function, int i, int j){
        if (j==0) {
            mx.setValue(0, 2, (function[i][j+1] - function[i][j]));
            mx.setValue(0, 3, (function[i+1][j+1] - function[i+1][j]));
            mx.setValue(1, 2, (function[i][j+2] - function[i][j]));
            mx.setValue(1, 3, (function[i+1][j+2] - function[i+1][j]));
        }
        else if (j==4) {
            mx.setValue(0, 2, (function[i][j+1] - function[i][j-1]));
            mx.setValue(0, 3, (function[i+1][j+1] - function[i+1][j-1]));
            mx.setValue(1, 2, (function[i][j+1] - function[i][j]));
            mx.setValue(1, 3, (function[i+1][j+1] - function[i+1][j]));
        }
        else {
            mx.setValue(0, 2, (function[i][j+1] - function[i][j-1]));
            mx.setValue(0, 3, (function[i+1][j+1] - function[i+1][j-1]));
            mx.setValue(1, 2, (function[i][j+2] - function[i][j]));
            mx.setValue(1, 3, (function[i+1][j+2] - function[i+1][j]));
        }
    }

    public static void fillXY (MatrixXL mx, double[][] function, int i, int j) {
        if (i==0) {
            if(j==0) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j])-(function[i][j+1] - function[i][j])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i][j+2] - function[i][j])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j])-(function[i][j+1] - function[i][j])));
                mx.setValue(3, 3, ((function[i+2][j+2] - function[i+2][j])-(function[i][j+2] - function[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i][j+2] - function[i][j])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+2][j+2] - function[i+2][j])-(function[i][j+2] - function[i][j])));
            }
            else {  //j=4
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(2, 3, ((function[i+1][j] - function[i+1][j-1])-(function[i][j] - function[i][j-1])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+2][j+1] - function[i+2][j])-(function[i][j+1] - function[i][j])));
            }
        }

        else if((i>0)&&(i<4)) {
            if(j==0) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j])-(function[i-1][j+1] - function[i-1][j])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i-1][j+2] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j])-(function[i][j+1] - function[i][j])));
                mx.setValue(3, 3, ((function[i+2][j+2] - function[i+2][j])-(function[i][j+2] - function[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i-1][j+1] - function[i-1][j-1])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i-1][j+2] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+2][j+2] - function[i+2][j])-(function[i][j+2] - function[i][j])));
            }
            else {  //j=4
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i-1][j+1] - function[i-1][j-1])));
                mx.setValue(2, 3, ((function[i+1][j+1] - function[i+1][j])-(function[i-1][j+1] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+2][j+1] - function[i+2][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+2][j+1] - function[i+2][j])-(function[i][j+1] - function[i][j])));
            }
        }
        else {      //i=5
            if(j==0) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j])-(function[i-1][j+1] - function[i-1][j])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i-1][j+2] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+1][j+1] - function[i+1][j])-(function[i][j+1] - function[i][j])));
                mx.setValue(3, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i][j+2] - function[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i-1][j+1] - function[i-1][j-1])));
                mx.setValue(2, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i-1][j+2] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+1][j+2] - function[i+1][j])-(function[i][j+2] - function[i][j])));
            }
            else {  //j=4
                mx.setValue(2, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i-1][j+1] - function[i-1][j-1])));
                mx.setValue(2, 3, ((function[i+1][j+1] - function[i+1][j])-(function[i-1][j+1] - function[i-1][j])));
                mx.setValue(3, 2, ((function[i+1][j+1] - function[i+1][j-1])-(function[i][j+1] - function[i][j-1])));
                mx.setValue(3, 3, ((function[i+1][j+1] - function[i+1][j])-(function[i][j+1] - function[i][j])));
            }
        }
}

    public static void setCoefficient(double[][] coefficient, MatrixXL ret) {
        int i,j;
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                ret.setValue(i,j,coefficient[i][j]);
            }
        }
    }

    public static void zeros(double[][] mx) {
        int n=mx.length;
        int m=mx[0].length;
        for (int i=0;i<n;i++) {
            for(int j=0;j<m;j++) {
                mx[i][j]=0;
            }
        }
    }


    public static MatrixXL[][] finalize() {
        return coefficientMatrixXL;
    }
}