package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a PID controller with an editor for the PIDF constants and a toggle switch for
 * enabling and disabling the controller.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.PIDController}</li>
 * </ul>
 * <br>This widget has no custom properties.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogPIDController {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}
