import javafx.geometry.Point2D;

import java.io.File;
import java.util.ArrayList;

public class PhysicsEngine {
    private Course terrainState;

    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY;

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
        System.out.println("currentX: "+ currentX);
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    private double currentX;
    private double currentY;

    public ArrayList<Point2D> getCoordinatesOfPath() {
        return coordinatesOfPath;
    }

    private ArrayList<Point2D> coordinatesOfPath;

    public PhysicsEngine() {
        this.readCourse();
        this.coordinatesOfPath = new ArrayList<>();
    }

    private boolean updateStateOfBall() {

        System.out.println("currentX+0.05*velocityX: " + (currentX+0.05*velocityX));
        if(collisionDetected(currentX+0.05*velocityX, currentY+0.05*velocityY)){
            return false;
        }
        else {
            currentX += 0.05*velocityX;
            currentY += 0.05*velocityY;
            Point2D point2D = new Point2D(currentX,currentY);
            System.out.println("Point: " + point2D.getX() + " "  + point2D.getY());
            coordinatesOfPath.add(point2D);
            return true;
        }
    }

    private boolean evaluateNewVelocity() {
        calculateAcceleration();

        boolean moveX = false;
        System.out.println("accelerationX " + accelerationX);

        System.out.println("velocityX : " + velocityX);
        if((0.05*accelerationX + velocityX >= 0 && velocityX > 0) || (0.05*accelerationX+velocityX <= 0 && velocityX <0))
        {
            velocityX += 0.05*accelerationX;
            System.out.println("velocityX : " + velocityX);
            moveX =true;
        }

        boolean moveY =false;

        System.out.println("accelerationY " + accelerationY);
        if((0.05*accelerationY + velocityY >= 0 && velocityY > 0) || (0.05*accelerationY+velocityY <= 0 && velocityY <0))
        {
            velocityY += 0.05*accelerationY;
            System.out.println("velocityY : " + velocityY);
            moveY = true;
        }

        return moveX || moveY;
    }

    private void calculateAcceleration() {
        double g = terrainState.getGravity();
        double dzTodx = calculateDerivativeWithRespectToX(currentX);
        System.out.println("dzTodx " + dzTodx);
        double dzTody = calculateDerivativeWithRespectToY(currentY);
        System.out.println("dzTody " + dzTody);
        double mu = terrainState.getFrictionCoef();

        accelerationX = -g * (dzTodx + mu * currentX);

        accelerationY = -g * (dzTody + mu * currentY);

    }

    private boolean collisionDetected(double x, double y){
        return calculteHeight(x, y) < 0;
    }

    private void readCourse(){
        File file = new File("src/Setup.txt");
        CourseReader cur = new CourseReader(file);
        cur.readCourse();

        this.terrainState = cur.getCourse();
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
        for(int i = 0; i < terrainState.getXcoefficients().size() -1; i++)
            derivative[i] = terrainState.getXcoefficients().get(i+1)*(i+1);

        double result =0;
        for (int i = 0; i < derivative.length; i++) {
            result+= derivative[i]*Math.pow(x, i);
        }
        return result;
    }

    private double calculateDerivativeWithRespectToY(double y){

        double derivative[] = new double[terrainState.getYcoefficients().size()-1];
        for(int i = 0; i < terrainState.getYcoefficients().size() - 1; i++)
            derivative[i] = terrainState.getYcoefficients().get(i+1)*(i+1);

        double result =0;
        for (int i = 0; i < derivative.length; i++) {
            result+= derivative[i]*Math.pow(y, i);
        }
        return result;
    }



    public void takeVelocityOfShot(double x, double y){
        if(x < terrainState.getMaxVelocity())
        {
            velocityX = x;
        }
        else {
            velocityX = terrainState.getMaxVelocity();
        }

        if(y< terrainState.getMaxVelocity()) {
            velocityY = y;
        }
        else {
            velocityY = terrainState.getMaxVelocity();
        }

        this.accelerationX = 0;
        this.accelerationY = 0;
    }

    public void startEngine(){

        boolean collision = false;
        while (evaluateNewVelocity() && !collision)
        {
            System.out.println("start");

            collision = !updateStateOfBall();
        }
    }
}
