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
        initialX = engine.getCurrentX();
        initialY = engine.getCurrentY();

        double maxVelocity = engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();
        double distance = 1;
        Shot current = null;
        int br = 0;
        while (distance > engine.getTerrainState().getToleranceRadius() * 5) {
            engine.setCurrentX(initialX);
            engine.setCurrentY(initialY);

            double xDirection = engine.getTerrainState().getGoal().getX();
            double yDirection = engine.getTerrainState().getGoal().getY();

            double velocityX = (rnd.nextDouble() - 0.5)*2 + xDirection;
            double velocityY = (rnd.nextDouble() - 0.5)*2 + yDirection;//* maxVelocity * 2 - maxVelocity;

            engine.takeVelocityOfShot(velocityX, velocityY);
            engine.executeShot();
            Point2D finalDestination = engine.finalDestination();

            distance = finalDestination.distance(engine.getTerrainState().getGoal());
            current = new Shot(velocityX, velocityY, distance);
            br++;
        }

        System.out.println(br);
        return current;

    }
}