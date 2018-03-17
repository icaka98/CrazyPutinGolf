import javafx.geometry.Point2D;

import java.util.ArrayList;


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

    public Course(double gravity, double frictionCoef, double maxVelocity, Point2D start, Point2D goal, double toleranceRadius, ArrayList<Double> xcoefficients, ArrayList<Double> ycoefficients) {
        this.gravity = gravity;
        this.frictionCoef = frictionCoef;
        this.maxVelocity = maxVelocity;
        this.start = start;
        this.goal = goal;
        this.toleranceRadius = toleranceRadius;
        Xcoefficients = xcoefficients;
        Ycoefficients = ycoefficients;
    }

}
