import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.io.File;
import java.util.ArrayList;

public class PhysicsEngine {
    private Course terrainState;

    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY;

    private CourseReader cour;
    private Function functionEvaluator;

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
        this.functionEvaluator = new Function(this.cour.getEquation());
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

        accelerationX = -g * (dzTodx + mu * currentX);

        accelerationY = -g * (dzTody + mu * currentY);

    }

    private boolean collisionDetected(double x, double y){
        return calculteHeight(x, y) < 0;
    }

    private void readCourse(){
        File file = new File("src/Setup.txt");
        cour = new CourseReader(file);
        cour.readCourse();

        this.terrainState = cour.getCourse();
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


    public void takeVelocityOfShot(double x, double y){
        if(x < terrainState.getMaxVelocity())
        {
            velocityX = x;
        }
        else {
            velocityX = terrainState.getMaxVelocity();
        }

        if(y < terrainState.getMaxVelocity()) {
            velocityY = y;
        }
        else {
            velocityY = terrainState.getMaxVelocity();
        }
    }

    public void startEngine(){

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
