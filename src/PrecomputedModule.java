import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrecomputedModule {
    private List<Point2D> precompiledVelocity;

    public PrecomputedModule(){
        this.precompiledVelocity = new ArrayList<>();
        this.readTheFile();
    }

    private void readTheFile(){
        try {
            FileReader fileReader = new FileReader(new File(Constants.DEFAULT_PRECOMPILED_FILE));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            while(line != null){
                line = line.substring(1, line.length() - 2);
                double[] point = Arrays.stream(line.split(", ")).mapToDouble(Double::parseDouble).toArray();

                this.precompiledVelocity.add(
                        new Point2D(point[0], point[1]));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Point2D> getVelocity() {
        return this.precompiledVelocity;
    }
}
