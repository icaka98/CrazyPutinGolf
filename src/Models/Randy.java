package Models;

import Core.PhysicsEngine;
import Utils.Shot;
import javafx.geometry.Point2D;

import java.util.Random;

public class Randy extends Bot {

    public Randy(PhysicsEngine physicsEngine) {
        super(physicsEngine);
    }

    @Override
    public Shot go() {
        this.initialX = this.engine.getCurrentX();
        this.initialY = this.engine.getCurrentY();

        double maxVelocity = engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();
        double distance = 1;
        Shot current = null;
        int br = 0;

        while (distance > engine.getTerrainState().getToleranceRadius() * 5) {
            this.engine.setCurrentX(this.initialX);
            this.engine.setCurrentY(this.initialY);

            double xDirection = this.engine.getTerrainState().getGoal().getX();
            double yDirection = this.engine.getTerrainState().getGoal().getY();

            double velocityX = (rnd.nextDouble() - 0.5)*2 + xDirection;
            double velocityY = (rnd.nextDouble() - 0.5)*2 + yDirection;//* maxVelocity * 2 - maxVelocity;

            this.engine.takeVelocityOfShot(velocityX, velocityY);
            this.engine.executeShot();
            Point2D finalDestination = this.engine.finalDestination();

            distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
            current = new Shot(velocityX, velocityY, distance);
            br++;
        }

        System.out.println(br);
        return current;
    }
}