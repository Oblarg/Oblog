import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a value as its default widget type on Shuffleboard.  Provides no configuration options.
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}