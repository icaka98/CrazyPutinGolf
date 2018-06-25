package Core;

import Models.*;
import Utils.Constants;
import Utils.Shot;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private static final String COURSE_CODE = "1";

    private double amplification;

    private double startX, startY, finishX, finishY, tolerance, maxHeight, minHeight;
    private int steps, precomputedStep;
    private boolean precomputedMode, animationRunning;
    private Course course;
    private Function functionEvaluator;
    private PrecomputedModule precomputedModule;

    private static double scalar = Constants.SCALAR;

    private Bot bot;

    private PhysicsEngine physicsEngine;

    private static Main twoDimensionalScreen;
    private static Main3D threeDimensional;

    public Controller() {
        initKeyVars();
        init2DVars();
        init3DVars();
    }

    public void initKeyVars(){
        twoDimensionalScreen = new Main(this);
        threeDimensional = new Main3D(this);

    }

    /**
     * Initializes all the variable fields of the class.
     */
    public void init2DVars() {

        this.steps = 0;
        this.precomputedStep = 0;
        this.precomputedMode = false;
        this.animationRunning = false;

        this.course = new Course(COURSE_CODE);
        //this.mainPane = new Pane();
        this.functionEvaluator = new Function(this.course.getEquation());
        this.precomputedModule = new PrecomputedModule();

        this.startX = this.course.getStart().getX() * scalar;
        this.startY = this.course.getStart().getY() * scalar;

        this.finishX = this.course.getGoal().getX() * scalar;
        this.finishY = this.course.getGoal().getY() * scalar;
        this.tolerance = this.course.getToleranceRadius() * scalar * 10;

        this.physicsEngine = new PhysicsEngine(COURSE_CODE);
        this.bot = new Alistair(this.physicsEngine, this);
        //this.bot = new Putin(this.physicsEngine);

        /*Grid gr = new Grid(Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT, Constants.obstacle1);
        Point s = new Point(250, 260);
        Point g = new Point(250, 400);

        path = AStar.search(gr, s, g);*/
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getFinishX() {
        return finishX;
    }

    public double getFinishY() {
        return finishY;
    }

    public double getTolerance() {
        return tolerance;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public boolean isPrecomputedMode() {

        return precomputedMode;
    }

    public void changeMode()
    {
        this.precomputedMode = !this.precomputedMode;

    }

    public int getSteps() {
        return steps;
    }

    public void increaseStepBy1() {
        this.steps++;
    }

    public void setAnimationRunning(boolean animationRunning) {

        this.animationRunning = animationRunning;
    }

    public void draw2D()
    {
        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                Circle point = new Circle(x + Constants.FIELD_WIDTH / 2,
                        y + Constants.FIELD_HEIGHT / 2, 3, javafx.scene.paint.Color.GREEN);
                //System.out.println("Height: " + height);
                //System.out.println("MinHeight " + minHeight);
                //System.out.println("MaxHeight " + maxHeight);
                //int blueRatio = (int) (255*(1.0 - height/Function.minHeight));
                int blueRatio = (int) (255*(1.0 + height/minHeight));
                if (blueRatio < 0)  blueRatio = 0;
                if (blueRatio > 255)  blueRatio = 255;
                //int greenRatio = 105 + (int)(130.0*(height/Function.maxHeight));
                int greenRatio = 105 + (int)(130.0*(height/(maxHeight*2)));
                if (greenRatio < 0)  greenRatio = 0;
                if (greenRatio > 255)  greenRatio = 255;
                if(height < 0.0)
                    point.setFill(
                            javafx.scene.paint.Color.rgb(0,0, blueRatio));
                else{
                    if(Constants.FIELD_WIDTH / 2 - x < Constants.WALL_THICKNESS
                            || x < -Constants.FIELD_WIDTH / 2 + Constants.WALL_THICKNESS
                            || y < -Constants.FIELD_HEIGHT / 2 + Constants.WALL_THICKNESS
                            || Constants.FIELD_HEIGHT / 2 - y < Constants.WALL_THICKNESS){
                        point.setFill(javafx.scene.paint.Color.valueOf("#ECD540"));
                    }else point.setFill(

                            javafx.scene.paint.Color.rgb(0,greenRatio,0));
                }

                twoDimensionalScreen.getMainPane().getChildren().add(point);
            }
        }

        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                if(x + Constants.FIELD_WIDTH / 2 - 10 <= 4.5){
                    Circle fence = new Circle(x, y, 3, Color.BLACK);
                    twoDimensionalScreen.getMainPane().getChildren().add(fence);
                }
            }
        }
    }

    public void calculateMinMax(){
        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){
                double height = this.functionEvaluator.solve(x / scalar, y / scalar);
                this.maxHeight = Math.max(this.maxHeight, height);
                this.minHeight = Math.min(this.minHeight, height);
            }
        }
    }
    public Shot getBotShot() {

        return this.bot.go();
    }

    public int getPrecomputedStep() {
        return this.precomputedStep;
    }

    public PrecomputedModule getPrecomputedModule() {
        return this.precomputedModule;
    }

    public Point2D getNextMove() {
        return this.precomputedModule.getVelocities().get(this.precomputedStep++);
    }


    public Course getCourse() {
        return course;
    }

    /**
     * Executing a shot in the Physics engine
     * @param aimX the X coordinate of the selected velocity
     * @param aimY the Y coordinate of the selected velocity
     * @return collection of all moves that have to be processed
     * @see PhysicsEngine
     */
    public List<Point2D> prepare2DEngine(double aimX, double aimY){

        double cenX = (twoDimensionalScreen.getBall().getCenterX() - Constants.FIELD_WIDTH / 2) / scalar;
        double cenY = (twoDimensionalScreen.getBall().getCenterY() - Constants.FIELD_HEIGHT / 2) / scalar;

        this.physicsEngine.setCurrentX(cenX);

        this.physicsEngine.setCurrentY(cenY);

        this.physicsEngine.takeVelocityOfShot(aimX, aimY);
        this.physicsEngine.executeShot();

        return this.physicsEngine.getCoordinatesOfPath();
    }

    public double solve(double x, double y){
        return functionEvaluator.solve(x, y);
    }

    public void checkFunctionBounds(){
        for (double x = -2.50; x < 2.50; x+=0.01) {
            for (double y = -2.50; y < 2.50; y+=0.01) {
                this.maxHeight = Math.max(solve(x, y), this.maxHeight);
                this.minHeight = Math.min(solve(x, y), this.minHeight);
            }
        }
    }
    
    public void init3DVars() {
        this.maxHeight = this.maxHeight * Constants.SCALAR;

        this.amplification = (float) -1;//(-250.0f / this.maxHeight);
    }
    


    public double getAmplification() {
        return amplification;
    }

    public List<Point3D> prepare3DEngine(double aimX, double aimY){

        double cenX = (threeDimensional.getBall().getTranslateX()) / Constants.SCALAR;
        double cenY = (threeDimensional.getBall().getTranslateZ()) / Constants.SCALAR;

        aimX /= Constants.SCALAR;
        aimY /= Constants.SCALAR;

        this.physicsEngine.setCurrentX(cenX);
        this.physicsEngine.setCurrentY(cenY);

        this.physicsEngine.takeVelocityOfShot(aimX, aimY);
        this.physicsEngine.executeShot();

        return this.physicsEngine.getCoordinatesOfPath().subList(0,
                this.physicsEngine.getCoordinatesOfPath().size()-1).stream()
                .map(m -> new Point3D(m.getX(),
                        this.solve(m.getX(), m.getY()) * amplification,
                        m.getY()))
                .collect(Collectors.toList());
    }


    public static void start3D() throws Exception {
        threeDimensional.start(new Stage());
    }

    public static void startGame() throws Exception {
        twoDimensionalScreen.start(new Stage());
    }

    public void loadObstacles() {
        this.course.getObstacles().forEach(Main::addObstacle);
    }
}
