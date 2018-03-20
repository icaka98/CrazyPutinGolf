import javafx.geometry.Point3D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PhysicsEngine {
    private Course terrainState;

    private Timer timer;
    private int ticksPerSecond;

    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY;

    private double currentX;
    private double currentY;

    private ArrayList<Point2D> coordinatesOfPath;

    public PhysicsEngine() {
        timer = new Timer();
    }


    private void updateStateOfBall() {

        if(collisionDetected(currentX+0.1*velocityX, currentY+0.1*velocityY)){
            velocityY = 0;
            velocityX = 0;
            //do something to stop the ball
        }
        else {
            currentX += 0.1*velocityX;
            currentY += 0.1*velocityY;
            Point2D point2D = new Point2D.Double(currentX,currentY);
            coordinatesOfPath.add(point2D);
        }
    }

    private boolean evaluateNewVelocity() {
        calculateAcceleration();

        boolean moveX = false;
        if((0.001*accelerationX + velocityX >= 0 && velocityX > 0) || (0.001*accelerationX+velocityX <= 0 && velocityX <0))
        {
            velocityX += 0.001*accelerationX;
            moveX =true;
        }

        boolean moveY =false;
        if((0.001*accelerationY + velocityY >= 0 && velocityY > 0) || (0.001*accelerationY+velocityY <= 0 && velocityY <0))
        {
            velocityY += 0.001*accelerationY;
            moveY =true;
        }

        return moveX || moveY;
    }

    private void calculateAcceleration() {
        double g = terrainState.getGravity();
        double dzTodx = calculateDerivativeWithRespectToX(currentX);
        double dzTody = calculateDerivativeWithRespectToY(currentY);
        double mu = terrainState.getFrictionCoef();
        double square = Math.sqrt(dzTodx * dzTodx + dzTody * dzTody);
        accelerationX = -g * (dzTodx + mu * currentX / square);

        accelerationY = -g * (dzTody + mu * currentY / square);

    }

    private boolean collisionDetected(double x, double y){
        return calculteHeight(x, y) < 0;
    }

    public void readCourse(){
        //TODO :read from the File and parse the data to the new Course
        //terrainState = new Course()
    }

    private double calculteHeight(double x, double y){

        double result =0;
        ArrayList<Double> xCoefficients = terrainState.getXcoefficients();
        for (int i = 0; i < xCoefficients.size(); i++) {
            result+= xCoefficients.get(i)*Math.pow(x, i);
        }

        ArrayList<Double> yCoefficients = terrainState.getYcoefficients();
        for (int i = 0; i < yCoefficients.size(); i++) {
            result+= yCoefficients.get(i)*Math.pow(y, i);
        }

        return result;
    }

    private double calculateDerivativeWithRespectToX(double x){

        double derivative[] = new double[terrainState.getXcoefficients().size()-1];
        for(int i = 0; i < terrainState.getXcoefficients().size()-1; i++)
            derivative[i] = terrainState.getXcoefficients().get(i)*(terrainState.getXcoefficients().size()-i-1);

        double result =0;
        for (int i = 0; i < derivative.length; i++) {
            result+= derivative[i]*Math.pow(x, i);
        }
        return result;
    }

    private double calculateDerivativeWithRespectToY(double y){

        double derivative[] = new double[terrainState.getYcoefficients().size()-1];
        for(int i = 0; i < terrainState.getYcoefficients().size()-1; i++)
            derivative[i] = terrainState.getYcoefficients().get(i)*(terrainState.getYcoefficients().size()-i-1);

        double result =0;
        for (int i = 0; i < derivative.length; i++) {
            result+= derivative[i]*Math.pow(y, i);
        }
        return result;
    }

    public void takeVelocityOfShot(double x, double y){
        velocityX = x;
        velocityY = y;
    }

    public void startEngine(){
        while (evaluateNewVelocity() == true)
            updateStateOfBall();
    }
}
