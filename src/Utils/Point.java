package Utils;

public class Point implements Comparable {
    private double velocityX;
    private double velocityY;
    private double distanceToGoal;

    public Point(double endX, double endY, double distanceToGoal) {
        this.velocityX = endX;
        this.velocityY = endY;
        this.distanceToGoal = distanceToGoal;
    }

    public double getDistanceToGoal() {
        return distanceToGoal;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public int compareTo(Object o) {

        Point other = (Point) o;
        if(this.distanceToGoal > other.distanceToGoal)
        {
            return 1;
        }
        else if(other.distanceToGoal > this.distanceToGoal){
            return -1;
        }
        else {
            return 0;
        }
    }
}
