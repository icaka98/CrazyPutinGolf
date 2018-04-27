package Models;

import Core.PhysicsEngine;

import Utils.Point;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Putin {

    private PhysicsEngine engine;

    private ArrayList<Point> population;

    public Putin(PhysicsEngine physicsEngine) {
        engine = physicsEngine;
        population = new ArrayList<>();

    }

    public Point go()
    {
        double maxVelocity = engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();
        for (int i = 0; i < 300; i++) {
            engine.setCurrentX(engine.getTerrainState().getStart().getX());
            engine.setCurrentY(engine.getTerrainState().getStart().getY());

            double velocityX = rnd.nextDouble()*maxVelocity*2 - maxVelocity;
            double velocityY = rnd.nextDouble()*maxVelocity*2 - maxVelocity;

            engine.takeVelocityOfShot(velocityX, velocityY);
            engine.executeShot();
            Point2D finalDestination  = engine.finalDestination();

            double distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
            Point current = new Point(velocityX, velocityY, distance);
            population.add(current);
        }

        Collections.sort(this.population);
        for (Point p: this.population)
        {
            System.out.println(p.getDistanceToGoal());
        }

        while (this.population.get(0).getDistanceToGoal() > 0.1)
        {
            reproduce();
        }

        return population.get(0);
    }

    private void reproduce() {
        ArrayList<Point> newIndividuals = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Point current = this.population.get(i);
            for (int j = 0; j < 6; j++) {
                Point other = this.population.get(j);

                engine.setCurrentX(engine.getTerrainState().getStart().getX());
                engine.setCurrentY(engine.getTerrainState().getStart().getY());

                double velocityX = (current.getVelocityX() + other.getVelocityX())/2;
                double velocityY = (current.getVelocityY() + other.getVelocityY())/2;

                engine.takeVelocityOfShot(velocityX, velocityY);
                engine.executeShot();
                Point2D finalDestination  = engine.finalDestination();

                double distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
                Point p = new Point(velocityX, velocityY, distance);
                newIndividuals.add(p);
            }
        }

        Collections.sort(newIndividuals);
        this.population = newIndividuals;
    }
}