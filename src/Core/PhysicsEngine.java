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
    private double velocityX, velocityY;
    private double currentX, currentY;

    private CourseReader courseReader;
    private Function f;

    private ArrayList<Point2D> coordinatesOfPath;

    //private double accelerationX;
    //private double accelerationY;

    public PhysicsEngine() {
        this.readCourse();

        this.coordinatesOfPath = new ArrayList<>();
        this.f = new Function(this.courseReader.getEquation());

        this.currentY = this.getTerrainState().getStart().getY();
        this.currentX = this.getTerrainState().getStart().getX();

        this.g = this.terrainState.getGravity();
        this.mu = this.terrainState.getFrictionCoef();

        this.h = Constants.TIMESTEP_h;
    }

    public Course getTerrainState() {
        return this.terrainState;
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

    public double getCurrentX() {
        return this.currentX;
    }

    public double getCurrentY() {
        return this.currentY;
    }

    /**
     * the moves in specific direction with certain power denoted by velocityX-Y
     * the new state of the ball is added to a list containing all ball states
     */
    private void updateStateOfBall() {
        //double[] v = calculateVelocity(h/2);
        //calculateRelevantVelocity();

        double lastX = currentX;
        double lastY = currentY;
        double lastVx = velocityX;
        double lastVy = velocityY;
        rk4();

        //currentX += h*velocityX;
        //currentY += h*velocityY;
        //currentX += h*v[0];
        //currentY += h*v[1];

        Line2D path = new Line2D.Double(lastX*Constants.SCALAR, lastY*Constants.SCALAR,
                currentX*Constants.SCALAR, currentY*Constants.SCALAR);//the line between the last and the current point

        if(Constants.UP_MID_LINE.intersectsLine(path)
                || Constants.DOWN_MID_LINE.intersectsLine(path)){
            this.currentX = lastX;
            this.currentY = lastY;
            this.velocityX = lastVx;
            this.velocityY = lastVy;

            this.velocityY *= -1;
            rk4();

        }

        if(Constants.RIGHT_MID_LINE.intersectsLine(path)
                || Constants.LEFT_MID_LINE.intersectsLine(path)){
            this.currentX = lastX;
            this.currentY = lastY;
            this.velocityX = lastVx;
            this.velocityY = lastVy;

            this.velocityX *= -1;
            rk4();
        }

        if(path.intersectsLine(Constants.UP_WALL)
                || path.intersectsLine(Constants.BOTTOM_WALL)){
            /*velocityY *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;*/
            this.currentX = lastX;
            this.currentY = lastY;
            this.velocityX = lastVx;
            this.velocityY = lastVy;

            this.velocityY *= -1;
            rk4();
        }

        if(path.intersectsLine(Constants.RIGHT_WALL)
                || path.intersectsLine(Constants.LEFT_WALL)){
            /*velocityX *= -1;
            currentX = lastX + h*velocityX;
            currentY = lastY + h*velocityY;*/
            this.currentX = lastX;
            this.currentY = lastY;
            this.velocityX = lastVx;
            this.velocityY = lastVy;

            this.velocityX *= -1;
            rk4();
        }


        Point2D point2D = new Point2D(this.currentX, this.currentY);
        //System.out.println("Shot: " + point2D.getX() + " "  + point2D.getY());
        this.coordinatesOfPath.add(point2D);
    }

    /**
     * method that updates the position and velocity using the classical 4th order runge-kutta method.
     */
    private void rk4(){
        double kx0, kx1, kx2, kx3, ky0,ky1,ky2,ky3,lx0,lx1,lx2,lx3,ly0,ly1,ly2,ly3;
        double x=currentX, y=currentY,vx=velocityX,vy=velocityY;

        kx0 = h * vx;
        ky0 = h * vy;
        lx0 = h * calcXAcceleration(x, y, vx, vy);
        ly0 = h * calcYAcceleration(x, y, vx, vy);
        kx1 = h * (vx + 0.5 * lx0);
        ky1 = h * (vy + 0.5 * ly0);
        lx1 = h * calcXAcceleration(x + 0.5 * kx0, y + 0.5 * ky0, vx + 0.5 * lx0, vy + 0.5 * ly0);
        ly1 = h * calcYAcceleration(x + 0.5 * kx0, y + 0.5 * ky0, vx + 0.5 * lx0, vy + 0.5 * ly0);
        kx2 = h * (vx + 0.5 * lx1);
        ky2 = h * (vy + 0.5 * ly1);
        lx2 = h * calcXAcceleration(x + 0.5 * kx1, y + 0.5 * ky1, vx + 0.5 * lx1, vy + 0.5 * ly1);
        ly2 = h * calcYAcceleration(x + 0.5 * kx1, y + 0.5 * ky1, vx + 0.5 * lx1, vy + 0.5 * ly1);
        kx3 = h * (vx + lx2);
        ky3 = h * (vy + ly2);
        lx3 = h * calcXAcceleration(x + kx2, y + ky2, vx + lx2, vy + ly2);
        ly3 = h * calcYAcceleration(x + kx2, y + ky2, vx + lx2, vy + ly2);

        this.currentX = x + (1.0 / 6.0) * (kx0 + 2 * kx1 + 2 * kx2 + kx3);
        this.currentY = y + (1.0 / 6.0) * (ky0 + 2 * ky1 + 2 * ky2 + ky3);

        this.velocityX = vx + (1.0 / 6.0) * (lx0 + 2 * lx1 + 2 * lx2 + lx3);
        this.velocityY = vy + (1.0 / 6.0) * (ly0 + 2 * ly1 + 2 * ly2 + ly3);
    }

    /**
     * the acceleration of the ball is calculated by standard formula which takes into account:
     * partial derivatives of the height of the function,velocity, friction coefficient and gravity
     */
    private double calcXAcceleration(double x, double y, double vx, double vy){
        double dzTodx = calculateDerivativeWithRespectToX(x, y);

        return -g * (dzTodx + mu * (vx/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
    }

    private double calcYAcceleration(double x, double y, double vx, double vy){
        double dzTody = calculateDerivativeWithRespectToY(x, y);

        return -g * (dzTody + mu * (vy/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
    }

    /**
     * @param x current X coordinate of the ball
     * @param y current Y coordinate of the ball
     * @return true if the ball has fallen into the water(z<0), false if the ball is on the ground(z>=0)
     */
    private boolean collisionDetected(double x, double y){
        return this.f.solve(x,y) < 0;
    }

    /**
     * Sets up the course variables by reading them from a file
     */
    private void readCourse(){
        File file = new File(Constants.DEFAULT_COURSE_FILE);
        this.courseReader = new CourseReader(file);

        this.terrainState = this.courseReader.getCourse();
    }

    /**
     * partial derivative calculated with respect to X, using a given x and a given y
     * using the 5-point-difference method.
     * @return the value of the derivative
     */
    private double calculateDerivativeWithRespectToX(double x, double y){
        // df(x,y)/dx = (f(x+h),y) - f(x-h, y))/(2*h)
        //return (f.solve(x +h , y) - f.solve(x - h, y))/(2*h);
        return (f.solve(x-2*h,y)-8*f.solve(x-h,y)+8*f.solve(x+h,y)-f.solve(x+2*h,y))/(12*h);
    }

    /**
     * partial derivative calculated with respect to Y, using a given x a given y;
     * using the 5-point-difference method
     * @return the value of the derivative
     */
    private double calculateDerivativeWithRespectToY(double x, double y){
        // df(x,y)/dy = (f(x,y+h) - f(x,y-h))/(2*h)
        //return (f.solve(x, y +h) - f.solve(x, y - h))/(2*h);
        return (f.solve(x,y-2*h)-8*f.solve(x,y-h)+8*f.solve(x,y+h)-f.solve(x,y+2*h))/(12*h);
    }

    /**
     * A function that calculates the velocity of the player's shot based on the end point of the shot arrow and the current point of the ball
     * It takes into account the max velocity
     * @param endX X coordinate of the end of the shot arrow
     * @param endY Y coordinate of the end of the shot arrow
     */
    public void takeVelocityOfShot(double endX, double endY){
        double velX = endX - this.currentX;
        double velY = endY - this.currentY;

        if(Math.abs(velX) < this.terrainState.getMaxVelocity()) this.velocityX = velX;
        else if(velX < 0) this.velocityX = - this.terrainState.getMaxVelocity();
        else this.velocityX = this.terrainState.getMaxVelocity();

        this.velocityX *= Constants.VELOCITY_SCALAR;

        if(Math.abs(velY) < this.terrainState.getMaxVelocity())
            this.velocityY = velY;
        else if(velY < 0)
            this.velocityY = - this.terrainState.getMaxVelocity();
        else
            this.velocityY = this.terrainState.getMaxVelocity();

        this.velocityY *= Constants.VELOCITY_SCALAR;
    }

    /**
     * the start of the engine which is triggered when the player initiates a shot
     * gathers the sequence of points which the ball has to go through
     */
    public void executeShot(){
        this.coordinatesOfPath = new ArrayList<>();

        double startX = this.currentX;
        double startY = this.currentY;
        //calculateRelevantVelocity();
        rk4();

        while ((Math.abs(velocityX) > Constants.STOP_SPEED
                || Math.abs(velocityY) > Constants.STOP_SPEED)
                && !collisionDetected(this.currentX, this.currentY)) {
            this.updateStateOfBall();
        }

        //if there is a collision detected the ball returns to the initial state
        if(collisionDetected(this.currentX, this.currentY)){
            this.coordinatesOfPath.add(new Point2D(startX,  startY));
        }
    }

    public Point2D finalDestination() {
        int lastPointIndex = this.coordinatesOfPath.size() - 1;

        if(lastPointIndex > 0)
            return this.coordinatesOfPath.get(lastPointIndex);
        else
            return this.getTerrainState().getStart();
    }
}

/*private double calcXAcceleration(double x, double y, double vx, double vy){
        double dzTodx = calculateDerivativeWithRespectToX(currentX, currentY);

        double ax = -g * (dzTodx + mu * (velocityX/Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2))));
        return ax;

    }

    private double calcYAcceleration(double x, double y, double vx, double vy){
        double dzTody = calculateDerivativeWithRespectToY(currentX, currentY);

        double ay = -g * (dzTody + mu * (velocityY/Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2))));
        return ay;

    }

    /**
     * the method updates the velocity of the ball depending on the current acceleration

    private void calculateRelevantVelocity() {
        //calculateXAcceleration();
        //calculateYAcceleration();
        double x = currentX, y=currentY, vx = velocityX, vy = velocityY, ax = accelerationX, ay = accelerationY;
        double[] a = calculateAcceleration(x+h/2*vx,y+h/2*vy,vx+h/2*ax,vy+h/2*ay);
        vx = velocityX + h*a[0];
        vy = velocityY + h*a[1];
        System.out.println("ax = "+ax +"; ay = "+ay);

        velocityX = vx;
        velocityY = vy;

    }
    private double[] calculateVelocity(double h){

        double x = currentX, y=currentY, vx = velocityX, vy = velocityY, ax = accelerationX, ay = accelerationY;


        double[] a = calculateAcceleration(x+h/2*vx,y+h/2*vy,vx+h/2*ax,vy+h/2*ay);
        double v[] = new double[2];
        v[0] = velocityX + h*a[0];
        v[1] = velocityY + h*a[1];

        //velocityX = v[0];
        //velocityY = v[1];

        return v;


    }
    private double[] calculateAcceleration(double x, double y, double vx, double vy){
        double dzTodx = calculateDerivativeWithRespectToX(x, y);
        double dzTody = calculateDerivativeWithRespectToY(x, y);

        double[] a = new double[2];
        a[0] = -g * (dzTodx + mu * (vx/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        a[1] = -g * (dzTody + mu * (vy/Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2))));
        return a;
    }*/

