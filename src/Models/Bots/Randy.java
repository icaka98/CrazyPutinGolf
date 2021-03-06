package Models.Bots;

import Core.Physics.PhysicsEngine;
import Models.Shot;
import javafx.geometry.Point2D;

import java.util.Random;

/**
 * @author Hao Yun
 * Trial and error algorithm
 **/
public class Randy extends Bot {

    public Randy(PhysicsEngine physicsEngine) {
        super(physicsEngine);
    }

    /**
     * Executes random shots until a good one is not found.
     **/
    @Override
    public Shot go() {
        this.initialX = this.engine.getCurrentX();
        this.initialY = this.engine.getCurrentY();

        Random rnd = new Random();
        double distance = 1;
        Shot current = null;
        int executedShots = 0;

        double xDirection = this.engine.getTerrainState().getGoal().getX();
        double yDirection = this.engine.getTerrainState().getGoal().getY();//getting the coordinates of the target

        while (distance > engine.getTerrainState().getToleranceRadius()*10 && executedShots < 20000) {
            this.engine.setCurrentX(this.initialX);
            this.engine.setCurrentY(this.initialY);//intializing the physics engine

            double velocityX = (rnd.nextDouble() - 0.5) * this.engine.getTerrainState().getMaxVelocity() + xDirection;
            double velocityY = (rnd.nextDouble() - 0.5) * this.engine.getTerrainState().getMaxVelocity() + yDirection;//getting random velocity

            this.engine.takeVelocityOfShot(velocityX, velocityY);
            this.engine.executeShot();
            Point2D finalDestination = this.engine.finalDestination();

            distance = finalDestination.distance(this.engine.getTerrainState().getGoal());//measuring the distance to the goal
            current = new Shot(velocityX, velocityY, distance);
            executedShots++;
        }

        return current;
    }
}