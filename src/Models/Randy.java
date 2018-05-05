package Models;

import Core.PhysicsEngine;
import Utils.Point;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class Randy extends Bot {


    public Randy(PhysicsEngine physicsEngine) {
        super(physicsEngine);
    }
    @Override
    public Point go() {
        double maxVelocity = engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();
        double distance = 1;
        Point current = null;
        int br = 0;
        while (distance > engine.getTerrainState().getToleranceRadius() * 5) {
            engine.setCurrentX(engine.getTerrainState().getStart().getX());
            engine.setCurrentY(engine.getTerrainState().getStart().getY());

            double velocityX = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;
            double velocityY = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;

            engine.takeVelocityOfShot(velocityX, velocityY);
            engine.executeShot();
            Point2D finalDestination = engine.finalDestination();

            distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
            current = new Point(velocityX, velocityY, distance);
            br++;
        }

        System.out.println(br);
        return current;

    }
}
