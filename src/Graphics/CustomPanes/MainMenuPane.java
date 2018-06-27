package Graphics.CustomPanes;

import Core.Controller;
import Graphics.ComponentFactory;
import Utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * @author Hristo Minkov
 */
public class MainMenuPane extends Pane {
    private Button start, chooseCourse, exit, graphics;
    private Label titleLabel, currentCourseLabel;
    private Controller controller;

    public MainMenuPane(Controller controller){
        this.controller = controller;
        this.init();
    }

    /**
     * Initializes the graphic components
     */
    private void init() {
        this.start = ComponentFactory.getButton("Play Golf!", 180, 60, 220, 270);
        this.chooseCourse = ComponentFactory.getButton("Choose course", 200, 60, 210, 340);
        this.exit = ComponentFactory.getButton("Exit game", 180, 60, 220, 510);
        this.graphics = ComponentFactory.getButton("3D Representation", 180, 60, 220, 410);

        this.titleLabel = ComponentFactory.getLabel("Putin Golf", 200, 10);
        this.titleLabel.setTextFill(Color.GREEN);
        this.titleLabel.setEffect(ComponentFactory.getDropShadow());
        this.titleLabel.setFont(Font.font(null, FontWeight.BOLD, 46));

        this.currentCourseLabel = ComponentFactory.getLabel("Current course",
                250, 100);
        this.currentCourseLabel.setFont(Font.font(null, FontWeight.BOLD, 18));

        this.setListeners();

        this.getChildren().addAll(
                this.start,
                this.chooseCourse,
                this.exit,
                this.titleLabel,
                this.getSubScene(),
                this.currentCourseLabel,
                this.graphics
        );
    }

    /**
     * Apply listeners to components
     */
    private void setListeners() {
        this.exit.setOnMouseClicked(e -> {
            System.exit(0);
        });

        this.graphics.setOnMouseClicked(e ->{
            try {
                Controller.start3D();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.chooseCourse.setOnMouseClicked(e ->{

        });

        this.start.setOnMouseClicked(e -> {
            try {
                Controller.startGame();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    /**
     * Adding SubScene containing the rotating current course.
     * @return SubScene
     */
    private SubScene getSubScene(){
        Translate pivot = new Translate();
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                pivot, rotate,
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0 , 0, -50)
        );

        TriangleMesh mesh = new TriangleMesh();

        int size = 15;

        addPointsMesh(mesh, size, this.controller);

        addTextureMesh(mesh, size);

        addFacesMesh(mesh, size);

        PhongMaterial fieldMaterial = new PhongMaterial();
        fieldMaterial.setSpecularColor(Color.LIGHTGREEN);
        fieldMaterial.setDiffuseColor(Color.LIGHTGREEN);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        Group group = new Group();
        group.getChildren().addAll(meshView, camera);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(rotate.angleProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(15),
                        new KeyValue(rotate.angleProperty(), 360)
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        SubScene subScene = new SubScene(group, 180, 120, true, SceneAntialiasing.DISABLED);
        subScene.setFill(Color.LIGHTBLUE);
        subScene.setCamera(camera);
        subScene.setLayoutX(220);
        subScene.setLayoutY(130);

        return subScene;
    }

    /**
     * Adding mesh's faces
     * @param mesh the mesh
     * @param size custom size
     */
    public static void addFacesMesh(TriangleMesh mesh, int size) {
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
    }

    /**
     * Adding the points to the mesh.
     * @param mesh the mesh
     * @param size custom size
     */
    static void addPointsMesh(TriangleMesh mesh, int size, Controller controller) {
        for (double x = -2.5; x <= 2.5; x+=4.9999/((float)(size-1))) {
            for (double y = -2.5; y <= 2.5; y+=4.9999/((float)(size-1))) {
                double z = (controller.solve(x, y) * -1);
                if(z < -2.5) z = -2.5;
                if(z > 2.5) z = 2.5;
                mesh.getPoints().addAll(
                        (int)(x * Constants.SCALAR / 30),
                        (int)(z * Constants.SCALAR / 30),
                        (int)(y * Constants.SCALAR / 30));
            }
        }
    }

    /**
     * Adding texture to mesh.
     * @param mesh the mesh
     * @param size custom size
     */
    public static void addTextureMesh(TriangleMesh mesh, int size) {
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
    }
}
