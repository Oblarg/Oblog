import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Show velocity vectors</td><td>Boolean</td><td>true</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogMecanumDrive {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Whether to show the velocity vectors.
    boolean showVel() default true;
}
