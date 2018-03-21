import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance;

    private Line aiming;
    private Pane mainPane;
    private Circle ball, hole;

    private PhysicsEngine physicsEngine;

    private void init(Course course) {
        this.startX = course.getStart().getX();
        this.startY = course.getStart().getY();

        this.finishX = course.getGoal().getX();
        this.finishY = course.getGoal().getY();
        this.tolerance = course.getToleranceRadius();

        this.physicsEngine = new PhysicsEngine();
    }

    private double calculateFunction(double x, double y){
        return Math.pow(x, 7) + Math.pow(y, 7) + 1_000_000_000_000_000_0L;
        // 0.1*x + 0.03*x*x + 0.2*y;
        //x * y + 20_000;
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
                move.getX(),
                move.getY());

        double endX = path.getEndX();
        double endY = path.getEndY();

        PathTransition transition = new PathTransition();
        transition.setNode(this.ball);
        transition.setDuration(Duration.millis(6));
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

        Course exampleCourse = new Course(9.81, 0.5, 3,
                new Point2D(250, 250), new Point2D(100, 100), 20,
                null, null);

        init(exampleCourse);

        double maxHeight = calculateFunction(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        this.hole = new Circle(this.finishX, this.finishY, this.tolerance, Color.BLACK);
        hole.setOpacity(.6);

        this.ball = new Circle(this.startX, this.startY, 10, Color.WHITE);

        for(double x = - Constants.SCENE_WIDTH / 2; x < Constants.SCENE_WIDTH / 2 ; x += 3.5){
            for(double y = - Constants.SCENE_HEIGHT / 2; y < Constants.SCENE_HEIGHT / 2; y += 3.5){
                double height = this.calculateFunction(x, y);

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
            this.physicsEngine.takeVelocityOfShot(aiming.getEndX() / 100, aiming.getEndY() / 100);
            this.physicsEngine.setCurrentX(this.ball.getCenterX() / 100);
            this.physicsEngine.setCurrentY(this.ball.getCenterY() / 100);
            this.physicsEngine.startEngine();

            List<Point2D> moves = this.getMoves();//this.physicsEngine.getCoordinatesOfPath();

            aiming.setEndY(0);
            aiming.setEndX(0);
            aiming.setStartY(0);
            aiming.setStartX(0);
            aiming.setStrokeWidth(0.0);

            SequentialTransition sequentialTransition = new SequentialTransition();

            for(int i=0;i<moves.size();i++)
                sequentialTransition.getChildren().add(this.createNextTransition(moves, i));

            sequentialTransition.play();
        });

        this.mainPane.getChildren().add(ball);
        this.mainPane.getChildren().add(hole);

        Scene mainScene = new Scene(mainPane,
                Constants.SCENE_WIDTH,
                Constants.SCENE_HEIGHT);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
