package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a mecanum drive with a widget that displays the speed of each wheel, and vectors for
 * the direction and rotation of the drivebase. The widget will be controllable if the robot is
 * in test mode.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.drive.MecanumDrive}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Show velocity vectors</td><td>Boolean</td><td>true</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogMecanumDrive {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Whether to show the velocity vectors.
    boolean showVel() default true;
}
