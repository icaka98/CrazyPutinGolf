package Models;

import Utils.Constants;
import javafx.geometry.Point2D;

import java.io.*;

/**
 * @author Zhecho Mitev
 * Class Course contains all information about the terrain and the ball of the game
 */
public class Course {
    private double g, mu, vmax, tolerance;
    private Point2D start, goal;
    private String equation;

    private FileReader fr;
    private BufferedReader br;
    private Course instance;

    public Course(double g, double mu, double vmax, Point2D start,
                  Point2D goal, double tolerance){
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
        this.tolerance = tolerance;
    }

    public Course(String code) {
        this.setupReader(new File(Constants.DEFAULT_COURSE_DIR + "course" + code + ".txt"));
    }

    private void setupReader(File file) {
        try {
            this.fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.br = new BufferedReader(fr);
        this.readCourse();
    }

    private void readCourse() {
        try {
            this.g = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.mu = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.vmax = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.start = new Point2D(Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, "")),
                    Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, "")));
            this.goal = new Point2D(Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, "")),
                    Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, "")));
            this.tolerance = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));

            this.equation = this.br.readLine().replaceAll("z = ", "");

            this.fr.close();
            this.br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getGravity() {
        return this.g;
    }

    public double getToleranceRadius() {
        return this.tolerance;
    }

    public double getFrictionCoef() {
        return this.mu;
    }

    public double getMaxVelocity() {
        return this.vmax;
    }

    public Point2D getStart() {
        return this.start;
    }

    public Point2D getGoal() {
        return this.goal;
    }

    /**
     * Get the equation that shapes the course field
     * @return the equation that shapes the course field
     */
    public String getEquation() {
        return this.equation;
    }

    /**
     * Get the equation that shapes the course field in a compact form
     * @return the equation that shapes the course field
     */
    public String getCompactEquation(){
        return this.equation.replaceAll("\\s+", "");
    }
}
