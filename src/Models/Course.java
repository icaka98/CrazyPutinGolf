package Models;

import javafx.geometry.Point2D;

import java.util.ArrayList;


/**
 * @author Zhecho Mitev
 * Class Course contains all information about the terrain and the ball of the game
 */
public class Course {
    private double gravity;
    private double frictionCoef;
    private double maxVelocity;

    private Point2D start;
    private Point2D goal;

    private double toleranceRadius;

    private ArrayList<Double> Xcoefficients;///the coefficients in front of X in the function of the height
    private ArrayList<Double> Ycoefficients;///the coefficients in front of Y in the function of the height

    public double getGravity() {
        return gravity;
    }

    public double getToleranceRadius() {
        return toleranceRadius;
    }

    public ArrayList<Double> getXcoefficients() {
        return Xcoefficients;
    }

    public ArrayList<Double> getYcoefficients() {
        return Ycoefficients;
    }

    public double getFrictionCoef() {
        return frictionCoef;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public Point2D getStart() {
        return start;
    }

    public Point2D getGoal() {
        return goal;
    }

    /**
     *
     * @param gravity gravitational power of course in m/s^-2
     * @param frictionCoef coefficient which determines the friction of the ball
     * @param maxVelocity the maximum power of a shot
     * @param start starting point of the ball
     * @param goal the center of the area which the ball shall reach
     * @param toleranceRadius the radius of the area the ball shall reach
     * @param xcoefficients the coefficients in front of X in the function of the height
     * @param ycoefficients the coefficients in front of Y in the function of the height
     */
    public Course(double gravity, double frictionCoef, double maxVelocity, Point2D start, Point2D goal, double toleranceRadius, ArrayList<Double> xcoefficients, ArrayList<Double> ycoefficients) {
        this.gravity = gravity;
        this.frictionCoef = frictionCoef;
        this.maxVelocity = maxVelocity;
        this.start = start;
        this.goal = goal;
        this.toleranceRadius = toleranceRadius;

        //TODO the coefficients have to be removed after implementing the numerical derivative
        Xcoefficients = xcoefficients;
        Ycoefficients = ycoefficients;
    }

}
