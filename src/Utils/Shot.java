package Utils;

public class Shot implements Comparable {
    private double velocityX, velocityY;
    private double distanceToGoal;
    private boolean found;

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
        return this.found;
    }

    @Override
    public int compareTo(Object other) {
        return Double.compare(this.distanceToGoal, ((Shot)other).distanceToGoal);
    }
}
