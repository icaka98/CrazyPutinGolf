package Models;

import Core.Controller;
import Core.PhysicsEngine;
import Utils.Constants;
import Utils.Shot;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

/***
 * @author Mathieu Coenegracht
 * @author Zhecho Mitev
 * Bot that uses a combination of the pathfinding algorithm and the genetic algorithm
 *
 */

public class Alistair extends Bot {

    private Controller controller;
    private Grid grid;
    private ArrayList<Point> path;
    private ArrayList<Shot> population;
    private final double mutationRate = 0.4;
    private List<Rectangle> obs;

    public Alistair(PhysicsEngine physicsEngine, Controller c){
        super(physicsEngine);
        this.controller = c;
        grid = new Grid(controller);
        int startx = (int)controller.getStartX()+Constants.FIELD_WIDTH/2;
        int starty = (int)controller.getStartY()+ Constants.FIELD_WIDTH/2;
        //System.out.println("\nstartx="+startx+"\nstarty="+starty);

        Point start = new Point((int)controller.getStartX()+Constants.FIELD_WIDTH/2, (int)controller.getStartY()+ Constants.FIELD_HEIGHT/2);
        Point goal = new Point((int)controller.getFinishX()+Constants.FIELD_WIDTH/2, (int)controller.getFinishY()+Constants.FIELD_HEIGHT/2);
        System.out.println("Start = "+start);
        System.out.println("Goal = "+goal);
        path = AStar.search(grid, start, goal);
        Collections.reverse(path);
        obs = c.getCourse().getObstacles();

    }

    public Shot go(){

        this.population = new ArrayList<>();
        this.initialX = this.engine.getCurrentX();
        this.initialY = this.engine.getCurrentY();
        Point newGoal = new Point(path.get(path.size()-1));
        boolean blocked = false;

        //System.out.println("[initx = "+initialX+";inity = "+initialY+"]");

        for(Point p : path) {
            Line2D l = new Line2D.Double(this.initialX*Constants.SCALAR, this.initialY*Constants.SCALAR, p.x-Constants.FIELD_WIDTH/2, p.y-Constants.FIELD_HEIGHT/2);
            /*for (Line2D obstacle : Constants.LINES) {
                if (l.intersectsLine(obstacle)) {
                    blocked = true;
                }
            }*/
            for(Rectangle o: obs){
                Rectangle2D newRect = new Rectangle2D.Double((o.getX()-Constants.FIELD_WIDTH/2),(o.getY()-Constants.FIELD_HEIGHT/2),o.getWidth(),o.getHeight());
                if(l.intersects(newRect)){
                    blocked=true;
                }
            }

            if (!blocked) {
                newGoal= p;
                break;
            }
            blocked = false;
        }

        //System.out.println("newGoal="+newGoal);


        double xDirection = (newGoal.x-Constants.FIELD_WIDTH/2)/Constants.SCALAR;
        double yDirection = (newGoal.y-Constants.FIELD_HEIGHT/2)/Constants.SCALAR;

        //double goalX = this.engine.getTerrainState().getGoal().getX();
        //double goalY = this.engine.getTerrainState().getGoal().getY();
        //Shot goaltry = putinGo(goalX, goalY);
        //Shot othertry = putinGo(xDirection,yDirection);

        return putinGo(xDirection, yDirection);
    }

    private Shot putinGo(double xDir, double yDir) {
        double maxVelocity = this.engine.getTerrainState().getMaxVelocity();
        Random rnd = new Random();

        double distance;
        Point2D goal = new Point2D(xDir, yDir);
        //System.out.println(goal);
        Shot current = null;
        for (int i = 0; i < 250; i++) {
            this.engine.setCurrentX(this.initialX);
            this.engine.setCurrentY(this.initialY);

            double velocityX = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;
            double velocityY = rnd.nextDouble() * maxVelocity * 2 - maxVelocity;

            this.engine.takeVelocityOfShot(velocityX, velocityY);
            this.engine.executeShot();
            Point2D finalDestination = this.engine.finalDestination();

            distance = finalDestination.distance(goal);
            current = new Shot(velocityX, velocityY, distance);
            this.population.add(current);
        }
        Collections.sort(this.population);
        long startTime = System.nanoTime();
        long currentTime = System.nanoTime();
        while (this.population.get(0).getDistanceToGoal() > engine.getTerrainState().getToleranceRadius()*5
                && (currentTime-startTime) < 2000000000) {
            reproduce(goal);
            currentTime = System.nanoTime();
        }
        return population.get(0);
    }

    private void reproduce(Point2D goal) {
        ArrayList<Shot> newIndividuals = new ArrayList<>();

        Random rnd  = new Random();
        for (int i = 0; i < 8; i++) {
            Shot current = this.population.get(i);
            for (int j = 0; j < 8; j++) {
                Shot other = this.population.get(j);

                this.engine.setCurrentX(this.initialX);
                this.engine.setCurrentY(this.initialY);

                double velocityX = (current.getVelocityX() + other.getVelocityX())/2;
                double velocityY = (current.getVelocityY() + other.getVelocityY())/2;

                double change = rnd.nextDouble();
                if(change< this.mutationRate*2)
                    velocityX += (change - 0.5)*this.engine.getTerrainState().getMaxVelocity()*this.engine.getTerrainState().getToleranceRadius()*30;

                change = rnd.nextDouble();
                if(change< this.mutationRate*2)
                    velocityY += (change - 0.5)*this.engine.getTerrainState().getMaxVelocity()*this.engine.getTerrainState().getToleranceRadius()*30;

                this.engine.takeVelocityOfShot(velocityX, velocityY);
                this.engine.executeShot();
                Point2D finalDestination = this.engine.finalDestination();

                double distance = finalDestination.distance(goal);
                Shot p = new Shot(velocityX, velocityY, distance);
                newIndividuals.add(p);
            }
        }
        Collections.sort(newIndividuals);
        this.population = newIndividuals;
    }
}

        /*double distance = 1;
        Shot current = null;
        Random rnd = new Random();
        int executedShots = 0;
        double maxV = this.engine.getTerrainState().getMaxVelocity();*/

        /*while (distance > engine.getTerrainState().getToleranceRadius() * 5 && executedShots < 20000) {
            this.engine.setCurrentX(this.initialX);
            this.engine.setCurrentY(this.initialY);

            double velocityX = (rnd.nextDouble() - 0.5) * maxV+ xDirection;
            double velocityY = (rnd.nextDouble() - 0.5) * maxV+ yDirection;
            System.out.println("Vx = "+velocityX);
            System.out.println("Vy = "+velocityY);

            this.engine.takeVelocityOfShot(velocityX, velocityY);
            this.engine.executeShot();

            Point2D finalDestination = this.engine.finalDestination();

            distance = finalDestination.distance(new Point2D(xDirection, yDirection));
            current = new Shot(velocityX, velocityY, distance);
            executedShots++;
        }
        System.out.println(executedShots);
        return current;*/
        /*for (Shot p: this.population) {
            System.out.println("distance: " + p.getDistanceToGoal() + " X: " + p.getVelocityX() + " Y: " + p.getVelocityY());
        }*/
        /*(System.out.println("distance: " + this.population.get(0).getDistanceToGoal()); //+ " X: " + this.population.get(0).getVelocityX() + " Y: " + this.population.get(0).getVelocityY());
            System.out.println("distance: " + this.population.get(1).getDistanceToGoal()); //+ " X: " + this.population.get(0).getVelocityX() + " Y: " + this.population.get(0).getVelocityY());*/
