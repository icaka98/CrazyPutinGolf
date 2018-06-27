package Graphics.CustomPanes;

import Core.Controller;
import Graphics.ComponentFactory;
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

public class ChooseCoursePane extends Pane {
    private Button chooseBtn;
    private Label titleLabel;
    private Controller controller;

    public ChooseCoursePane(Controller controller){
        this.controller = controller;
        this.init();
    }

    private void init() {
        this.chooseBtn = ComponentFactory.getButton("Choose", 160, 40, 120, 450);

        this.titleLabel = ComponentFactory.getLabel("Choose course", 105, 10);
        this.titleLabel.setTextFill(Color.GREEN);
        this.titleLabel.setEffect(ComponentFactory.getDropShadow());
        this.titleLabel.setFont(Font.font(null, FontWeight.BOLD, 40));

        this.setListeners();

        this.getChildren().addAll(
                this.chooseBtn,
                this.titleLabel,
                this.getSubScene()
        );
    }

    private void setListeners() {
        this.chooseBtn.setOnMouseClicked(e -> {

        });
    }

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

        MainMenuPane.addPointsMesh(mesh, size, this.controller);

        MainMenuPane.addTextureMesh(mesh, size);

        // faces
        MainMenuPane.addFacesMesh(mesh, size);

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
        subScene.setLayoutX(110);
        subScene.setLayoutY(130);

        return subScene;
    }
}
