import java.awt.geom.Point2D;

public class ShortMove {
    private Point2D finalPoint;
    private double velocity;

    public ShortMove(double finalX, double finalY, double velocity){
        this.finalPoint = new Point2D.Double(finalX, finalY);
        this.setVelocity(velocity);
    }

    public double getFinalX() { return this.finalPoint.getX(); }

    public double getFinalY() { return this.finalPoint.getY(); }

    public double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
