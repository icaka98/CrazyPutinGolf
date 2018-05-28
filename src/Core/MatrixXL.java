package Core;

public class MatrixXL {

    private double[][] leftFactor;
    private double[][] rightFactor;

    private double[][] setStuff;
    private double[][] pointMatrix = new double[6][6];

    public MatrixXL(){
        setStuff = new double[4][4];
        this.leftFactor = new double[][]{
                { 1, 0, 0, 0 },
                { 0, 0, 1, 0 },
                { -3, 3, -2, -1 },
                { 2, -2, 1, 1 },
        };
        this.rightFactor = new double[][]{
                { 1, 0, -3, 2 },
                { 0, 0, 3, -2 },
                { 0, 1, -2, 1 },
                { 0, 0, -1, 1 },
        };

        newPointMatrix();
    }

    public void newPointMatrix() {
        
        pointMatrix[0][0]=-1;
        pointMatrix[0][1]=-1;
        pointMatrix[0][2]=-1;
        pointMatrix[0][3]=1;
        pointMatrix[0][4]=1;
        pointMatrix[0][5]=0;
        pointMatrix[1][0]=-1;
        pointMatrix[1][1]=-1;
        pointMatrix[1][2]=2;
        pointMatrix[1][3]=3;
        pointMatrix[1][4]=1;
        pointMatrix[1][5]=0;
        pointMatrix[2][0]=-1;
        pointMatrix[2][1]=1;
        pointMatrix[2][2]=1;
        pointMatrix[2][3]=2;
        pointMatrix[2][4]=1;
        pointMatrix[2][5]=2;
        pointMatrix[3][0]=1;
        pointMatrix[3][1]=2;
        pointMatrix[3][2]=1;
        pointMatrix[3][3]=0;
        pointMatrix[3][4]=1;
        pointMatrix[3][5]=2;
        pointMatrix[4][0]=1;
        pointMatrix[4][1]=2;
        pointMatrix[4][2]=3;
        pointMatrix[4][3]=4;
        pointMatrix[4][4]=1;
        pointMatrix[4][5]=4;
        pointMatrix[5][0]=1;
        pointMatrix[5][1]=1;
        pointMatrix[5][2]=0;
        pointMatrix[5][3]=2;
        pointMatrix[5][4]=3;
        pointMatrix[5][5]=4;
    }

    public double[][] getLeftFactor() {
        return leftFactor;
    }

    public double[][] getRightFactor() {
        return rightFactor;
    }

    public double[][] getPointMatrix() {
        return pointMatrix;
    }

    public void set(int x, int y, double z){
        setStuff[x][y] = z;
    }

    public double[][] getSetStuff(){
        return setStuff;
    }

    public double getCoefficient (int i, int j) { return this.setStuff [i][j]; }
}
