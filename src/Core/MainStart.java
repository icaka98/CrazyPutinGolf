package Core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStart extends Application {
    private static Application representation3d, game;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new MainMenu(), 400, 500);

        game = new Main();
        representation3d = new Main3D();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void start3D() throws Exception {
        representation3d.start(new Stage());
    }

    public static void startGame() throws Exception {
        game.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
