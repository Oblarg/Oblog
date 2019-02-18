import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a command with a toggle button. Pressing the button will start the command, and the
 * button will automatically release when the command completes.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.command.Command}</li>
 * <li>{@link edu.wpi.first.wpilibj.command.CommandGroup}</li>
 * <li>Any custom subclass of {@code Command} or {@code CommandGroup}</li>
 * </ul>
 * <br>This widget has no custom properties.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogCommand {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}
