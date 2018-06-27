package Models.Bots;

import Core.Physics.PhysicsEngine;
import Models.Shot;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author Zhecho Mitev
 * Genetic algorithm
 **/
public class Putin extends Bot {
    private ArrayList<Shot> population;

    private final double mutationRate = 0.1; //10%

    public Putin(PhysicsEngine physicsEngine) {
        super(physicsEngine);
        this.population = new ArrayList<>();
    }

    /**
     * Executes the main logic of the genetic algorithm
     * @return the best shot that has been found by the algorithm
     */
    public Shot go() {
        double maxVelocity = this.engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();

        this.initialX = this.engine.getCurrentX();
        this.initialY = this.engine.getCurrentY();

        double distance;
        Shot current = null;
        for (int i = 0; i < 250; i++) { // creating the first population
            this.engine.setCurrentX(this.initialX);
            this.engine.setCurrentY(this.initialY);

            double velocityX = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;
            double velocityY = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;

            this.engine.takeVelocityOfShot(velocityX, velocityY);
            this.engine.executeShot();
            Point2D finalDestination = this.engine.finalDestination();

            distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
            current = new Shot(velocityX, velocityY, distance);
            this.population.add(current);

        }

        Collections.sort(this.population);

        long startTime = System.nanoTime();
        long currentTime = System.nanoTime();
        while (this.population.get(0).getDistanceToGoal() > engine.getTerrainState().getToleranceRadius()*10 &&
                (currentTime-startTime) < 2000000000){//searches for a good shot, works for no more than 2 secs

            reproduce();

            currentTime = System.nanoTime();
        }
        return this.population.get(0);
    }

    /**
     * Crossing-over the first 12 individuals and creating the new population.
     */
    private void reproduce() {
        ArrayList<Shot> newIndividuals = new ArrayList<>();

        Random rnd  = new Random();
        for (int i = 0; i < 12; i++) {
            Shot current = this.population.get(i);
            for (int j = 0; j < 12; j++) {
                Shot other = this.population.get(j);

                this.engine.setCurrentX(this.initialX);
                this.engine.setCurrentY(this.initialY);

                double velocityX = (current.getVelocityX() + 3*other.getVelocityX())/4;
                double velocityY = (current.getVelocityY() + 3*other.getVelocityY())/4;

                double rate = rnd.nextDouble();
                if(rate < this.mutationRate)
                {
                    velocityX = velocityX + (rate - 0.5)*this.engine.getTerrainState().getMaxVelocity()*4;
                    rate = rnd.nextDouble();
                    velocityY = velocityY + (rate - 0.5)*this.engine.getTerrainState().getMaxVelocity()*4;
                }

                this.engine.takeVelocityOfShot(velocityX, velocityY);
                this.engine.executeShot();
                Point2D finalDestination = this.engine.finalDestination();

                double distance = finalDestination.distance(this.engine.getTerrainState().getGoal());
                Shot p = new Shot(velocityX, velocityY, distance);
                newIndividuals.add(p);
            }
        }

        Collections.sort(newIndividuals);//sorting the individuals by their fitness function
        this.population = newIndividuals;
    }
}