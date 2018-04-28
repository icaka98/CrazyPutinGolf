package Core;

import Models.Course;
import Models.PrecomputedModule;
import Models.Function;
import Models.Putin;
import Utils.Constants;
import Utils.CourseReader;
import Utils.Point;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * @author Hristo Minkov
 */
public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance;
    private int steps, precomputedStep;
    private boolean precomputedMode, animationRunning;

    private Line aiming;
    private Pane mainPane;
    private Circle ball, hole;
    private Line stopLine;

    private Button next, changeMode, courseDesigner;
    private Label modeState;

    private static double scalar = Constants.SCALAR;

    private PhysicsEngine physicsEngine;
    private Function functionEvaluator;
    private PrecomputedModule precomputedModule;

    private Putin putin;

    /**
     * Initializes all the variable fields of the class.
     */
    private void initVars() {
        this.steps = 0;
        this.precomputedStep = 0;
        this.precomputedMode = false;
        this.animationRunning = false;

        CourseReader courseReader = new CourseReader(new File(Constants.DEFAULT_COURSE_FILE));
        this.mainPane = new Pane();
        this.functionEvaluator = new Function(courseReader.getEquation());
        this.precomputedModule = new PrecomputedModule();

        Course course = courseReader.getCourse();

        this.startX = course.getStart().getX() * scalar;
        this.startY = course.getStart().getY() * scalar;

        this.finishX = course.getGoal().getX() * scalar;
        this.finishY = course.getGoal().getY() * scalar;
        this.tolerance = course.getToleranceRadius() * scalar * 10;

        this.physicsEngine = new PhysicsEngine();
        this.putin = new Putin(physicsEngine);
    }

    /**
     * Initializes all the graphic components of the class.
     */
    private void initComponents(){
        this.hole = new Circle(
                this.finishX + Constants.SCENE_WIDTH / 2,
                this.finishY + Constants.SCENE_HEIGHT / 2,
                this.tolerance, Color.BLACK);
        this.hole.setOpacity(.6);

        this.aiming = new Line(0, 0, 0, 0);
        this.aiming.setStrokeWidth(0.0);

        this.stopLine = new Line(
                 Constants.MID_LINE.getX1()+ Constants.SCENE_WIDTH / 2 - 10, Constants.MID_LINE.getY1() +Constants.SCENE_HEIGHT / 2,
                 Constants.MID_LINE.getX2()+ Constants.SCENE_WIDTH / 2 - 10,  Constants.MID_LINE.getY2() + Constants.SCENE_HEIGHT / 2);

        this.stopLine.setStrokeWidth(Constants.WALL_THICKNESS);

        this.ball = new Circle(
                this.startX + Constants.SCENE_WIDTH / 2,
                this.startY + Constants.SCENE_HEIGHT / 2,
                10, Color.WHITE);

        this.next = new Button("Next");
        this.next.setPrefSize(60, 30);
        this.next.setLayoutX(140.0);
        this.next.setLayoutY(10.0);
        this.next.setVisible(false);

        this.changeMode = new Button("Change mode");
        this.changeMode.setPrefSize(120, 30);
        this.changeMode.setLayoutX(10.0);
        this.changeMode.setLayoutY(10.0);

        this.modeState = new Label("Player mode");
        this.modeState.setLayoutX(20.0);
        this.modeState.setLayoutY(50.0);
        this.modeState.setTextFill(Color.WHITE);

        this.courseDesigner = new Button("Course designer");
        this.courseDesigner.setPrefSize(150, 30);
        this.courseDesigner.setLayoutX(210.0);
        this.courseDesigner.setLayoutY(10.0);

        this.mainPane.getChildren().add(this.aiming);
        this.mainPane.getChildren().add(this.ball);
        this.mainPane.getChildren().add(this.hole);
        this.mainPane.getChildren().add(this.changeMode);
        this.mainPane.getChildren().add(this.next);
        this.mainPane.getChildren().add(this.modeState);
        this.mainPane.getChildren().add(this.courseDesigner);
        this.mainPane.getChildren().add(this.stopLine);
    }

    /**
     * Draws the concrete course using points with particular colors.
     * @param maxHeight the maximum height of the course function
     * @param minHeight the minimum height of the course function
     */
    private void drawField(double maxHeight, double minHeight){
        for(double x = -Constants.SCENE_WIDTH / 2; x < Constants.SCENE_WIDTH / 2; x += 3.5){
            for(double y = -Constants.SCENE_HEIGHT / 2; y < Constants.SCENE_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                Circle point = new Circle(x + Constants.SCENE_WIDTH / 2,
                        y + Constants.SCENE_HEIGHT / 2, 3, Color.GREEN);

                if(height < 0.0)
                    point.setFill(
                            Color.rgb(0,0, (int)(255*(1.0 - height/minHeight))));
                else{
                    if(Constants.SCENE_WIDTH / 2 - x < Constants.WALL_THICKNESS
                            || x < -Constants.SCENE_WIDTH / 2 + Constants.WALL_THICKNESS
                            || y < -Constants.SCENE_HEIGHT / 2 + Constants.WALL_THICKNESS
                            || Constants.SCENE_HEIGHT / 2 - y < Constants.WALL_THICKNESS){
                        point.setFill(Color.valueOf("#ECD540"));
                    }else point.setFill(
                            Color.rgb(0,105 + (int)(130.0*(height/maxHeight)),0));
                }

                this.mainPane.getChildren().add(point);
            }
        }

        for(double x = -Constants.SCENE_WIDTH / 2; x < Constants.SCENE_WIDTH / 2; x += 3.5){
            for(double y = -Constants.SCENE_HEIGHT / 2; y < Constants.SCENE_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                if(x + Constants.SCENE_WIDTH / 2 - 10 <= 4.5){
                    Circle fence = new Circle(x, y, 3, Color.BLACK);
                    this.mainPane.getChildren().add(fence);
                }
            }
        }
    }

    /**
     * Creates a specific transition corresponding to a given move
     * @param moves the collection of moves that should be executed
     * @param idx the current index of the moves showing the transition state
     * @return the newly created transition
     * @see PathTransition
     */
    private PathTransition createNextTransition(List<Point2D> moves, int idx){
        Point2D move = moves.get(idx);

        Line path = new Line(
                this.ball.getCenterX(),
                this.ball.getCenterY(),
                (move.getX()) * scalar + Constants.SCENE_WIDTH / 2,
                (move.getY()) * scalar + Constants.SCENE_HEIGHT / 2);

        double endX = path.getEndX();
        double endY = path.getEndY();

        PathTransition transition = new PathTransition();
        transition.setNode(this.ball);
        transition.setDuration(Duration.millis(Constants.TRANSITION_DURATION));
        transition.setPath(path);
        transition.setCycleCount(1);

        this.ball.setCenterX(endX);
        this.ball.setCenterY(endY);

        return transition;
    }

    /**
     * Add all transitions to a sequential transition and execute the main transition
     * @param moves a list with all the velocities to be processed
     * @see SequentialTransition
     */
    private void executeTransitions(List<Point2D> moves){
        SequentialTransition sequentialTransition = new SequentialTransition();

        for(int i=0;i<moves.size();i++)
            sequentialTransition.getChildren().add(this.createNextTransition(moves, i));

        sequentialTransition.setOnFinished( e -> {
            this.animationRunning = false;
            this.changeMode.setDisable(false);
            this.next.setDisable(false);

            if(Math.sqrt((this.hole.getCenterX() - this.ball.getCenterX())
                    * (this.hole.getCenterX() - this.ball.getCenterX())
                    + (this.hole.getCenterY() - this.ball.getCenterY())
                    * (this.hole.getCenterY() - this.ball.getCenterY()))
                    <= this.hole.getRadius()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText(null);
                alert.setContentText("You managed to score in " + this.steps + " steps.");

                alert.show();
            }
        });

        this.animationRunning = true;
        this.changeMode.setDisable(true);
        this.next.setDisable(true);
        sequentialTransition.play();
    }

    /**
     * Executing a shot in the Physics engine
     * @param cenX the center X of the ball
     * @param cenY the center Y of the ball
     * @param aimX the X coordinate of the selected velocity
     * @param aimY the Y coordinate of the selected velocity
     * @return collection of all moves that have to be processed
     * @see PhysicsEngine
     */
    private List<Point2D> prepareEngine(double cenX, double cenY, double aimX, double aimY){

        //UNCOMMENT TO ACTIVATE THE BOT
       //Point p = putin.go();
       //aimX = p.getVelocityX();
       //aimY = p.getVelocityY();

        this.physicsEngine.setCurrentX(cenX);
        this.physicsEngine.setCurrentY(cenY);

        this.physicsEngine.takeVelocityOfShot(aimX, aimY);
        this.physicsEngine.executeShot();

        return this.physicsEngine.getCoordinatesOfPath();
    }

    /**
     * The core method of the application - everything starts here
     * @param primaryStage the main Stage to which components are attached
     * @see Stage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Constants.STAGE_TITLE);

        this.initVars();

        double maxHeight = this.functionEvaluator.solve(Constants.SCENE_WIDTH / 2.0 / scalar,
                Constants.SCENE_HEIGHT / 2.0 / scalar);

        double minHeight = this.functionEvaluator.solve(- 1.66666667, - Constants.SCENE_HEIGHT / 2.0 / scalar);
        minHeight = Math.min(minHeight, this.functionEvaluator.solve(- Constants.SCENE_WIDTH / 2 / scalar,
                - Constants.SCENE_HEIGHT / 2.0 / scalar));

        this.drawField(maxHeight, minHeight);
        this.initComponents();

        this.ball.setOnMouseDragged(event -> {
            if(this.precomputedMode) return;
            if(this.animationRunning) return;

            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            Circle src = (Circle)(event.getSource());
            aiming.setStartX(src.getCenterX());
            aiming.setStartY(src.getCenterY());
            aiming.setEndX(src.getCenterX() + (src.getCenterX() - mouseX));
            aiming.setEndY(src.getCenterY() + (src.getCenterY() - mouseY));

            aiming.setStrokeWidth(6.9);
            aiming.setFill(Color.ORANGE);
        });

        this.ball.setOnMouseReleased(event -> {
            if(this.precomputedMode) return;
            if(this.animationRunning) return;

            this.steps++;

            double aimX = (aiming.getEndX() - Constants.SCENE_WIDTH / 2) / scalar;
            double aimY = (aiming.getEndY() - Constants.SCENE_HEIGHT / 2) / scalar;

            double cenX = (this.ball.getCenterX() - Constants.SCENE_WIDTH / 2) / scalar;
            double cenY = (this.ball.getCenterY() - Constants.SCENE_HEIGHT / 2) / scalar;

            List<Point2D> moves = this.prepareEngine(cenX, cenY, aimX, aimY);
            System.out.println("LEN: " + moves.size());

            aiming.setEndY(0);
            aiming.setEndX(0);
            aiming.setStartY(0);
            aiming.setStartX(0);
            aiming.setStrokeWidth(0.0);

            this.executeTransitions(moves);
        });

        this.next.setOnAction(e -> {
            if(this.animationRunning) return;

            if(!this.precomputedMode
                    || this.precomputedStep >= this.precomputedModule.getVelocities().size()) return;

            Point2D nextMove = this.precomputedModule.getVelocities().get(this.precomputedStep++);
            this.steps++;

            double cenX = (this.ball.getCenterX() - Constants.SCENE_WIDTH / 2) / scalar;
            double cenY = (this.ball.getCenterY() - Constants.SCENE_HEIGHT / 2) / scalar;

            System.out.printf("%f %f %f %f\n", cenX, cenY, nextMove.getX(), nextMove.getY());

            List<Point2D> moves = this.prepareEngine(cenX, cenY, nextMove.getX(), nextMove.getY());
            System.out.println("LEN: " + moves.size());

            this.executeTransitions(moves);
        });

        this.changeMode.setOnAction(e -> {
            this.precomputedMode = !this.precomputedMode;
            this.next.setVisible(this.precomputedMode);
            this.modeState.setText(this.precomputedMode ? "Precomputed mode" : "Player mode");
        });

        this.courseDesigner.setOnAction(e -> {
            if(animationRunning) return;

            CourseDesigner.run();
        });

        Scene mainScene = new Scene(this.mainPane,
                Constants.SCENE_WIDTH,
                Constants.SCENE_HEIGHT);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Main method of the application.
     * @param args arguments may pass to the application when starting
     */
    public static void main(String[] args) {
        launch(args);
    }
}
