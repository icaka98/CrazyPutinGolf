import java.io.*;
import java.lang.Double;

public class CourseReader {

    private double g = 9.81;
    private Double mu;
    private Double vmax;

    private Double startX;
    private Double startY;
    private Double goalX;
    private Double goalY;
    private Double tolerance;

    File file = new File("C:/Users/svfal/OneDrive/Documents/GitHub/CrazyPutinGolf/src/Setup.txt");
    FileReader fr;
    BufferedReader br;


    private void setupReader() {
        try {
            this.fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.br = new BufferedReader(fr);
    }

    public void readCourse() {
        setupReader();
        try {
            this.mu = mu.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.vmax = vmax.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.startX = startX.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.startY = startY.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.goalX = goalX.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.goalY = goalY.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
            this.tolerance = tolerance.parseDouble(br.readLine().replaceAll("[^\\d.]", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printData() {
        System.out.println(mu);
        System.out.println(vmax);
        System.out.println(startX);
        System.out.println(startY);
        System.out.println(goalX);
        System.out.println(goalY);
        System.out.println(tolerance);
    }
}
