package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a differential drive with a widget that displays the speed of each side of the
 * drivebase and a vector for the direction and rotation of the drivebase. The widget will be
 * controllable if the robot is in test mode.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.drive.DifferentialDrive}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Number of wheels</td><td>Number</td><td>4</td><td>Must be a positive even integer
 * </td></tr>
 * <tr><td>Wheel diameter</td><td>Number</td><td>80</td><td>Pixels</td></tr>
 * <tr><td>Show velocity vectors</td><td>Boolean</td><td>true</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogDifferentialDrive {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Number of wheels.  Must be a positive, even integer.
    int numWheels() default 4;

    //Wheel diameter in pixels.
    double wheelDiameter() default 80;

    //Whether to show the velocity vectors.
    boolean showVel() default true;
}
