package Core.Physics;

/**
 * @author Silvia Fallone
 */
public class MatrixXL {
    private double[][] fMatrix;
    private double[][] pointMatrix;

    public MatrixXL(){
        this.pointMatrix = new double[6][6];
        this.fMatrix = new double[4][4];

        this.newPointMatrix();
    }

    private void newPointMatrix() {
       /*this.pointMatrix = new double[][] { //36 preset points
                {0, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1},
        };*/
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

    public double[][] getPointMatrix() {
        return this.pointMatrix;
    }

    public void setfMatrix(int x, int y, double z){
        this.fMatrix[x][y] = z;
    }

    public double[][] getfMatrix(){
        return this.fMatrix;
    }

    public double getCoefficient (int i, int j) { return this.fMatrix[i][j]; }
}
