package Core;

import Core.Physics.PhysicsEngine;
import Graphics.ComponentFactory;
import Models.Bots.Alistair;
import Models.Bots.Bot;
import Models.Course;
import Utils.Constants;
import Models.Shot;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;


public class BotTester {
    private static Course course = new Course("1");
    private static PhysicsEngine engine = new PhysicsEngine("1");
    private static Controller c = new Controller();
    private static Bot bot = new Alistair(engine, c);
    private static Circle hole = ComponentFactory.getHole(c.getFinishX(), c.getFinishY(), c.getTolerance());;
    private static Circle ball = ComponentFactory.getBall(c.getStartX(),c.getStartY());

    public static void main(String[] args){
        int[] steps = new int[10];
        ArrayList<Long> steptime = new ArrayList<>();
        Point2D start = course.getStart();

        for(int i = 0; i < 10; i++){
            Boolean goalreached = false;
            engine.setCurrentX(start.getX());
            engine.setCurrentY(start.getY());
            int count = 0;
            while(!goalreached){
                count++;
                Long startTime = System.currentTimeMillis();
                Shot s = bot.go();
                long endTime = System.currentTimeMillis();
                steptime.add(endTime-startTime);
                engine.takeVelocityOfShot(s.getVelocityX(),s.getVelocityY());
                engine.executeShot();
                ball.setCenterX(engine.getCurrentX()*Constants.SCALAR+Constants.FIELD_WIDTH/2);
                ball.setCenterY(engine.getCurrentY()*Constants.SCALAR+Constants.FIELD_HEIGHT/2);

                //System.out.println(engine.finalDestination());
                if(Math.sqrt((hole.getCenterX() - ball.getCenterX())
                        * (hole.getCenterX() - ball.getCenterX())
                        + (hole.getCenterY() - ball.getCenterY())
                        * (hole.getCenterY() - ball.getCenterY()))
                        <= hole.getRadius()) {
                    goalreached = true;
                }
            }
            steps[i]=count;
        }
        double sum = 0;
        for(int i : steps){
            sum+=i;
        }

        long timesum = 0;
        for(long l : steptime){
            timesum+=l;
        }

        double average = sum/(double)steps.length;
        double averageTime = timesum/(long)steptime.size();
        System.out.println(steps.length);
        System.out.println(Arrays.toString(steps));
        System.out.println(sum);
        System.out.println("Average number of steps = "+average);
        System.out.println("Average shot time in ms = "+averageTime);
    }
}
