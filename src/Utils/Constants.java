package Utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Constants used in the application.
 * @author Hristo Minkov
 * @author Zhecho Mitev
 */
public class Constants {
    public static final String STAGE_TITLE = "Crazy Putin";

    public static final String NON_NUMBERS = "[^\\d.-]";

    public static final int SCENE_WIDTH = 500;

    public static final int SCENE_HEIGHT = 500;

    public static final double TIMESTEP_h = 0.02;

    public static final double SCALAR = 100;

    public static final int TRANSITION_DURATION = 100;

    public static final double WALL_POSITION = 2.3;

    public static final double WALL_THICKNESS = 8.0;

    public static final String DEFAULT_COURSE_FILE = "src/Assets/save_course.txt";

    public static final String DEFAULT_PRECOMPILED_FILE = "src/Assets/precomputed_velocity.txt";

    public static final String DEFAULT_SAVE_FILE = "src/Assets/save_course.txt";

    public static final double STOP_SPEED = 0.1;

    public static final Line2D MID_LINE = new Line2D.Double(
            -100 , 45
            ,200 ,45 );

    public static final int VELOCITY_SCALAR = 2;
}
