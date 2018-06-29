package Models;

import Utils.Constants;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhecho Mitev
 * Class Course contains all information about the terrain and the ball of the game
 */
public class Course {
    private double g, mu, vmax, tolerance;
    private Point2D start, goal;
    private String equation;

    private List<Rectangle> obstacles;

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
        this.obstacles = new ArrayList<>();
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

            this.obstacles = new ArrayList<>();

            String line = this.br.readLine();

            if(line == null){
                this.fr.close();
                this.br.close();

                return;
            }

            String obstacleInfo = this.br.readLine().split(" = ")[1];
            obstacleInfo = obstacleInfo.substring(1, obstacleInfo.length() - 1);

            String[] obstaclesStrings = Arrays.stream(obstacleInfo.split("\\),\\(")).toArray(String[]::new);

            for (String obstr : obstaclesStrings){
                double[] tokens = Arrays.stream(obstr.split(",")).mapToDouble(Double::parseDouble).toArray();
                this.obstacles.add(new Rectangle(
                        tokens[0]*Constants.SCALAR + 250.0,
                        tokens[1]*Constants.SCALAR + 250.0,
                        tokens[2]*Constants.SCALAR,
                        tokens[3]*Constants.SCALAR));
            }

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

    public List<Rectangle> getObstacles(){ return this.obstacles; }
}
