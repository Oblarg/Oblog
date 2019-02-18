import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Blocks the repetition on the dashboard of an occurrence of a class that has been marked as repeatable with
 * {@link AllowRepeat}.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ForbidRepeat {
}
