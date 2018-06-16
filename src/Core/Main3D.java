package Core;

import Models.Function;
import Utils.Constants;
import Utils.CourseReader;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main3D extends Application {
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private double maxHeight, minHeight, amplification;
    private Sphere ball;
    private double curX, curY, curZ;
    private Group cube;
    private List<Point3D> moves;
    private PhysicsEngine physicsEngine;
    private Function functionEvaluator;

    private double solve(double x, double y){
        return functionEvaluator.solve(x, y);
    }

    private void checkFunctionBounds(){
        for (double x = -2.50; x < 2.50; x+=0.01) {
            for (double y = -2.50; y < 2.50; y+=0.01) {
                this.maxHeight = Math.max(solve(x, y), this.maxHeight);
                this.minHeight = Math.min(solve(x, y), this.minHeight);
            }
        }
    }

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

    private List<Point3D> prepareEngine( double aimX, double aimY){

        double cenX = (this.ball.getTranslateX()) / Constants.SCALAR;
        double cenY = (this.ball.getTranslateZ()) / Constants.SCALAR;

        aimX /= Constants.SCALAR;
        aimY /= Constants.SCALAR;

        this.physicsEngine.setCurrentX(cenX);
        this.physicsEngine.setCurrentY(cenY);

        this.physicsEngine.takeVelocityOfShot(aimX, aimY);
        this.physicsEngine.executeShot();

        return this.physicsEngine.getCoordinatesOfPath().subList(0,
                this.physicsEngine.getCoordinatesOfPath().size()-1).stream()
                .map(m -> new Point3D(m.getX(),
                        this.solve(m.getX(), m.getY()) * amplification,
                        m.getY()))
                .collect(Collectors.toList());
    }

    @Override
    public void start(Stage primaryStage) {
        this.cube = new Group();
        this.physicsEngine = new PhysicsEngine();

        CourseReader courseReader = new CourseReader(new File(Constants.DEFAULT_COURSE_FILE));
        this.functionEvaluator = new Function(courseReader.getEquation());

        this.cube.getTransforms().addAll(rotateY);

        this.moves = new ArrayList<>();

        TriangleMesh mesh = new TriangleMesh();

        this.checkFunctionBounds();

        this.maxHeight = this.maxHeight * Constants.SCALAR;

        this.amplification = (float) -1;//(-250.0f / this.maxHeight);

        int size = 100;

        for (double x = -2.5; x <= 2.5; x+=4.9999/((float)(size-1))) {
            for (double y = -2.5; y <= 2.5; y+=4.9999/((float)(size-1))) {
                double z = (solve(x, y) * this.amplification);
                if(z < -2.5) z = -2.5;
                if(z > 2.5) z = 2.5;
                mesh.getPoints().addAll(
                        (int)(x * Constants.SCALAR),
                        (int)(z * Constants.SCALAR),
                        (int)(y * Constants.SCALAR));
            }
        }

        this.ball = new Sphere();
        this.ball.setRadius(6);
        this.ball.setTranslateZ(200);
        this.ball.setTranslateX(200);
        this.ball.setTranslateY(this.solve(200, -200) * this.amplification - this.ball.getRadius());

        // texture

        for (float x = 0; x < size - 1; x++) {
            for (float y = 0; y < size - 1; y++) {
                float x0 = x / (float) size;
                float y0 = y / (float) size;
                float x1 = (x + 1) / (float) size;
                float y1 = (y + 1) / (float) size;

                mesh.getTexCoords().addAll(
                        x1, y1,
                        x1, y0,
                        x0, y1,
                        x0, y0
                );
            }
        }

        // faces
        for (int x = 0; x < size - 1; x++) {
            for (int z = 0; z < size - 1; z++) {
                int p0 = x * size + z;
                int p1 = x * size + z + 1;
                int p2 = (x + 1) * size + z;
                int p3 = (x + 1) * size + z + 1;

                mesh.getFaces().addAll(p2, 0, p1, 0, p0, 0);
                mesh.getFaces().addAll(p2, 0, p3, 0, p1, 0);

            }
        }

        cube.setTranslateY(400);
        cube.setTranslateX(400);

        PhongMaterial fieldMaterial = new PhongMaterial();
        fieldMaterial.setSpecularColor(Color.LIGHTGREEN);
        fieldMaterial.setDiffuseColor(Color.LIGHTGREEN);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        meshView.setOnMouseClicked(e -> {
            PickResult pr = e.getPickResult();

            this.curX = pr.getIntersectedPoint().getX();
            this.curY = this.solve(this.curX, this.curZ) * this.amplification;
            this.curZ = pr.getIntersectedPoint().getZ();

            List<Point3D> moves = this.prepareEngine(this.curX, this.curZ);

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
            }
        });

        this.cube.getChildren().add(this.ball);

        makeZoomable(this.cube);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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

    private static double clamp(double value) {
        if (Double.compare(value, 0.1) < 0) return 0.1;
        if (Double.compare(value, 10.0) > 0) return 10.0;
        return value;
    }

    public static void main(String[] args) {
        launch(args);
    }
}