package Models;
import java.awt.Point;
import java.util.HashSet;
import java.util.ArrayList;
import java.awt.geom.Line2D;

public class Grid {

    private int width, height;
    private HashSet<Point> obstacles;
    private ArrayList<Line2D> walls;

    private static Point[] dirs = {new Point(-1,1),new Point(0,1), new Point(1,1), new Point(-1,0),
                                   new Point(1,0), new Point(-1,-1), new Point(0,-1), new Point(1,-1)};

    public Grid(int width, int height, ArrayList<Line2D> walls){
        this.width = width;
        this.height = height;
        this.walls = walls;

    }

    private boolean inBounds(Point id){
        return 0 <= id.x && id.x <= width && 0 <= id.y && id.y <= height;
    }

    private boolean passable(Point id){

        for(Line2D w: walls){
            if(w.contains(id.x,id.y)){
                return false;
            }
        }
        return !obstacles.contains(id);
    }

    public double cost(Point a, Point b){return 1;}

    public ArrayList<Point> neighbours(Point id){
        ArrayList<Point> neighbours = new ArrayList<>();

        for(Point p : dirs){
            Point neighbour = new Point(id.x + p.x, id.y +p.y);
            if(inBounds(neighbour) && passable(neighbour)){
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }



}
