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

    File file = new File("C:/Users/svfal/OneDrive/Documents/Coding/TmpCrazyPutinGolf/src/Setup.txt");
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
            this.mu = mu.parseDouble(br.readLine());
            this.vmax = vmax.parseDouble(br.readLine());
            this.startX = startX.parseDouble(br.readLine());
            this.startY = startY.parseDouble(br.readLine());
            this.goalX = goalX.parseDouble(br.readLine());
            this.goalY = goalY.parseDouble(br.readLine());
            this.tolerance = tolerance.parseDouble(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
