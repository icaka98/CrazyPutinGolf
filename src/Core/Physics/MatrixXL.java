package Core.Physics;

public class MatrixXL {
    private double[][] leftFactor, rightFactor;
    private double[][] setStuff;
    private double[][] pointMatrix;

    public MatrixXL(){
        this.pointMatrix = new double[6][6];
        this.setStuff = new double[4][4];

        this.initFactors();
        this.newPointMatrix();
    }

    private void initFactors() {
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
    }

    private void newPointMatrix() {
        this.pointMatrix[0][0]=0;
        this.pointMatrix[0][1]=1;
        this.pointMatrix[0][2]=1;
        this.pointMatrix[0][3]=1;
        this.pointMatrix[0][4]=1;
        this.pointMatrix[0][5]=1;
        this.pointMatrix[1][0]=0;
        this.pointMatrix[1][1]=0.05;
        this.pointMatrix[1][2]=0.1;
        this.pointMatrix[1][3]=0;
        this.pointMatrix[1][4]=0;
        this.pointMatrix[1][5]=0;
        this.pointMatrix[2][0]=0;
        this.pointMatrix[2][1]=1;
        this.pointMatrix[2][2]=0.05;
        this.pointMatrix[2][3]=0;
        this.pointMatrix[2][4]=0.1;
        this.pointMatrix[2][5]=0;
        this.pointMatrix[3][0]=0;
        this.pointMatrix[3][1]=0.05;
        this.pointMatrix[3][2]=0.15;
        this.pointMatrix[3][3]=0.2;
        this.pointMatrix[3][4]=0.1;
        this.pointMatrix[3][5]=0;
        this.pointMatrix[4][0]=0;
        this.pointMatrix[4][1]=1;
        this.pointMatrix[4][2]=0;
        this.pointMatrix[4][3]=0.05;
        this.pointMatrix[4][4]=0.1;
        this.pointMatrix[4][5]=0;
        this.pointMatrix[5][0]=0;
        this.pointMatrix[5][1]=0.05;
        this.pointMatrix[5][2]=1;
        this.pointMatrix[5][3]=1;
        this.pointMatrix[5][4]=1;
        this.pointMatrix[5][5]=1;
    }

    public double[][] getLeftFactor() {
        return this.leftFactor;
    }

    public double[][] getRightFactor() {
        return this.rightFactor;
    }

    public double[][] getPointMatrix() {
        return this.pointMatrix;
    }

    public void set(int x, int y, double z){
        this.setStuff[x][y] = z;
    }

    public double[][] getSetStuff(){
        return this.setStuff;
    }

    public double getCoefficient (int i, int j) { return this.setStuff[i][j]; }
}
