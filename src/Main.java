import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Constants.STAGE_TITLE);

        Pane mainPane = new Pane();

        double maxHeight = Constants.SCENE_WIDTH * 0.1
                + 1.03 * Constants.SCENE_WIDTH * Constants.SCENE_WIDTH;

        double startX = 250;
        double startY = 250;

        double finishX = 150;
        double finishY = 150;
        double tolerance = 20;

        Circle hole = new Circle(finishX, finishY, tolerance, Color.BLACK);
        Circle ball = new Circle(startX, startY, 10, Color.WHITE);

        for(double x = 0.0; x < Constants.SCENE_WIDTH ; x += 3.5){
            for(double y = 0.0; y < Constants.SCENE_HEIGHT; y += 3.5){
                double height = 0.1 * x + 1.03 * x * x - 0.5 * y;

                Circle point = new Circle(x, y, 3, Color.GREEN);

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
    }
}
