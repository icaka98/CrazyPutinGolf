package Core;

import Models.Course;
import Utils.FileWriter;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Hristo Minkov
 */
public class CourseDesigner {
    private static Dialog<Pair<String, Course>> dialog;
    private static ButtonType submitButton;

    /**
     * A function that creates the dialog and displays it so that the user can enter
     * information about the desired course. Then the information is sent ot a
     * FileWriter which saves it into a file.
     * @see FileWriter
     */
    public static void run(){
        createDialog();
        addSubmitButton();
        addComponents();

        processResult();
    }

    /**
     * Processing the result after clicking the submit button.
     */
    private static void processResult() {
        Optional<Pair<String, Course>> result = dialog.showAndWait();

        result.ifPresent( res -> {
            FileWriter.writeToFile(res.getValue(), res.getKey());
        });
    }

    /**
     * Adding the submit button to the dialog.
     */
    private static void addSubmitButton() {
        submitButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);
    }

    /**
     * Create the dialog with initial information.
     */
    private static void createDialog(){
        dialog = new Dialog<>();

        dialog.setTitle("Course Designer");
        dialog.setHeaderText("Please enter information to create a new course!");
    }

    /**
     * Adding the needed components to the dialog.
     */
    private static void addComponents(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField gField = new TextField();
        gField.setPromptText("9.81");

        TextField muField = new TextField();
        muField.setPromptText("0.5");

        TextField vmaxField = new TextField();
        vmaxField.setPromptText("3");

        TextField startField = new TextField();
        startField.setPromptText("x, y");

        TextField goalField = new TextField();
        goalField.setPromptText("x, y");

        TextField tolerance = new TextField();
        tolerance.setPromptText("0.2");

        TextField functionField = new TextField();
        functionField.setPromptText("x + y");

        grid.add(new Label("Gravity: "), 0, 0);
        grid.add(gField, 1, 0);

        grid.add(new Label("Friction: "), 0, 1);
        grid.add(muField, 1, 1);

        grid.add(new Label("Max velocity: "), 0, 2);
        grid.add(vmaxField, 1, 2);

        grid.add(new Label("Starting point x, y: "), 0, 3);
        grid.add(startField, 1, 3);

        grid.add(new Label("Goal point x, y: "), 0, 4);
        grid.add(goalField, 1, 4);

        grid.add(new Label("Tolerance: "), 0, 5);
        grid.add(tolerance, 1, 5);

        grid.add(new Label("Function: "), 0, 6);
        grid.add(functionField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(gField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButton) {
                return new Pair<>(functionField.getText(),
                        new Course(Double.parseDouble(gField.getText()),
                                Double.parseDouble(muField.getText()),
                                Double.parseDouble(vmaxField.getText()),
                                createPoint(startField.getText()),
                                createPoint(goalField.getText()),
                                Double.parseDouble(tolerance.getText())
                        ));
            }
            return null;
        });
    }

    /**
     * Creating 2D point from a String
     * @param point the string representation of the point
     * @return the Point2D object that is corresponding to the String
     * @see Point2D
     */
    private static Point2D createPoint(String point) {
        double[] points = Arrays.stream(point.split(", "))
                .mapToDouble(Double::parseDouble).toArray();

        return new Point2D(points[0], points[1]);
    }
}
