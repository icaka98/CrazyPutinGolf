package Utils;

import Models.Course;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author Hao Yun
 */
public class FileWriter {
    public static void writeToFile(Course course, String function){
        try{
            PrintWriter writer = new PrintWriter(Constants.DEFAULT_SAVE_FILE);

            writer.println("g = " + course.getGravity());
            writer.println("mu = " + course.getFrictionCoef());
            writer.println("vmax = " + course.getMaxVelocity());
            writer.println("start = (" + course.getStart().getX() + "," + course.getStart().getY() + ")");
            writer.println("goal = (" + course.getGoal().getX() + "," + course.getGoal().getX() + ")");
            writer.println("tolerance = " + course.getToleranceRadius());
            writer.println("height = " + function);
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
