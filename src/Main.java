import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private double startX, startY, finishX, finishY, tolerance;

    private Pane mainPane;

    private void init(Course course) {
        this.startX = course.getStart().getX();
        this.startY = course.getStart().getX();

        this.finishX = course.getGoal().getX();
        this.finishY = course.getGoal().getX();
        this.tolerance = course.getToleranceRadius();
    }

    private double calculateFunction(double x, double y){
        return x * y + 20_000;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Constants.STAGE_TITLE);
        this.mainPane = new Pane();

        Course exampleCourse = new Course(9.81, 0.5, 3,
                new Point2D(250, 250), new Point2D(100, 150), 20,
                null, null);

        init(exampleCourse);

        double maxHeight = calculateFunction(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        Circle hole = new Circle(finishX, finishY, tolerance, Color.BLACK);
        Circle ball = new Circle(startX, startY, 10, Color.WHITE);

        for(double x = - Constants.SCENE_WIDTH / 2; x < Constants.SCENE_WIDTH / 2 ; x += 3.5){
            for(double y = - Constants.SCENE_HEIGHT / 2; y < Constants.SCENE_HEIGHT / 2; y += 3.5){
                double height = this.calculateFunction(x, y);

                Circle point = new Circle(x + Constants.SCENE_WIDTH / 2,
                        y + Constants.SCENE_HEIGHT / 2, 3, Color.GREEN);

                if(height < 0.0) point.setFill(Color.BLUE);
                else point.setFill(
                        Color.rgb(0,75 + (int)(100.0*(1.0 - height/maxHeight)),0));

                mainPane.getChildren().add(point);
            }
        }

        final Line[] line = {new Line(0, 0, 0, 0)};

        mainPane.getChildren().add(line[0]);

        ball.setOnMouseDragged(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            Circle src = (Circle)(event.getSource());
            line[0].setStartX(src.getCenterX());
            line[0].setStartY(src.getCenterY());
            line[0].setEndX(src.getCenterX() + (src.getCenterX() - mouseX));
            line[0].setEndY(src.getCenterY() + (src.getCenterY() - mouseY));

            line[0].setStrokeWidth(6.9);
            line[0].setFill(Color.ORANGE);
        });

        ball.setOnMouseReleased(event -> {
            PathTransition transition = new PathTransition();
            transition.setNode(ball);
            transition.setDuration(Duration.seconds(1.6));
            transition.setPath(line[0]);
            transition.setCycleCount(1);
            transition.setInterpolator(Interpolator.EASE_OUT);
            transition.play();

            ball.setCenterX(line[0].getEndX());
            ball.setCenterY(line[0].getEndY());

            line[0].setEndY(0);
            line[0].setEndX(0);
            line[0].setStartY(0);
            line[0].setStartX(0);
        });

        mainPane.getChildren().add(ball);
        mainPane.getChildren().add(hole);

        Scene mainScene = new Scene(mainPane, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        //FileWriter a = new FileWriter();
        //a.writeToFile(9.81, 0.5, 3, 0.0, 0.0, 0.0, 1.0, 0.02, "0.1*x + 0.03*x^2 + 0.2*y");
    }
}
