package Utils;

public class Shot implements Comparable {
    private double velocityX;
    private double velocityY;

    private boolean found;

    private double distanceToGoal;

    public Shot(double endX, double endY, double distanceToGoal) {
        this.velocityX = endX;
        this.velocityY = endY;
        this.distanceToGoal = distanceToGoal;
        this.found = false;
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


    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isFound() {

        return found;
    }

    @Override
    public int compareTo(Object o) {

        Shot other = (Shot) o;
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
