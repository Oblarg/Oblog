import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Displays a PID command with a checkbox and an editor for the PIDF constants. Selecting the
 * checkbox will start the command, and the checkbox will automatically deselect when the command
 * completes.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.command.PIDCommand}</li>
 * <li>Any custom subclass of {@code PIDCommand}</li>
 * </ul>
 * <br>This widget has no custom properties.
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogPIDCommand {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}
