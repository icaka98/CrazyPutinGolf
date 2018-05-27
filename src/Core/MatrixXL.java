package Core;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixXL {

    private double[][] leftFactor;
    private double[][] rightFactor;

    private double[][] setStuff;
    private double min, max, cntr30, cntr70;
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
        this.min = -6;
        this.max = 6;
        this.cntr30 = 0;
        this.cntr70 = 0;

        newPointMatrix();
    }

    public void newPointMatrix() {
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                double r = ThreadLocalRandom.current().nextDouble(min,max + 1);
                pointMatrix[i][j] = r;
                if(r < 0) {
                    cntr30++;
                } else {
                    cntr70++;
                }
                if(cntr30 > (36*0.3)){
                    min = 0;
                }
                if(cntr70 > (36*0.7)){
                    max = 0;
                }
            }
        }
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
