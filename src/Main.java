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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance;

    private Line aiming;
    private Pane mainPane;
    private Circle ball, hole;

    private static double scalar = Constants.SCALAR;

    private PhysicsEngine physicsEngine;
    private CourseReader courseReader;

    private void init(Course course) {
        this.startX = course.getStart().getX() * scalar;
        this.startY = course.getStart().getY() * scalar;

        this.finishX = course.getGoal().getX() * scalar;
        this.finishY = course.getGoal().getY() * scalar;
        this.tolerance = course.getToleranceRadius() * scalar * 10;

        this.physicsEngine = new PhysicsEngine();
    }

    private double calculateFunction(double x, double y){
        return 0.1*x + 0.03*x*x + 0.2*y;
        // 0.0003*x + 0.0002*y + 0.1;
        //Math.pow(x, 7) + Math.pow(y, 7) + 1_000_000_000_000_000_0L;
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
                move.getX() * scalar,
                move.getY() * scalar);

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

        init(this.courseReader.getCourse());

        double maxHeight = calculateFunction(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        System.out.println("dsa:" + this.tolerance);
        this.hole = new Circle(this.finishX, this.finishY, this.tolerance, Color.BLACK);
        hole.setOpacity(.6);

        this.ball = new Circle(this.startX, this.startY, 10, Color.WHITE);

        for(double x = 0; x < Constants.SCENE_WIDTH ; x += 3.5){
            for(double y = 0; y < Constants.SCENE_HEIGHT; y += 3.5){
                double height = this.calculateFunction(x, y);

                Circle point = new Circle(x,
                        y, 3, Color.GREEN);

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
            double aimX = (aiming.getEndX()) / scalar;
            double aimY = (aiming.getEndY()) / scalar;

            double cenX = this.ball.getCenterX() / scalar;
            double cenY = this.ball.getCenterY() / scalar;

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
