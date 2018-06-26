package Models;
import Utils.Constants;
import Core.Controller;

import java.awt.Point;
import javafx.geometry.Point2D;
import java.util.HashSet;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import java.util.List;
import java.awt.geom.Line2D;

public class Grid {

    private int width, height;
    private HashSet<Point> obstacles = new HashSet<>();
    private List<Rectangle> obs;
    private Controller control;
    //private Function f;

    private static Point[] dirs = {new Point(-1,1),new Point(0,1), new Point(1,1), new Point(-1,0),
                                   new Point(1,0), new Point(-1,-1), new Point(0,-1), new Point(1,-1)};
    //private static Point[] dirs = {new Point(0,1),new Point(0,-1), new Point(1,0), new Point(-1,0)};

    public Grid(Controller c){
        this.width = Constants.FIELD_WIDTH;
        this.height = Constants.FIELD_HEIGHT;
        this.control = c;
        obs = c.getCourse().getObstacles();

        /*for(int i = Constants.obstacle1[0]; i < Constants.obstacle1[1]; i++){

            for(int j = Constants.obstacle1[2]; j < Constants.obstacle1[3]; j++) {
                obstacles.add(new Point(i, j));
                //System.out.println("x = "+i+" y = "+j);
            }
        }*/
        System.out.println(obs.size());
        for (Rectangle o : obs){
            //System.out.println("HI");
            for(int i = (int) o.getX(); i < (int)o.getX() + o.getWidth(); i++){
                for(int j = (int)o.getY(); j < (int)o.getY() +o.getHeight(); j++) {
                    obstacles.add(new Point(i, j));
                    //System.out.println("x = "+i+" y = "+j);
                }
            }
        }

    }

    private boolean inBounds(Point id){
        return 0 <= id.x && id.x <= width && 0 <= id.y && id.y <= height;
    }

    private boolean passable(Point id){

        /*for(Line2D w: walls){
            if(w.contains(id.x,id.y)){
                return false;
            }
        }*/
        if(control.solve((id.x - Constants.FIELD_WIDTH/2)/Constants.SCALAR, (id.y - Constants.FIELD_HEIGHT/2)/Constants.SCALAR) < 0){
                    return false;
        }
        for(int i = id.x-10; i < id.x+10; i++){
            for(int j = id.y-10; j < id.y+10;j++){
                if(obstacles.contains(new Point(i,j))){return false;}
                /*for(Rectangle o : obs){
                    if(o.contains(new Point2D(id.x, id.y))){
                        return false;
                    }
                }*/

                if(!inBounds(new Point(i,j))){
                    return false;
                }

            }
        }
        return true;
        /*if(!inBounds(id)){
            return false;
        }
        if(control.solve((id.x - Constants.FIELD_WIDTH/2)/Constants.SCALAR, (id.y - Constants.FIELD_HEIGHT/2)/Constants.SCALAR)<0){
            return false;
        }
        return !obstacles.contains(id);*/
    }

    public double cost(Point a, Point b){return 1;}

    public ArrayList<Point> neighbours(Point id){
        ArrayList<Point> neighbours = new ArrayList<>();

        for(Point p : dirs){
            //boolean passable = true;
            Point neighbour = new Point(id.x + p.x, id.y +p.y);

            /*for(int i = neighbour.x-10; i < neighbour.x+10; i++){
                for(int j = neighbour.y-10; j < neighbour.y+10;j++){
                    passable = passable(new Point(i,j));
                    if(control.solve((neighbour.x - Constants.FIELD_WIDTH/2)/Constants.SCALAR, (neighbour.y - Constants.FIELD_HEIGHT/2)/Constants.SCALAR) < 0){
                        passable = false;
                    }
                }
            }*/

            /*if(inBounds(neighbour) && passable(neighbour)){
                neighbours.add(neighbour);
            }*/
            //double height = control.solve((neighbour.x - Constants.FIELD_WIDTH/2)/Constants.SCALAR, (neighbour.y - Constants.FIELD_HEIGHT/2)/Constants.SCALAR);
            if(passable(neighbour)){
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }



}
