package Core;

public class Interpolator {
    private static MatrixXL[][] courseGrid;

    public static void designCourse(int a, int b) {
        MatrixXL aCoefficients = new MatrixXL();

        double[][] LeftFactor = aCoefficients.getLeftFactor();
        double[][] RightFactor = aCoefficients.getRightFactor();

        double[][] tmpResultMx = new double [4][4];
        double[][] finalResultMx = new double [4][4];

        courseGrid = new MatrixXL[5][5];
        double[][] pointMatrix = aCoefficients.getPointMatrix();

        aCoefficients.set(0,0, pointMatrix[a][b]);
        aCoefficients.set(0,1, pointMatrix[a][b+1]);
        aCoefficients.set(1,0, pointMatrix[a+1][b]);
        aCoefficients.set(1,1, pointMatrix[a+1][b+1]);

        fillX(aCoefficients, pointMatrix, a, b);
        fillY(aCoefficients, pointMatrix, a, b);
        fillXY(aCoefficients, pointMatrix, a, b);

        zeros(tmpResultMx);
        zeros(finalResultMx);

        multiply(aCoefficients.getSetStuff(),RightFactor,tmpResultMx);
        multiply(LeftFactor,tmpResultMx,finalResultMx);
        setCoefficient(finalResultMx, aCoefficients);
        courseGrid[a][b]= aCoefficients;

    }

    private static void multiply(double[][] A, double[][] B, double[][] C) {
        int i,j,k;
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                for (k=0;k<4;k++) {
                    C[i][j] = C[i][j] + (A[i][k]*B[k][j]);
                }
            }
        }
    }

    private static void fillX(MatrixXL mx, double[][] pointMx, int i, int j){
        if (i==0) {
            mx.set(2, 0, (pointMx[i + 1][j] - pointMx[i][j]));
            mx.set(2, 1, (pointMx[i + 2][j] - pointMx[i][j]));
            mx.set(3, 0, (pointMx[i + 1][j+1] - pointMx[i][j+1]));
            mx.set(3, 1, (pointMx[i + 2][j+1] - pointMx[i][j+1]));
        }
        else if (i==4) {
            mx.set(2, 0, (pointMx[i + 1][j] - pointMx[i-1][j]));
            mx.set(2, 1, (pointMx[i + 1][j] - pointMx[i][j]));
            mx.set(3, 0, (pointMx[i + 1][j+1] - pointMx[i-1][j+1]));
            mx.set(3, 1, (pointMx[i + 1][j+1] - pointMx[i][j+1]));
        }
        else {
            mx.set(2, 0, (pointMx[i + 1][j] - pointMx[i-1][j]));
            mx.set(2, 1, (pointMx[i + 2][j] - pointMx[i][j]));
            mx.set(3, 0, (pointMx[i + 1][j+1] - pointMx[i-1][j+1]));
            mx.set(3, 1, (pointMx[i + 2][j+1] - pointMx[i][j+1]));
        }
    }

    private static void fillY(MatrixXL mx, double[][] pointMx, int i, int j){
        if (j==0) {
            mx.set(0, 2, (pointMx[i][j+1] - pointMx[i][j]));
            mx.set(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j]));
            mx.set(1, 2, (pointMx[i][j+2] - pointMx[i][j]));
            mx.set(1, 3, (pointMx[i+1][j+2] - pointMx[i+1][j]));
        }
        else if (j==4) {
            mx.set(0, 2, (pointMx[i][j+1] - pointMx[i][j-1]));
            mx.set(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j-1]));
            mx.set(1, 2, (pointMx[i][j+1] - pointMx[i][j]));
            mx.set(1, 3, (pointMx[i+1][j+1] - pointMx[i+1][j]));
        }
        else {
            mx.set(0, 2, (pointMx[i][j+1] - pointMx[i][j-1]));
            mx.set(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j-1]));
            mx.set(1, 2, (pointMx[i][j+2] - pointMx[i][j]));
            mx.set(1, 3, (pointMx[i+1][j+2] - pointMx[i+1][j]));
        }
    }

    private static void fillXY(MatrixXL mx, double[][] pointMx, int i, int j) {
        if (i==0) {
            if(j==0) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.set(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else {  //j=4
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j] - pointMx[i+1][j-1])-(pointMx[i][j] - pointMx[i][j-1])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        }

        else if((i>0)&&(i<4)) {
            if(j==0) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.set(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else {  //j=4
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        }
        else {      //i=5
            if(j==0) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.set(3, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else if((j>0)&&(j<4)) {
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
            }
            else {  //j=4
                mx.set(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.set(2, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.set(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.set(3, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        }
}

    private static void setCoefficient(double[][] coefficient, MatrixXL ret) {
        int i,j;
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                ret.set(i,j,coefficient[i][j]);
            }
        }
    }

    private static void zeros(double[][] mx) {
        int n=mx.length;
        int m=mx[0].length;
        for (int i=0;i<n;i++) {
            for(int j=0;j<m;j++) {
                mx[i][j]=0;
            }
        }
    }

    public static MatrixXL[][] getCoefficients() {
        return courseGrid;
    }
}