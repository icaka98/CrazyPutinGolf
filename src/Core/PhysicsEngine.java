package Core;

import Models.Course;
import Models.Putin;
import Utils.Constants;
        import Utils.CourseReader;
        import Models.Function;
        import javafx.geometry.Point2D;

import java.awt.geom.Line2D;
import java.io.File;
        import java.util.ArrayList;

/**
 * @author Zhecho Mitev
 * @author Silvia Fallone
 * @author Mathieu Coenegracht
 */
public class PhysicsEngine {
    private Course terrainState;

    private double g, mu, h;
    private double velocityX;
    private double velocityY;

    private double accelerationX;
    private double accelerationY;

    private double currentX;
    private double currentY;
    private int count;

    private CourseReader courseReader;

    private Function f;

    private ArrayList<Point2D> coordinatesOfPath;

    public Course getTerrainState() {
        return terrainState;
    }

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
        this.f = new Function(this.courseReader.getEquation());

        this.currentY = this.getTerrainState().getStart().getY();
        this.currentX = this.getTerrainState().getStart().getX();
        g = terrainState.getGravity();
        mu = terrainState.getFrictionCoef();
        h = Constants.TIMESTEP_h;


    }

    public double getCurrentX() {
        return currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    /**
     * the moves in specific direction with certain power denoted by velocityX-Y
     * the new state of the ball is added to a list containing all ball states
     */
    private void updateStateOfBall() {


        calculateRelevantVelocity(h);

        double lastX = currentX;
        double lastY = currentY;

        currentX += h*velocityX;
        currentY += h*velocityY;

        Line2D path = new Line2D.Double(lastX*Constants.SCALAR, lastY*Constants.SCALAR,
                currentX*Constants.SCALAR, currentY*Constants.SCALAR);//the line between the last and the current point

        if(Constants.UP_MID_LINE.intersectsLine(path) || Constants.DOWN_MID_LINE.intersectsLine(path)){
            velocityY *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;
        }

        if(Constants.RIGHT_MID_LINE.intersectsLine(path) || Constants.LEFT_MID_LINE.intersectsLine(path)){
            velocityX *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;
        }

        if(path.intersectsLine(Constants.UP_WALL) || path.intersectsLine(Constants.BOTTOM_WALL)){
            velocityY *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;
        }

        if(path.intersectsLine(Constants.RIGHT_WALL) || path.intersectsLine(Constants.LEFT_WALL)){
            velocityX *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;
        }


        Point2D point2D = new Point2D(currentX,currentY);
        //System.out.println("Shot: " + point2D.getX() + " "  + point2D.getY());
        coordinatesOfPath.add(point2D);
    }

    /**
     * the method updates the velocity of the ball depending on the current acceleration
     */
    private void calculateRelevantVelocity(double h) {
        double x = currentX, y=currentY, vx = velocityX, vy = velocityY, ax = accelerationX, ay = accelerationY;
        double[] a = calculateAcceleration(x+h/2*vx,y+h/2*vy,vx+h/2*ax,vy+h/2*ay);
        vx = velocityX + h*a[0];
        vy = velocityY + h*a[1];

        System.out.println("Ax = " +a[0]);
        System.out.println("Ay = " +a[1]);
        System.out.println("Vx = " +vx);
        System.out.println("Vy = " +vy);

        velocityX = vx;
        velocityY = vy;

    }


    /**
     * the acceleration of the ball is calculated by standard formula which takes into account:
     * partial derivatives of the height of the function,velocity, friction coefficient and gravity
     */

    private double calculateXAcceleration(double x, double y, double vx, double vy){
        double dzTodx = calculateDerivativeWithRespectToX(x, y);

        double ax = -g * (dzTodx + mu * (vx/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        return ax;
    }

    private double calculateYAcceleration(double x, double y, double vx, double vy){
        double dzTody = calculateDerivativeWithRespectToY(x, y);

        double ay = -g * (dzTody + mu * (vy/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        return ay;
    }

    private double[] calculateAcceleration(double x, double y, double vx, double vy){
        double dzTodx = calculateDerivativeWithRespectToX(x, y);
        double dzTody = calculateDerivativeWithRespectToY(x, y);

        double[] a = new double[2];
        a[0] = -g * (dzTodx + mu * (vx/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        a[1] = -g * (dzTody + mu * (vy/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        return a;
    }

    /**
     *
     * @param x current X coordinate of the ball
     * @param y current Y coordinate of the ball
     * @return true if the ball has fallen into the water(z<0), false if the ball is on the ground(z>=0)
     */
    private boolean collisionDetected(double x, double y){
        return f.solve(x,y) < 0;
    }

    /**
     * Sets up the course variables by reading them from a file
     */
    private void readCourse(){
        File file = new File(Constants.DEFAULT_COURSE_FILE);
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
        return f.solve(x, y);
    }

    /**
     * partial derivative calculated with respect to X, using the currentX and currentY
     * @return the value of the derivative
     */
    private double calculateDerivativeWithRespectToX(double x, double y){
        // df(x,y)/dx = (f(x+h),y) - f(x-h, y))/(2*h)
        double h = 0.000001;
        //System.out.println("dx = " + dx);
        return (f.solve(x +h , y) - f.solve(x - h, y))/(2*h);

    }

    /**
     * partial derivative calculated with respect to Y, using the currentX and currentY
     * @return the value of the derivative
     */
    private double calculateDerivativeWithRespectToY(double x, double y){
        // df(x,y)y = (f(x,y+h) - f(x,y-h))/(2*h)
        double h = 0.000001;
        //System.out.println("dy = " +dy);
        return (f.solve(x, y +h) - f.solve(x, y - h))/(2*h);
    }


    /**
     * A function that calculates the velocity of the player's shot based on the end point of the shot arrow and the current point of the ball
     * It takes into account the max velocity
     * @param endX X coordinate of the end of the shot arrow
     * @param endY Y coordinate of the end of the shot arrow
     */
    public void takeVelocityOfShot(double endX, double endY){
        count = 0;
        double velX = endX - currentX;
        double velY = endY - currentY;

        if(Math.abs(velX) < terrainState.getMaxVelocity())
        {
            velocityX = velX;
        }
        else if(velX < 0){
            velocityX = -terrainState.getMaxVelocity();
        }
        else{
            velocityX = terrainState.getMaxVelocity();
        }
        velocityX *= Constants.VELOCITY_SCALAR;

        if(Math.abs(velY) < terrainState.getMaxVelocity()) {
            velocityY = velY;
        }
        else if(velY < 0){
            velocityY = -terrainState.getMaxVelocity();
        }
        else {
            velocityY = terrainState.getMaxVelocity();
        }
        velocityY *= Constants.VELOCITY_SCALAR;

    }

    /**
     * the start of the engine which is triggered when the player initiates a shot
     * gathers the sequence of points which the ball has to go through
     */
    public void executeShot(){
        this.coordinatesOfPath = new ArrayList<>();

        double startX = currentX;
        double startY = currentY;
        calculateRelevantVelocity(h/2);

        while (Math.abs(velocityX) > Constants.STOP_SPEED || Math.abs(velocityY) > Constants.STOP_SPEED)
        {
            updateStateOfBall();
        }

        if(collisionDetected(currentX,currentY)){//if there is a collision detected the ball returns to the initial state
            coordinatesOfPath.add(new Point2D(startX,  startY));
        }

    }

    public Point2D finalDestination() {
        int lastPointIndex = this.coordinatesOfPath.size() - 1;
        if(lastPointIndex>0) return this.coordinatesOfPath.get(lastPointIndex);
        else return this.getTerrainState().getStart();
    }
}

