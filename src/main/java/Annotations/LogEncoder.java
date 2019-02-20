package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays an {@link edu.wpi.first.wpilibj.Encoder} displaying its speed, total travelled
 * distance, and its distance per tick.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.Encoder}</li>
 * </ul>
 * <br>This widget has no custom properties.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogEncoder {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}
