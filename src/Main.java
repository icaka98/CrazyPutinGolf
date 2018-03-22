import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance;
    private int steps;

    private Line aiming;
    private Pane mainPane;
    private Circle ball, hole;

    private static double scalar = Constants.SCALAR;

    private PhysicsEngine physicsEngine;
    private CourseReader courseReader;
    private Function functionEvaluator;

    private void init(Course course) {
        this.steps = 0;

        this.startX = course.getStart().getX() * scalar;
        this.startY = course.getStart().getY() * scalar;

        this.finishX = course.getGoal().getX() * scalar;
        this.finishY = course.getGoal().getY() * scalar;
        this.tolerance = course.getToleranceRadius() * scalar * 10;

        this.physicsEngine = new PhysicsEngine();
    }

    private List<Point2D> getMoves(){
        List<Point2D> moves = new ArrayList<>();

        for(int i=1;i<=150;i++){
            moves.add(new Point2D(250 - i, 250 - i));
        }

        return moves;
    }

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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Constants.STAGE_TITLE);
        this.mainPane = new Pane();
        this.courseReader = new CourseReader(new File("src/Setup.txt"));
        this.functionEvaluator = new Function(this.courseReader.getEquation());

        init(this.courseReader.getCourse());

        double maxHeight = this.functionEvaluator.solve(Constants.SCENE_WIDTH / scalar, Constants.SCENE_HEIGHT / scalar);

        this.hole = new Circle(
                this.finishX + Constants.SCENE_WIDTH / 2,
                this.finishY + Constants.SCENE_HEIGHT / 2,
                this.tolerance, Color.BLACK);
        this.hole.setOpacity(.6);

        this.ball = new Circle(
                this.startX + Constants.SCENE_WIDTH / 2,
                this.startY + Constants.SCENE_HEIGHT / 2,
                10, Color.WHITE);

        for(double x = -Constants.SCENE_WIDTH / 2; x < Constants.SCENE_WIDTH / 2; x += 3.5){
            for(double y = -Constants.SCENE_HEIGHT / 2; y < Constants.SCENE_HEIGHT / 2; y += 3.5){

                double height = this.functionEvaluator.solve(x / scalar, y / scalar);

                Circle point = new Circle(x + Constants.SCENE_WIDTH / 2,
                        y + Constants.SCENE_HEIGHT / 2, 3, Color.GREEN);

                if(height < 0.0) point.setFill(Color.BLUE);
                else point.setFill(
                        Color.rgb(0,75 + (int)(130.0*(1.0 - height/maxHeight)),0));

                this.mainPane.getChildren().add(point);
            }
        }

        this.aiming = new Line(0, 0, 0, 0);
        this.aiming.setStrokeWidth(0.0);
        this.mainPane.getChildren().add(aiming);

        ball.setOnMouseDragged(event -> {
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

        ball.setOnMouseReleased(event -> {
            this.steps++;

            double aimX = (aiming.getEndX() - Constants.SCENE_WIDTH / 2) / scalar;
            double aimY = (aiming.getEndY() - Constants.SCENE_HEIGHT / 2) / scalar;

            double cenX = (this.ball.getCenterX() - Constants.SCENE_WIDTH / 2) / scalar;
            double cenY = (this.ball.getCenterY() - Constants.SCENE_HEIGHT / 2) / scalar;

            this.physicsEngine.setCurrentX(cenX);
            this.physicsEngine.setCurrentY(cenY);
            this.physicsEngine.takeVelocityOfShot(aimX, aimY);
            System.out.println("end: " + aimX + " " + aimY);
            System.out.println("current: " + cenX + " " + cenY);

            this.physicsEngine.executeShot();

            List<Point2D> moves = this.physicsEngine.getCoordinatesOfPath();
            System.out.println("LEN: " + moves.size());

            aiming.setEndY(0);
            aiming.setEndX(0);
            aiming.setStartY(0);
            aiming.setStartX(0);
            aiming.setStrokeWidth(0.0);

            SequentialTransition sequentialTransition = new SequentialTransition();

            for(int i=0;i<moves.size();i++)
                sequentialTransition.getChildren().add(this.createNextTransition(moves, i));

            sequentialTransition.setOnFinished( e -> {
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

            sequentialTransition.play();
        });

        this.mainPane.getChildren().add(this.ball);
        this.mainPane.getChildren().add(this.hole);

        Scene mainScene = new Scene(this.mainPane,
                Constants.SCENE_WIDTH,
                Constants.SCENE_HEIGHT);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
