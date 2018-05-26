package Core;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixXL {
    private double[][] leftFactor = new double[][]{
            { 1, 0, 0, 0 },
            { 0, 0, 1, 0 },
            { -3, 3, -2, -1 },
            { 2, -2, 1, 1 },
    };
    private double[][] rightFactor = new double[][]{
            { 1, 0, -3, 2 },
            { 0, 0, 3, -2 },
            { 0, 1, -2, 1 },
            { 0, 0, -1, 1 },
    };
    private double min = -6, max = 6, cntr30 = 0, cntr70 = 0;
    private double[][] pointMatrix = new double[6][6];

    public MatrixXL(){
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

    public void setValue(int x, int y, double z){
        pointMatrix[x][y] = z;
    }

//    public double getB(){
//
//    }

}
