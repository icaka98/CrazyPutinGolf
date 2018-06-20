package Models;
import java.util.Collections;
import java.util.HashMap;
import java.awt.Point;
import java.util.PriorityQueue;

import javafx.geometry.Point3D;
import javafx.util.Pair;
import java.util.ArrayList;

public class AStar {

    public static ArrayList<Point> search(Grid graph, Point start, Point goal){
        HashMap<Point, Point> cameFrom = new HashMap<Point,Point>();
        HashMap<Point, Double> costSoFar = new HashMap<Point, Double>();


        PriorityQueue<Pair<Point,Double>> pq = new PriorityQueue<Pair<Point,Double>>((v,o) -> Double.compare(v.getValue(), o.getValue()));
        costSoFar.put(start,0.0);
        cameFrom.put(start,start);

        pq.add(new Pair(start,0.0));

        while(!pq.isEmpty()){
            Point current = pq.poll().getKey();

            if(current.equals(goal)) {
                System.out.println("Goal reached");
                break;
            }

            for(Point next : graph.neighbours(current)){
                double newCost = costSoFar.get(current) + graph.cost(current, next);

                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {

                    costSoFar.put(next,newCost);
                    double priority = newCost + heuristic(next, goal);
                    pq.add(new Pair(next, priority));
                    cameFrom.put(next, current);
                }
            }
        }
        return calculatePath(cameFrom, start, goal);
    }

    private static ArrayList<Point> calculatePath(HashMap<Point,Point> cameFrom, Point start, Point goal){
        ArrayList<Point> path = new ArrayList<>();
        path.add(goal);

        while(!goal.equals(start)){
            //System.out.println("hi");
            goal = cameFrom.get(goal);
            path.add(goal);
        }
        Collections.reverse(path);
        return path;
    }

    private static double heuristic(Point a, Point b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
