package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a class as repeatable.  This will allow it to be sent to the dashboard repeatably, e.g. if it
 * is a field of more than one class.  If you further want to disallow repetition of specific occurrences, mark with
 * {@link LogExclude}.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LogRepeat {
}
