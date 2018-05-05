package Models;

import Core.PhysicsEngine;

import Utils.Point;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Putin extends Bot{

    private ArrayList<Point> population;

    public Putin(PhysicsEngine physicsEngine) {
        super(physicsEngine);
        population = new ArrayList<>();
    }

    public Point go() {

        double maxVelocity = engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();

        double distance;
        Point current = null;
        for (int i = 0; i < 200; i++) {
            engine.setCurrentX(engine.getTerrainState().getStart().getX());
            engine.setCurrentY(engine.getTerrainState().getStart().getY());

            double velocityX = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;
            double velocityY = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;

            engine.takeVelocityOfShot(velocityX, velocityY);
            engine.executeShot();
            Point2D finalDestination = engine.finalDestination();

            distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
            current = new Point(velocityX, velocityY, distance);
            population.add(current);

        }

       Collections.sort(this.population);
        for (Point p: this.population)
        {
            System.out.println("distance: " + p.getDistanceToGoal() + " X: " + p.getVelocityX() + " Y: " + p.getVelocityY());
        }

        while (this.population.get(0).getDistanceToGoal() > engine.getTerrainState().getToleranceRadius()*5)
        {
            reproduce();
            System.out.println("distance: " + this.population.get(0).getDistanceToGoal()); //+ " X: " + this.population.get(0).getVelocityX() + " Y: " + this.population.get(0).getVelocityY());
            System.out.println("distance: " + this.population.get(1).getDistanceToGoal()); //+ " X: " + this.population.get(0).getVelocityX() + " Y: " + this.population.get(0).getVelocityY());
        }

        return population.get(0);
    }

    private void reproduce() {
        ArrayList<Point> newIndividuals = new ArrayList<>();

        Random rnd  = new Random();
        for (int i = 0; i < 7; i++) {
            Point current = this.population.get(i);
            for (int j = 0; j < 7; j++) {
                Point other = this.population.get(j);

                engine.setCurrentX(engine.getTerrainState().getStart().getX());
                engine.setCurrentY(engine.getTerrainState().getStart().getY());


                double velocityX = (current.getVelocityX() + other.getVelocityX())/2;
                double velocityY = (current.getVelocityY() + other.getVelocityY())/2;


                double change = rnd.nextDouble();
                if(change< 0.9)
                {
                    velocityX += change - 0.45;
                }

                change = rnd.nextDouble();
                if(change< 0.9)
                {
                    velocityY += change - 0.45;
                }

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
