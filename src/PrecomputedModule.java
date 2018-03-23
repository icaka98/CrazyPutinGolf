import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrecomputedModule {
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private List<Point2D> precompiledVelocity;

    public PrecomputedModule(){
        this.precompiledVelocity = new ArrayList<>();

        try {
            this.fileReader = new FileReader(new File("src/precomputed_velocity.txt"));
            this.bufferedReader = new BufferedReader(this.fileReader);

            String line = this.bufferedReader.readLine();

            while(line != null){
                line = line.substring(1, line.length() - 2);
                double[] point = Arrays.stream(line.split(", ")).mapToDouble(Double::parseDouble).toArray();

                this.precompiledVelocity.add(
                        new Point2D(point[0], point[1]));

                line = this.bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Point2D> getVelocity() {
        return this.precompiledVelocity;
    }
}
