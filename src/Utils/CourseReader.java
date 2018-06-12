package Utils;

import Models.Course;
import javafx.geometry.Point2D;
import java.io.*;
import java.lang.Double;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Silvia Fallone
 * A class that reads the course from a file
 */
public class CourseReader {
    private Double g, mu, vmax, tolerance;
    private Double startX, startY, goalX, goalY;
    private String equation;

    private FileReader fr;
    private BufferedReader br;

    /**
     * CourseReader constructor
     * @param file the file to read the text from
     */
    public CourseReader(File file) {
        this.setupReader(file);
    }

    /**
     * Gets the course with the current terrain features
     * @return the course with the current terrain features
     */
    public Course getCourse(){
        return new Course(
                this.g,
                this.mu,
                this.vmax,
                new Point2D(this.startX, this.startY),
                new Point2D(this.goalX, this.goalY),
                this.tolerance,
                new ArrayList<>(Arrays.asList(0.0, 0.1, 0.03)),
                new ArrayList<>(Arrays.asList(0.1, 0.2)));
    }

    /**
     * Sets up the input stream for the reader
     * @param file the file to read the text from
     */
    private void setupReader(File file) {
        try {
            this.fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.br = new BufferedReader(fr);
        this.readCourse();
    }

    /**
     * Stores the values and the equation in the given file as variables
     */
    private void readCourse() {
        try {
            this.g = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.mu = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.vmax = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.startX = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.startY = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.goalX = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.goalY = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));
            this.tolerance = Double.parseDouble(br.readLine().replaceAll(Constants.NON_NUMBERS, ""));

            this.equation = br.readLine().replaceAll("z = ", "");

            this.fr.close();
            this.br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the equation that shapes the course field
     * @return the equation that shapes the course field
     */
    public String getEquation() {
        return this.equation;
    }
}