import javafx.geometry.Point2D;

import java.io.*;
import java.lang.Double;
import java.util.ArrayList;
import java.util.Arrays;

public class CourseReader {
    private Double g;
    private Double mu;
    private Double vmax;

    private Double startX;
    private Double startY;
    private Double goalX;
    private Double goalY;
    private Double tolerance;
    private String equation;

    private FileReader fr;
    private BufferedReader br;

    public CourseReader(File file) {
        setupReader(file);
    }

    public Course getCourse(){
        return new Course(g, mu, vmax, new Point2D(startX, startY),
                new Point2D(goalX, goalY), tolerance,
                new ArrayList<>(Arrays.asList(0.1, 0.03)), new ArrayList<>(Arrays.asList(0.1, 0.2)));
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

    public void readCourse() {
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
            fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEquation() {
        return equation;
    }
}