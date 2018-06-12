package Core;

import Utils.Constants;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

class ComponentFactory {
    static Circle getHole(double finishX, double finishY, double tolerance){
        Circle hole = new Circle(
                finishX + Constants.FIELD_WIDTH / 2,
                finishY + Constants.FIELD_HEIGHT / 2,
                tolerance, Color.BLACK);

        hole.setOpacity(0.6f);
        return hole;
    }

    static Line getStopWall(){
        Line stopLine = new Line(
                Constants.DOWN_MID_LINE.getX1()+ Constants.FIELD_WIDTH / 2 + 8,
                Constants.DOWN_MID_LINE.getY1() +Constants.FIELD_HEIGHT / 2 + 8,
                Constants.DOWN_MID_LINE.getX2()+ Constants.FIELD_WIDTH / 2 - 8,
                Constants.DOWN_MID_LINE.getY2() + Constants.FIELD_HEIGHT / 2 + 8);

        stopLine.setStrokeWidth(Constants.WALL_THICKNESS);
        return stopLine;
    }

    static Line getAiming(){
        Line aiming = new Line(0, 0, 0, 0);
        aiming.setStrokeWidth(0.0);

        return aiming;
    }

    static Circle getBall(double startX, double startY){
        return new Circle(
                startX + Constants.FIELD_WIDTH / 2,
                startY + Constants.FIELD_HEIGHT / 2,
                Constants.BALL_RADIUS, Color.WHITE);
    }

    static Button getButton(String text,
                            double prefW, double prefH,
                            double layoutX, double layoutY){
        Button button = new Button(text);
        button.setPrefSize(prefW, prefH);
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);

        return button;
    }
}
