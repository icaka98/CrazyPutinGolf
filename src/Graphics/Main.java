package Graphics;

import Core.Controller;
import Graphics.CustomPanes.MainMenuPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Hristo Minkov
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new MainMenuPane(new Controller()), 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starts the application.
     * @param args - custom arguments for starting from console.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
