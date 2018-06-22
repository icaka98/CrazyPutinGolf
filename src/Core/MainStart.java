package Core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStart extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.controller = new Controller();
        Scene scene = new Scene(new MainMenu(this.controller), 400, 500);



        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
