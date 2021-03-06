package Graphics.Game;

import Core.Controller;
import Graphics.CustomPanes.MainMenuPane;
import Utils.Constants;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hristo Minkov
 */
public class Game3D extends Application {
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Sphere ball;
    private Group cube;
    private List<Point3D> moves;

    private Controller controller;

    public Game3D(Controller controller) {
        this.controller = controller;
    }

    /**
     * @return the ball object
     */
    public Sphere getBall() {
        return ball;
    }

    /**
     * Creates a specific transition corresponding to a given move
     * @param moves the collection of moves that should be executed
     * @param idx the current index of the moves showing the transition state
     * @return the newly created transition
     * @see PathTransition
     */
    private Pair<TranslateTransition, Point3D> createNextTransition(List<Point3D> moves, int idx, Point3D ballPos){
        Point3D move = moves.get(idx);

        System.out.println(move.getX() + "  " + move.getZ());

        TranslateTransition tt = new TranslateTransition(Duration.millis(100), this.ball);
        tt.setFromX(ballPos.getX());
        tt.setFromY(ballPos.getY());
        tt.setFromZ(ballPos.getZ());
        tt.setToX(move.getX() * Constants.SCALAR);
        tt.setToY(move.getY() * Constants.SCALAR);
        tt.setToZ(move.getZ() * Constants.SCALAR - 20);

        ballPos = new Point3D(move.getX() * Constants.SCALAR,
                move.getY() * Constants.SCALAR,
                move.getZ() * Constants.SCALAR - 20);

        return new Pair<>(tt, ballPos);
    }

    /**
     * Add all transitions to a sequential transition and execute the main transition
     * @param moves a list with all the velocities to be processed
     */
    private void executeTransitions(List<Point3D> moves){
        SequentialTransition sequentialTransition = new SequentialTransition();

        double ballX = this.ball.getTranslateX();
        double ballY = this.ball.getTranslateY();
        double ballZ = this.ball.getTranslateZ();

        Point3D ballPos = new Point3D(ballX, ballY, ballZ);

        for(int i=0;i<moves.size();i++){
            Pair result = this.createNextTransition(moves, i, ballPos);
            ballPos = (Point3D) result.getValue();
            sequentialTransition.getChildren().add((TranslateTransition) result.getKey());
        }

        Point3D finalBallPos = ballPos;
        sequentialTransition.setOnFinished(e -> {
            this.moves.clear();

            /*this.ball.setTranslateX(finalBallPos.getX());
            this.ball.setTranslateY(finalBallPos.getY());
            this.ball.setTranslateZ(finalBallPos.getZ());*/
        });

        sequentialTransition.play();
    }

    /**
     * Start method.
     * @param primaryStage the Stage that the start is applied to.
     */
    @Override
    public void start(Stage primaryStage) {
        this.cube = new Group();

        this.cube.getTransforms().addAll(this.rotateY);
        this.cube.getTransforms().addAll(this.rotateX);

        this.moves = new ArrayList<>();

        TriangleMesh mesh = new TriangleMesh();
        TriangleMesh water = new TriangleMesh();

        this.controller.checkFunctionBounds();

        this.controller.init3DVars();

        Box obs = new Box(30, 30, 30);
        this.cube.getChildren().addAll(obs);

        this.ball = new Sphere();
        this.ball.setRadius(6);
        this.ball.setTranslateZ(200);
        this.ball.setTranslateX(200);
        this.ball.setTranslateY(this.controller.solve(200, -200) * this.controller.getAmplification() - this.ball.getRadius());
        int size = 100;

        for (double x = -2.5; x <= 2.5; x+=4.9999/((float)(size-1))) {
            for (double y = -2.5; y <= 2.5; y+=4.9999/((float)(size-1))) {
                double z = (this.controller.solve(x, y) * this.controller.getAmplification());
                if(z < -2.5) z = -2.5;
                if(z > 2.5) z = 2.5;
                mesh.getPoints().addAll(
                        (int)(x * Constants.SCALAR),
                        (int)(z * Constants.SCALAR),
                        (int)(y * Constants.SCALAR));
            }
        }

        // texture
        MainMenuPane.addTextureMesh(mesh, size);

        // faces
        MainMenuPane.addFacesMesh(mesh, size);

        this.cube.setTranslateY(400);
        this.cube.setTranslateX(400);

        PhongMaterial fieldMaterial = new PhongMaterial();
        fieldMaterial.setSpecularColor(Color.LIGHTGREEN);
        fieldMaterial.setDiffuseColor(Color.LIGHTGREEN);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        meshView.setOnMouseClicked(e -> {
            PickResult pr = e.getPickResult();

            /*this.curX = ;
            this.curY = this.solve(this.curX, this.curZ) * this.amplification;
            this.curZ = ;
*/
            List<Point3D> moves = this.controller.prepare3DEngine(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getZ());

            this.executeTransitions(moves);
        });

        this.cube.getChildren().addAll(meshView);

        Scene scene = new Scene(this.cube, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GREY);
        scene.setCamera(new PerspectiveCamera());

        scene.setOnKeyPressed(t -> {
            switch (t.getCode()){
                case LEFT: this.rotateY.setAngle(this.rotateY.getAngle() - 10); break;
                case RIGHT: this.rotateY.setAngle(this.rotateY.getAngle() + 10); break;

                case UP: this.rotateX.setAngle(this.rotateX.getAngle() + 10); break;
                case DOWN: this.rotateX.setAngle(this.rotateX.getAngle() - 10); break;
            }
        });

        this.cube.getChildren().add(this.ball);

        makeZoomable(this.cube);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Allow zoom on object.
     * @param control object that should be zoomed
     */
    private void makeZoomable(Group control) {
        control.addEventFilter(ScrollEvent.ANY, event -> {
            double delta = 1.2;
            double scale = control.getScaleX();

            if (event.getDeltaY() < 0) scale /= delta;
            else scale *= delta;

            scale = clamp(scale);
            control.setScaleX(scale);
            control.setScaleY(scale);

            event.consume();
        });
    }

    /**
     * Scale the zoom value.
     * @param value the current zoom value.
     * @return the next zoom value
     */
    private static double clamp(double value) {
        if (Double.compare(value, 0.1) < 0) return 0.1;
        if (Double.compare(value, 10.0) > 0) return 10.0;
        return value;
    }

    /**
     * Implemented for testing.
     * @param args arguments that allow start from console
     */
    public static void main(String[] args) {
        launch(args);
    }
}