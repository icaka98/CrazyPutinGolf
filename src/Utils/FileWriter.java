package Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {
    public static void writeToFile(double g, double mu, double vmax,
                            double startX, double startY, double goalX,
                            double goalY, double tolerance, String function){
        try{
            PrintWriter writer = new PrintWriter(Constants.DEFAULT_SAVE_FILE);

            writer.println("g = " + g);
            writer.println("mu = " + mu);
            writer.println("vmax = " + vmax);
            writer.println("start = (" + startX + "," + startY + ")");
            writer.println("goal = (" + goalX + "," + goalY + ")");
            writer.println("tolerance = " + tolerance);
            writer.println("height = " + function);
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
