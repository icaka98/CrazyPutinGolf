package Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {

    public void writeToFile(double g, double mu, double vmax, double startX, double startY, double goalX, double goalY, double tolerance, String height){

        String fileName = "DataFile";
        try{
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("g = " + g);
            writer.println("mu = " + mu);
            writer.println("vmax = " + vmax);
            writer.println("start = (" + startX + "," + startY + ")");
            writer.println("goal = (" + goalX + "," + goalY + ")");
            writer.println("tolerance = " + tolerance);
            writer.println("height = " + height);
            writer.close();
            System.out.println("Write to file Done!");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }


    }
}
