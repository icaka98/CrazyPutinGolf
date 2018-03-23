package Core;

import Models.Course;
import Utils.Constants;
import Utils.CourseReader;
import Models.Function;
import javafx.geometry.Point2D;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Zhecho Mitev
 * @author Silvia Fallone
 * @author Mathieu Coenegracht
 */
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

    private ArrayList<Point2D> coordinatesOfPath;

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public ArrayList<Point2D> getCoordinatesOfPath() {
        return this.coordinatesOfPath;
    }

    public PhysicsEngine() {
        this.readCourse();
        this.coordinatesOfPath = new ArrayList<>();
        this.functionEvaluator = new Function(this.courseReader.getEquation());
    }

    /**
     * the moves in specific direction with certain power denoted by velocityX-Y
     * the new state of the ball is added to a list containing all ball states
     */
    private void updateStateOfBall() {

        System.out.println("currentX+Constants.TIMESTEP_h*velocityX: " + (currentX+ Constants.TIMESTEP_h*velocityX));

        currentX += Constants.TIMESTEP_h*velocityX;
        if(Math.abs(currentX) > Constants.WALL_POSITION)
        {
            velocityX *= -1;
        }

        currentY += Constants.TIMESTEP_h*velocityY;
        if(Math.abs(currentY) > Constants.WALL_POSITION)
        {
            velocityY *= -1;
        }

        Point2D point2D = new Point2D(currentX,currentY);
        System.out.println("Point: " + point2D.getX() + " "  + point2D.getY());
        coordinatesOfPath.add(point2D);
    }

    /**
     * the method updates the velocity of the ball depending on the current acceleration
     * @return true if the speed of the ball is enough to move, false if the ball does not have sufficient speed to move
     */
    private boolean calculateRelevantVelocity() {
        calculateAcceleration();

        velocityY += Constants.TIMESTEP_h*accelerationY;
        velocityX += Constants.TIMESTEP_h*accelerationX;


        return (Math.abs(velocityX) > 0.01 && Math.abs(velocityY) > 0.01);
    }

    /**
     * the acceleration of the ball is calculated by standard formula which takes into account:
     * partial derivatives of the height of the function,velocity, friction coefficient and gravity
     */
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

    /**
     *
     * @param x current X coordinate of the ball
     * @param y current Y coordinate of the ball
     * @return true if the ball has fallen into the water(z<0), false if the ball is on the ground(z>=0)
     */
    private boolean collisionDetected(double x, double y){
        return calculateHeight(x, y) < 0;
    }

    /**
     * Sets up the course variables by reading them from a file
     */
    private void readCourse(){
        File file = new File("src/Setup.txt");
        courseReader = new CourseReader(file);

        this.terrainState = courseReader.getCourse();
    }

    /**
     * calculate the height(z) function
     * @param x current X coordinate of the ball
     * @param y current Y coordinate of the ball
     * @return a double number which depends on x and y
     */
    private double calculateHeight(double x, double y){
         return functionEvaluator.solve(x,y);
    }

    /**
     * partial derivative calculated with respect to X, which is working only for polynomial functions
     * @param x value of X
     * @return the value of the derivative
     */
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

    /**
     * partial derivative calculated with respect to Y, which is working only for polynomial functions
     * @param y value of y
     * @return the value of the derivative
     */
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


    /**
     * A function that calculates the velocity of the player's shot based on the end point of the shot arrow and the current point of the ball
     * It takes into account the max velocity
     * @param endX X coordinate of the end of the shot arrow
     * @param endY Y coordinate of the end of the shot arrow
     */
    public void takeVelocityOfShot(double endX, double endY){
        double velX = endX - currentX;
        double velY = endY - currentY;

        if(Math.abs(velX*2) < terrainState.getMaxVelocity()*2)
        {
            velocityX = velX*2;
        }
        else if(velX < 0){
            velocityX = -terrainState.getMaxVelocity();
        }
        else{
            velocityX = terrainState.getMaxVelocity();
        }
        if(Math.abs(velY*2) < terrainState.getMaxVelocity()*2) {
            velocityY = velY*2;
        }
        else if(velY < 0){
            velocityY = -terrainState.getMaxVelocity();
        }
        else {
            velocityY = terrainState.getMaxVelocity();
        }
    }

    /**
     * the start of the engine which is triggered when the player initiates a shot
     * gathers the sequence of points which the ball has to go through
     */
    public void executeShot(){
        this.coordinatesOfPath = new ArrayList<>();

        double startX = currentX;
        double startY = currentY;

        while (calculateRelevantVelocity())
        {
            updateStateOfBall();
        }

        if(collisionDetected(currentX,currentY)){//if there is a collision detected the ball returns to the initial state
            coordinatesOfPath.add(new Point2D(startX,  startY));
        }
    }
}