package Core;

import Models.*;
import Utils.Constants;
import Utils.Shot;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hristo Minkov
 */
public class Main extends Application {

    private static double scalar = Constants.SCALAR;

    private Line aiming;
    private static Pane mainPane;
    private Circle ball, hole;
    private Line stopLine;

    private Button next, changeMode, courseDesigner, enableBot, restartBtn;
    private Label modeState, functionLabel, positionLabel, titleLabel, goalLabel;
    private VBox infoBox;

    private Stage mainStage;

    private Controller controller;

    public Main(Controller controller) {
        this.controller = controller;
    }

    /**
     * Initializes all the variable fields of the class.
     */
    private void initVars() { this.mainPane = new Pane(); }

    public Pane getMainPane() {
        return mainPane;
    }

    /**
     * Initializes all the graphic components of the class.
     */
    private void initComponents(){
        this.hole = ComponentFactory.getHole(this.controller.getFinishX(), this.controller.getFinishY(), this.controller.getTolerance());
        this.aiming = ComponentFactory.getAiming();
        this.stopLine = ComponentFactory.getStopWall();
        this.ball = ComponentFactory.getBall(this.controller.getStartX(), this.controller.getStartY());

        this.next = ComponentFactory.getButton("Next", 100, 30, 575, 315);
        this.next.setVisible(false);

        this.changeMode = ComponentFactory.getButton("Change mode", 160, 40, 545, 270);
        this.restartBtn = ComponentFactory.getButton("Restart", 160, 40, 545, 440);
        this.courseDesigner = ComponentFactory.getButton("Course designer", 160, 40, 545, 370);
        this.enableBot = ComponentFactory.getButton("Bot Shot", 160, 40, 545, 200);

        this.modeState = ComponentFactory.getLabel("Mode: Player mode", 510, 180);
        this.functionLabel = ComponentFactory.getLabel(this.getFunctionInfo(), 510, 90);
        this.positionLabel = ComponentFactory.getLabel(this.getBallInfo(), 510, 110);
        this.goalLabel = ComponentFactory.getLabel(this.getGoalInfo(), 510, 150);

        this.titleLabel = ComponentFactory.getLabel("Puting Golf", 545, 10);
        this.titleLabel.setTextFill(Color.GREEN);
        this.titleLabel.setEffect(ComponentFactory.getDropShadow());
        this.titleLabel.setFont(Font.font(null, FontWeight.BOLD, 32));

        this.infoBox = ComponentFactory.getVBox();
        this.infoBox.getChildren().addAll(this.modeState, this.goalLabel, this.positionLabel, this.functionLabel);

        this.attachAllComponents();
    }

    public static void addObstacle(Rectangle obstacle){
        mainPane.getChildren().add(obstacle);
    }

    private void attachAllComponents() {
        this.mainPane.getChildren().add(this.aiming);
        this.mainPane.getChildren().add(this.ball);
        this.mainPane.getChildren().add(this.hole);
        this.mainPane.getChildren().add(this.changeMode);
        this.mainPane.getChildren().add(this.enableBot);
        this.mainPane.getChildren().add(this.next);
        this.mainPane.getChildren().add(this.courseDesigner);
        //this.mainPane.getChildren().add(this.stopLine);
        this.mainPane.getChildren().add(this.restartBtn);
        this.mainPane.getChildren().add(this.titleLabel);
        this.mainPane.getChildren().add(this.infoBox);
    }

    /**
     * Draws the concrete course using points with particular colors.
     */
    private void drawField(){
        this.controller.draw2D();
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
                (move.getX()) * scalar + Constants.FIELD_WIDTH / 2,
                (move.getY()) * scalar + Constants.FIELD_HEIGHT / 2);

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
            controller.setAnimationRunning(false);

            this.changeMode.setDisable(false);
            this.enableBot.setDisable(false);
            this.next.setDisable(false);

            if(Math.sqrt((this.hole.getCenterX() - this.ball.getCenterX())
                    * (this.hole.getCenterX() - this.ball.getCenterX())
                    + (this.hole.getCenterY() - this.ball.getCenterY())
                    * (this.hole.getCenterY() - this.ball.getCenterY()))
                    <= this.hole.getRadius()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText(null);

                if(controller.getSteps() == 0) alert.setContentText("The bot scored hole-in-one!");
                else alert.setContentText("You scored in " + controller.getSteps() + " steps.");

                alert.show();
            }
        });

        controller.setAnimationRunning(true);

        this.changeMode.setDisable(true);
        this.enableBot.setDisable(true);
        this.next.setDisable(true);
        sequentialTransition.play();
    }

    public Circle getBall() {
        return ball;
    }

    private void restart(){
        this.mainStage.close();
        start(this.mainStage);
    }

    private String getBallInfo(){
        return "Ball position: (" + this.ball.getCenterX() +" , " + this.ball.getCenterY() + ")";
    }

    private String getGoalInfo(){
        return "Goal position: (" +
                (this.hole.getCenterX() + "").substring(0, 5) +
                " , " +
                (this.hole.getCenterY() + "").substring(0, 5) +
                ")";
    }

    private String getFunctionInfo(){
        return "Function: " + controller.getCourse().getCompactEquation();
    }

    /**
     * The core method of the application - everything starts here
     * @param primaryStage the main Stage to which components are attached
     * @see Stage
     */
    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        primaryStage.setTitle(Constants.STAGE_TITLE);

        this.initVars();

        controller.setMaxHeight(Double.MIN_VALUE) ;
        controller.setMinHeight(Double.MAX_VALUE);

        controller.calculateMinMax();

        this.drawField();
        this.controller.loadObstacles();
        this.initComponents();

        this.restartBtn.setOnAction(e -> {
            this.restart();
        });

        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(200),
                        event -> {
                            String ballX = (this.ball.getCenterX() + "").substring(0, 5);
                            String ballY = (this.ball.getCenterY() + "").substring(0, 5);
                            this.positionLabel.setText(
                                    "Ball position: (" + ballX + " , " + ballY + ")");
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        this.ball.setOnMouseDragged(event -> {
            if(controller.isPrecomputedMode()) return;
            if(controller.isAnimationRunning()) return;

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
            if(controller.isPrecomputedMode()) return;
            if(controller.isAnimationRunning()) return;

            controller.increaseStepBy1();

            double aimX = (aiming.getEndX() - Constants.FIELD_WIDTH / 2) / scalar;
            double aimY = (aiming.getEndY() - Constants.FIELD_HEIGHT / 2) / scalar;

            List<Point2D> moves = controller.prepare2DEngine(aimX, aimY);
            System.out.println("LEN: " + moves.size());

            aiming.setEndY(0);
            aiming.setEndX(0);
            aiming.setStartY(0);
            aiming.setStartX(0);
            aiming.setStrokeWidth(0.0);

            this.executeTransitions(moves);
        });

        this.next.setOnAction(e -> {
            if(controller.isAnimationRunning())
                return;

            if(!controller.isPrecomputedMode() ||
                    controller.getPrecomputedStep() >= controller.getPrecomputedModule().getVelocities().size())
                return;


            Point2D nextMove =  controller.getNextMove();

            System.out.printf("%f %f\n", nextMove.getX(), nextMove.getY());

            List<Point2D> moves = controller.prepare2DEngine(nextMove.getX(), nextMove.getY());
            System.out.println("LEN: " + moves.size());

            this.executeTransitions(moves);

            controller.increaseStepBy1();
        });

        this.changeMode.setOnAction(e -> {
            controller.changeMode();
            this.next.setVisible(controller.isPrecomputedMode());
            this.modeState.setText(controller.isPrecomputedMode() ? "Precomputed mode" : "Player mode");
        });

        this.enableBot.setOnAction(e -> {

            Shot shot = controller.getBotShot();
            double aimX = shot.getVelocityX();
            double aimY = shot.getVelocityY();


            this.aiming.setEndX((aimX * scalar + Constants.FIELD_WIDTH / 2));
            this.aiming.setEndY((aimY * scalar + Constants.FIELD_HEIGHT / 2));
            this.aiming.setStartX(this.ball.getCenterX());
            this.aiming.setStartY(this.ball.getCenterY());
            this.aiming.setStrokeWidth(6.9f);

            List<Point2D> moves = controller.prepare2DEngine(aimX, aimY);
            System.out.println("LEN: " + moves.size());

            Timer timer = new Timer(666, arg0 -> {
                this.executeTransitions(moves);
                this.aiming.setStrokeWidth(0.0f);
            });
            timer.setRepeats(false);
            timer.start();
        });

        this.courseDesigner.setOnAction(e -> {
            if(controller.isAnimationRunning()) return;

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
