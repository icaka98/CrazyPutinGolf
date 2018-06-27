package Core.Physics;

/**
 * @author Silvia Fallone
 */
public class Interpolator {
    private static MatrixXL[][] courseGrid;
    private static double[][] LeftFactor = new double[][]{
            { 1, 0, 0, 0 },
            { 0, 0, 1, 0 },
            { -3, 3, -2, -1 },
            { 2, -2, 1, 1 },
    };
    private static double[][] RightFactor = new double[][]{
            { 1, 0, -3, 2 },
            { 0, 0, 3, -2 },
            { 0, 1, -2, 1 },
            { 0, 0, -1, 1 },
    };

    /**
     * Where the interpolation actually starts
     * @param a x axis coordinate to localize a point in a specific square
     * @param b y axis coordinate to localize a point in a specific square
     */
    public static void designCourse(int a, int b) {
        MatrixXL aCoefficients = new MatrixXL();

        double[][] resultMx;

        courseGrid = new MatrixXL[5][5];
        double[][] pointMatrix = aCoefficients.getPointMatrix();

        aCoefficients.setfMatrix(0,0, pointMatrix[a][b]);
        aCoefficients.setfMatrix(0,1, pointMatrix[a][b+1]);
        aCoefficients.setfMatrix(1,0, pointMatrix[a+1][b]);
        aCoefficients.setfMatrix(1,1, pointMatrix[a+1][b+1]);

        getDerivativeX(aCoefficients, pointMatrix, a, b);
        getDerivativeY(aCoefficients, pointMatrix, a, b);
        getDerivativeXY(aCoefficients, pointMatrix, a, b);

        resultMx = multiply(aCoefficients.getfMatrix(),RightFactor);
        resultMx = multiply(LeftFactor,resultMx);
        setCoefficient(resultMx, aCoefficients);
        courseGrid[a][b]= aCoefficients;

    }

    /**
     * Execute matrix multiplication in between two factors
     * @param factorOne first factor to be multiplied
     * @param factorTwo second factor to be multiplied
     * @return the result matrix of the multiplication
     */
    private static double[][] multiply(double[][] factorOne, double[][] factorTwo) {
        int i,j,k;
        double[][] result = new double[4][4];
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                for (k=0;k<4;k++) {
                    result[i][j] = result[i][j] + (factorOne[i][k]*factorTwo[k][j]);
                }
            }
        }
        return result;
    }

    /**
     *  Retrieves the partial derivatives with respect to x through finite differences
     * @param mx aCoefficient where the derivatives are stored
     * @param pointMx preset point matrix
     * @param i first coordinate of the point of which to retrieve the derivative
     * @param j second coordinate of the point of which to retrieve the derivative
     */
    private static void getDerivativeX(MatrixXL mx, double[][] pointMx, int i, int j){
        if (i==0) {
            mx.setfMatrix(2, 0, (pointMx[i + 1][j] - pointMx[i][j]));
            mx.setfMatrix(2, 1, (pointMx[i + 2][j] - pointMx[i][j]));
            mx.setfMatrix(3, 0, (pointMx[i + 1][j+1] - pointMx[i][j+1]));
            mx.setfMatrix(3, 1, (pointMx[i + 2][j+1] - pointMx[i][j+1]));
        } else if (i==4) {
            mx.setfMatrix(2, 0, (pointMx[i + 1][j] - pointMx[i-1][j]));
            mx.setfMatrix(2, 1, (pointMx[i + 1][j] - pointMx[i][j]));
            mx.setfMatrix(3, 0, (pointMx[i + 1][j+1] - pointMx[i-1][j+1]));
            mx.setfMatrix(3, 1, (pointMx[i + 1][j+1] - pointMx[i][j+1]));
        } else {
            mx.setfMatrix(2, 0, (pointMx[i + 1][j] - pointMx[i-1][j]));
            mx.setfMatrix(2, 1, (pointMx[i + 2][j] - pointMx[i][j]));
            mx.setfMatrix(3, 0, (pointMx[i + 1][j+1] - pointMx[i-1][j+1]));
            mx.setfMatrix(3, 1, (pointMx[i + 2][j+1] - pointMx[i][j+1]));
        }
    }

    /**
     *  Retrieves the partial derivatives with respect to y through finite differences
     * @param mx aCoefficient where the derivatives are stored
     * @param pointMx preset point matrix
     * @param i first coordinate of the point of which to retrieve the derivative
     * @param j second coordinate of the point of which to retrieve the derivative
     */
    private static void getDerivativeY(MatrixXL mx, double[][] pointMx, int i, int j){
        if (j==0) {
            mx.setfMatrix(0, 2, (pointMx[i][j+1] - pointMx[i][j]));
            mx.setfMatrix(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j]));
            mx.setfMatrix(1, 2, (pointMx[i][j+2] - pointMx[i][j]));
            mx.setfMatrix(1, 3, (pointMx[i+1][j+2] - pointMx[i+1][j]));
        } else if (j==4) {
            mx.setfMatrix(0, 2, (pointMx[i][j+1] - pointMx[i][j-1]));
            mx.setfMatrix(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j-1]));
            mx.setfMatrix(1, 2, (pointMx[i][j+1] - pointMx[i][j]));
            mx.setfMatrix(1, 3, (pointMx[i+1][j+1] - pointMx[i+1][j]));
        } else {
            mx.setfMatrix(0, 2, (pointMx[i][j+1] - pointMx[i][j-1]));
            mx.setfMatrix(0, 3, (pointMx[i+1][j+1] - pointMx[i+1][j-1]));
            mx.setfMatrix(1, 2, (pointMx[i][j+2] - pointMx[i][j]));
            mx.setfMatrix(1, 3, (pointMx[i+1][j+2] - pointMx[i+1][j]));
        }
    }
    /**
     * Retrieves the cross derivatives
     * @param mx aCoefficient where the derivatives are stored
     * @param pointMx preset point matrix
     * @param i first coordinate of the point of which to retrieve the derivative
     * @param j second coordinate of the point of which to retrieve the derivative
     */
    private static void getDerivativeXY(MatrixXL mx, double[][] pointMx, int i, int j) {
        if (i==0) {
            if(j==0) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else if((j>0)&&(j<4)) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j] - pointMx[i+1][j-1])-(pointMx[i][j] - pointMx[i][j-1])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        } else if((i>0)&&(i<4)) {
            if(j==0) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else if((j>0)&&(j<4)) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+2] - pointMx[i+2][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+2][j+1] - pointMx[i+2][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+2][j+1] - pointMx[i+2][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        } else {
            if(j==0) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
                mx.setfMatrix(3, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else if((j>0)&&(j<4)) {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i-1][j+2] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+1][j+2] - pointMx[i+1][j])-(pointMx[i][j+2] - pointMx[i][j])));
            } else {
                mx.setfMatrix(2, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i-1][j+1] - pointMx[i-1][j-1])));
                mx.setfMatrix(2, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i-1][j+1] - pointMx[i-1][j])));
                mx.setfMatrix(3, 2, ((pointMx[i+1][j+1] - pointMx[i+1][j-1])-(pointMx[i][j+1] - pointMx[i][j-1])));
                mx.setfMatrix(3, 3, ((pointMx[i+1][j+1] - pointMx[i+1][j])-(pointMx[i][j+1] - pointMx[i][j])));
            }
        }
    }

    /**
     * Stores the fMatrix in the related aCoefficient object
     * @param coefficient fMAtrix value
     * @param mx aCoefficient matrix
     */
    private static void setCoefficient(double[][] coefficient, MatrixXL mx) {
        int i,j;
        for (i=0;i<4;i++) {
            for (j=0;j<4;j++) {
                mx.setfMatrix(i,j,coefficient[i][j]);
            }
        }
    }

    /**
     * rerieves the courseGrid
     * @return course grid containing all the aCoefficient matrices
     */
    public static MatrixXL[][] getGrid() {
        return courseGrid;
    }
}