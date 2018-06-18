package Utils;

import java.awt.geom.Line2D;

/**
 * Constants used in the application.
 * @author Hristo Minkov
 * @author Zhecho Mitev
 */
public class Constants {
    public static final String STAGE_TITLE = "Crazy Putin";

    public static final String NON_NUMBERS = "[^\\d.-]";

    public static final int FIELD_WIDTH = 500;

    public static final int FIELD_HEIGHT = 500;

    public static final int SCENE_WIDTH = 750;

    public static final int SCENE_HEIGHT = 500;

    public static final double TIMESTEP_h = 0.02;

    public static final double SCALAR = 100;

    public static final double BALL_RADIUS = 10;

    public static final int TRANSITION_DURATION = 100;

    public static final double WALL_THICKNESS = 8.0;

    public static final String DEFAULT_COURSE_DIR = "src/Assets/";

    public static final String DEFAULT_PRECOMPILED_FILE = "src/Assets/precomputed_velocity.txt";

    public static final String DEFAULT_SAVE_FILE = "src/Assets/save_course.txt";

    public static final double STOP_SPEED = 0.1;

    public static final Line2D DOWN_MID_LINE = new Line2D.Double(
            -100 , 85
            ,100 ,85 );

    public static final Line2D UP_MID_LINE = new Line2D.Double(
            -100 , 103
            ,100 ,103 );

    public static final Line2D LEFT_MID_LINE = new Line2D.Double(
            -100 , 85
            ,-100 ,103 );

    public static final Line2D RIGHT_MID_LINE = new Line2D.Double(
            100 , 85
            ,100 ,103 );

    public static final Line2D UP_WALL = new Line2D.Double(-Constants.FIELD_WIDTH / 2 , Constants.FIELD_HEIGHT / 2 - (Constants.BALL_RADIUS + Constants.WALL_THICKNESS), Constants.FIELD_WIDTH / 2, Constants.FIELD_HEIGHT / 2 - (Constants.BALL_RADIUS + Constants.WALL_THICKNESS));
    public static final Line2D BOTTOM_WALL = new Line2D.Double(-Constants.FIELD_WIDTH / 2, -Constants.FIELD_HEIGHT / 2 + (Constants.BALL_RADIUS + Constants.WALL_THICKNESS), Constants.FIELD_WIDTH / 2, -Constants.FIELD_HEIGHT / 2 + (Constants.BALL_RADIUS + Constants.WALL_THICKNESS));
    public static final Line2D RIGHT_WALL = new Line2D.Double(Constants.FIELD_WIDTH / 2 - (Constants.BALL_RADIUS + Constants.WALL_THICKNESS), -Constants.FIELD_HEIGHT / 2, Constants.FIELD_WIDTH / 2 -(Constants.BALL_RADIUS + Constants.WALL_THICKNESS), Constants.FIELD_HEIGHT / 2);
    public static final Line2D LEFT_WALL = new Line2D.Double(-Constants.FIELD_WIDTH / 2 + (Constants.BALL_RADIUS + Constants.WALL_THICKNESS), -Constants.FIELD_HEIGHT / 2, -Constants.FIELD_WIDTH / 2 + (Constants.BALL_RADIUS + Constants.WALL_THICKNESS), Constants.FIELD_HEIGHT / 2);

    public static final int VELOCITY_SCALAR = 2;
}
