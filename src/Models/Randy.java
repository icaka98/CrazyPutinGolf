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
        while (distance > engine.getTerrainState().getToleranceRadius() * 10) {
            engine.setCurrentX(initialX);
            engine.setCurrentY(initialY);

            double velocityX = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;
            double velocityY = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;

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
