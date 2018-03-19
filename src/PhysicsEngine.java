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

    public PhysicsEngine() {
        timer = new Timer();
    }

    private void updateStateOfBall() {

        if(collisionDetected(currentX+velocityX, currentY+velocityY)){
            velocityY = 0;
            velocityX = 0;
            //do something to stop the ball
        }
        else {
            currentX += velocityX;
            currentY += velocityY;
        }
    }

    private void evaluateNewVelocity() {
        calculateAcceleration();

        //not working for negative vectors
        if(accelerationX + velocityX > 0)
            velocityX += accelerationX;

        if(accelerationY + velocityY > 0)
            velocityY += accelerationY;
    }

    private void calculateAcceleration() {
            accelerationX -= terrainState.getFrictionCoef();

            accelerationY -= terrainState.getFrictionCoef();
    }

    private boolean collisionDetected(double x, double y){
        if(calculteHeight(x,y) < 0){
            return true;
        }

        return false;
    }

    public void readCourse(){
        //TODO :read from the File and parse the data to the new Course
        //terrainState = new Course()
    }

    public double calculteHeight(double x, double y){

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

    public double calculateDerivativeWithRespectToX(double x, double y){
        return 0;
    }

    public double calculateDerivativeWithRespectToY(double x, double y){
        return 0;
    }

    public void takeVelocityOfShot(double x, double y){
        velocityX = x;
        velocityY = y;
    }

    public void run(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                evaluateNewVelocity();
                updateStateOfBall();
            }
        }, ticksPerSecond);//not sure if working
    }
}
