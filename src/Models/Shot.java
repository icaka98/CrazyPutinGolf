package Models;

/**
 * @author Zhecho Mitev
 */
public class Shot implements Comparable {
    private double velocityX, velocityY;
    private double distanceToGoal;
    private boolean found;

    public Shot(double endX, double endY, double distanceToGoal) {
        this.setVelocityX(endX);
        this.setVelocityY(endY);
        this.setDistanceToGoal(distanceToGoal);
        this.setFound(false);
    }

    public double getDistanceToGoal() {
        return this.distanceToGoal;
    }

    private void setDistanceToGoal(double distanceToGoal) {
        this.distanceToGoal = distanceToGoal;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    private void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    private void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    private void setFound(boolean found) {
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
