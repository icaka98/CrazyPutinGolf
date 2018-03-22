import javafx.geometry.Point2D;

import java.io.File;
import java.util.ArrayList;

public class PhysicsEngine {
    private Course terrainState;

    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY;

    private double currentX;
    private double currentY;

    private CourseReader courseReader;
    private Function functionEvaluator;

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public ArrayList<Point2D> getCoordinatesOfPath() {
        return this.coordinatesOfPath;
    }

    private ArrayList<Point2D> coordinatesOfPath;

    public PhysicsEngine() {
        this.readCourse();
        this.coordinatesOfPath = new ArrayList<>();
        this.functionEvaluator = new Function(this.courseReader.getEquation());
    }

    private void updateStateOfBall() {

        System.out.println("currentX+Constants.TIMESTEP_h*velocityX: " + (currentX+Constants.TIMESTEP_h*velocityX));

        currentX += Constants.TIMESTEP_h*velocityX;
        currentY += Constants.TIMESTEP_h*velocityY;
        Point2D point2D = new Point2D(currentX,currentY);
        System.out.println("Point: " + point2D.getX() + " "  + point2D.getY());
        coordinatesOfPath.add(point2D);
    }

    private boolean evaluateNewVelocity() {
        calculateAcceleration();

        boolean moveX = false;
        System.out.println("accelerationX " + accelerationX);

        System.out.println("velocityX : " + velocityX);
        if((Constants.TIMESTEP_h*accelerationX + velocityX >= 0 && velocityX > 0) ||
                (Constants.TIMESTEP_h*accelerationX + velocityX <= 0 && velocityX <0))
        {
            velocityX += Constants.TIMESTEP_h*accelerationX;
            System.out.println("velocityX : " + velocityX);
            moveX =true;
        }

        boolean moveY =false;
        System.out.println("accelerationY " + accelerationY);
        if((Constants.TIMESTEP_h*accelerationY + velocityY >= 0 && velocityY > 0) ||
                (Constants.TIMESTEP_h*accelerationY+velocityY <= 0 && velocityY <0))
        {
            velocityY += Constants.TIMESTEP_h*accelerationY;
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

        accelerationX = -g * (dzTodx + mu * (velocityX/Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2))));

        accelerationY = -g * (dzTody + mu * (velocityY/Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2))));
    }

    private boolean collisionDetected(double x, double y){
        return calculteHeight(x, y) < 0;
    }

    private void readCourse(){
        File file = new File("src/Setup.txt");
        courseReader = new CourseReader(file);
        courseReader.readCourse();

        this.terrainState = courseReader.getCourse();
    }

    private double calculteHeight(double x, double y){
         return functionEvaluator.solve(x,y);
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


    public void takeVelocityOfShot(double endX, double endY){
        double velX = endX - currentX;
        double velY = endY - currentY;

        if(Math.abs(velX) < terrainState.getMaxVelocity())
        {
            velocityX = velX;
        }
        else {
            velocityX = terrainState.getMaxVelocity();
        }

        if(Math.abs(velY) < terrainState.getMaxVelocity()) {
            velocityY = velY;
        }
        else {
            velocityY = terrainState.getMaxVelocity();
        }
    }

    public void startEngine(){
        this.coordinatesOfPath = new ArrayList<>();

        double startX = currentX;
        double startY = currentY;
        while (evaluateNewVelocity() && !collisionDetected(currentX, currentY))
        {
            updateStateOfBall();
        }

        if(collisionDetected(currentX,currentY)){//if there is a collision detected the ball returns to the initial state
            coordinatesOfPath.add(new Point2D(startX,  startY));
        }
    }
}
