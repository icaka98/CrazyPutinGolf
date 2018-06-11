package Core;

import Models.*;
import Utils.Constants;
import Utils.CourseReader;
import Utils.Shot;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author Hristo Minkov
 */

//TODO make command line instead of putin the buttons on the course field
public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance, maxHeight, minHeight;
    private int steps, precomputedStep;
    private boolean precomputedMode, animationRunning;

    private Line aiming;
    private Pane mainPane;
    private Circle ball, hole;
    private Line stopLine;

    private Button next, changeMode, courseDesigner, enableBot, restartBtn;
    private Label modeState, functionLabel, positionLabel, titleLabel, goalLabel;

    private static double scalar = Constants.SCALAR;

    private Stage mainStage;
    private PhysicsEngine physicsEngine;
    private CourseReader courseReader;
    private Function functionEvaluator;
    private PrecomputedModule precomputedModule;

    private Bot bot;

    /**
     * Initializes all the variable fields of the class.
     */
    private void initVars() {
        this.steps = 0;
        this.precomputedStep = 0;
        this.precomputedMode = false;
        this.animationRunning = false;

        this.courseReader = new CourseReader(new File(Constants.DEFAULT_COURSE_FILE));
        this.mainPane = new Pane();
        this.functionEvaluator = new Function(this.courseReader.getEquation());
        this.precomputedModule = new PrecomputedModule();

        Course course = courseReader.getCourse();

        this.startX = course.getStart().getX() * scalar;
        this.startY = course.getStart().getY() * scalar;

        this.finishX = course.getGoal().getX() * scalar;
        this.finishY = course.getGoal().getY() * scalar;
        this.tolerance = course.getToleranceRadius() * scalar * 10;

        this.physicsEngine = new PhysicsEngine();
        this.bot = new Randy(physicsEngine);
    }

    /**
     * Initializes all the graphic components of the class.
     */
    private void initComponents(){
        this.hole = new Circle(
                this.finishX + Constants.FIELD_WIDTH / 2,
                this.finishY + Constants.FIELD_HEIGHT / 2,
                this.tolerance, Color.BLACK);
        this.hole.setOpacity(.6);

        this.aiming = new Line(0, 0, 0, 0);
        this.aiming.setStrokeWidth(0.0);

        this.stopLine = new Line(
                Constants.DOWN_MID_LINE.getX1()+ Constants.FIELD_WIDTH / 2 + 8, Constants.DOWN_MID_LINE.getY1() +Constants.FIELD_HEIGHT / 2 + 8,
                Constants.DOWN_MID_LINE.getX2()+ Constants.FIELD_WIDTH / 2 - 8,  Constants.DOWN_MID_LINE.getY2() + Constants.FIELD_HEIGHT / 2 + 8);

        this.stopLine.setStrokeWidth(Constants.WALL_THICKNESS);

        this.ball = new Circle(
                this.startX + Constants.FIELD_WIDTH / 2,
                this.startY + Constants.FIELD_HEIGHT / 2,
                Constants.BALL_RADIUS, Color.WHITE);

        this.next = new Button("Next");
        this.next.setPrefSize(60, 30);
        this.next.setLayoutX(510.0);
        this.next.setLayoutY(130.0);

        this.changeMode = new Button("Change mode");
        this.changeMode.setPrefSize(160, 40);
        this.changeMode.setLayoutX(545);
        this.changeMode.setLayoutY(270);

        this.restartBtn = new Button("Restart");
        this.restartBtn.setPrefSize(160, 40);
        this.restartBtn.setLayoutX(545);
        this.restartBtn.setLayoutY(440);

        this.modeState = new Label("Mode: Player mode");
        this.modeState.setLayoutX(510.0);
        this.modeState.setLayoutY(180.0);
        this.modeState.setTextFill(Color.BLACK);

        this.functionLabel = new Label("Function: " + this.courseReader.getEquation().replaceAll("\\s+", ""));
        this.functionLabel.setLayoutX(510.0);
        this.functionLabel.setLayoutY(90.0);
        this.functionLabel.setTextFill(Color.BLACK);

        this.positionLabel = new Label("Ball position: (" + this.ball.getCenterX() +" , " + this.ball.getCenterY() + ")");
        this.positionLabel.setLayoutX(510.0);
        this.positionLabel.setLayoutY(110.0);
        this.positionLabel.setTextFill(Color.BLACK);

        this.goalLabel = new Label(
                "Goal position: (" +
                        (this.hole.getCenterX() + "").substring(0, 5) +
                        " , " +
                        (this.hole.getCenterY() + "").substring(0, 5) +
                        ")");
        this.goalLabel.setLayoutX(510.0);
        this.goalLabel.setLayoutY(150.0);
        this.goalLabel.setTextFill(Color.BLACK);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setLayoutX(511);
        vbox.setLayoutY(60);
        vbox.setStyle("-fx-border-color: black");

        vbox.getChildren().addAll(this.modeState, this.goalLabel, this.positionLabel, this.functionLabel);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(1.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        this.titleLabel = new Label("Putin Golf");
        this.titleLabel.setLayoutX(545.0);
        this.titleLabel.setLayoutY(10.0);
        this.titleLabel.setTextFill(Color.GREEN);
        this.titleLabel.setEffect(ds);
        this.titleLabel.setFont(Font.font(null, FontWeight.BOLD, 32));

        this.courseDesigner = new Button("Course designer");
        this.courseDesigner.setPrefSize(160, 40);
        this.courseDesigner.setLayoutX(545);
        this.courseDesigner.setLayoutY(370);

        this.enableBot = new Button("Bot Shot");
        this.enableBot.setPrefSize(160, 40);
        this.enableBot.setLayoutX(545.0);
        this.enableBot.setLayoutY(200.0);

        Arc arc = new Arc();
        arc.setCenterX(300);
        arc.setCenterY(250);
        arc.setRadiusX(285);
        arc.setRadiusY(285);
        arc.setStartAngle(260.0f);
        arc.setLength(45.0f);
        arc.setType(ArcType.ROUND);
        arc.setOpacity(0.4);

        this.mainPane.getChildren().add(this.aiming);
        this.mainPane.getChildren().add(this.ball);
        this.mainPane.getChildren().add(this.hole);
        this.mainPane.getChildren().add(this.changeMode);
        this.mainPane.getChildren().add(this.enableBot);
        this.mainPane.getChildren().add(this.next);
        this.mainPane.getChildren().add(this.courseDesigner);
        this.mainPane.getChildren().add(this.stopLine);
        this.mainPane.getChildren().add(this.restartBtn);

        this.mainPane.getChildren().add(this.titleLabel);
        this.mainPane.getChildren().add(vbox);

    }

    /**
     * Draws the concrete course using points with particular colors.
     * @param maxHeight the maximum height of the course function
     * @param minHeight the minimum height of the course function
     */
    private void drawField(double maxHeight, double minHeight){
        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                Circle point = new Circle(x + Constants.FIELD_WIDTH / 2,
                        y + Constants.FIELD_HEIGHT / 2, 3, Color.GREEN);
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
                            Color.rgb(0,0, blueRatio));
                else{
                    if(Constants.FIELD_WIDTH / 2 - x < Constants.WALL_THICKNESS
                            || x < -Constants.FIELD_WIDTH / 2 + Constants.WALL_THICKNESS
                            || y < -Constants.FIELD_HEIGHT / 2 + Constants.WALL_THICKNESS
                            || Constants.FIELD_HEIGHT / 2 - y < Constants.WALL_THICKNESS){
                        point.setFill(Color.valueOf("#ECD540"));
                    }else point.setFill(

                            Color.rgb(0,greenRatio,0));
                }

                this.mainPane.getChildren().add(point);
            }
        }

        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                if(x + Constants.FIELD_WIDTH / 2 - 10 <= 4.5){
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
            this.animationRunning = false;
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

                if(this.steps == 0) alert.setContentText("The bot scored hole-in-one!");
                else alert.setContentText("You scored in " + this.steps + " steps.");

                alert.show();
            }
        });

        this.animationRunning = true;
        this.changeMode.setDisable(true);
        this.enableBot.setDisable(true);
        this.next.setDisable(true);
        sequentialTransition.play();
    }

    /**
     * Executing a shot in the Physics engine
     * @param aimX the X coordinate of the selected velocity
     * @param aimY the Y coordinate of the selected velocity
     * @return collection of all moves that have to be processed
     * @see PhysicsEngine
     */
    private List<Point2D> prepareEngine( double aimX, double aimY){

        double cenX = (this.ball.getCenterX() - Constants.FIELD_WIDTH / 2) / scalar;
        double cenY = (this.ball.getCenterY() - Constants.FIELD_HEIGHT / 2) / scalar;

        this.physicsEngine.setCurrentX(cenX);
        this.physicsEngine.setCurrentY(cenY);

        this.physicsEngine.takeVelocityOfShot(aimX, aimY);
        this.physicsEngine.executeShot();

        return this.physicsEngine.getCoordinatesOfPath();
    }

    private void calculateMinMax(){
        for(double x = -Constants.FIELD_WIDTH / 2; x < Constants.FIELD_WIDTH / 2; x += 3.5){
            for(double y = -Constants.FIELD_HEIGHT / 2; y < Constants.FIELD_HEIGHT / 2; y += 3.5){
                double height = this.functionEvaluator.solve(x / scalar, y / scalar);
                this.maxHeight = Math.max(this.maxHeight, height);
                this.minHeight = Math.min(this.minHeight, height);
            }
        }
    }

    private void restart(){
        this.mainStage.close();
        start(this.mainStage);
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

        this.maxHeight = Double.MIN_VALUE;
        this.minHeight = Double.MAX_VALUE;

        this.calculateMinMax();

        this.drawField(this.maxHeight, this.minHeight);
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

            double aimX = (aiming.getEndX() - Constants.FIELD_WIDTH / 2) / scalar;
            double aimY = (aiming.getEndY() - Constants.FIELD_HEIGHT / 2) / scalar;

            List<Point2D> moves = this.prepareEngine(aimX, aimY);
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

            System.out.printf("%f %f\n", nextMove.getX(), nextMove.getY());

            List<Point2D> moves = this.prepareEngine(nextMove.getX(), nextMove.getY());
            System.out.println("LEN: " + moves.size());

            this.executeTransitions(moves);
        });

        this.changeMode.setOnAction(e -> {
            this.precomputedMode = !this.precomputedMode;
            this.next.setVisible(this.precomputedMode);
            this.modeState.setText(this.precomputedMode ? "Precomputed mode" : "Player mode");
        });

        this.enableBot.setOnAction(e -> {
            Shot p = bot.go();
            double aimX = p.getVelocityX();
            double aimY = p.getVelocityY();


            this.aiming.setEndX((aimX * scalar + Constants.FIELD_WIDTH / 2));
            this.aiming.setEndY((aimY * scalar + Constants.FIELD_HEIGHT / 2));
            this.aiming.setStartX(this.ball.getCenterX());
            this.aiming.setStartY(this.ball.getCenterY());
            this.aiming.setStrokeWidth(6.9f);

            List<Point2D> moves = this.prepareEngine(aimX, aimY);
            System.out.println("LEN: " + moves.size());

            Timer timer = new Timer(666, arg0 -> {
                this.executeTransitions(moves);
                this.aiming.setStrokeWidth(0.0f);
            });
            timer.setRepeats(false);
            timer.start();
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
