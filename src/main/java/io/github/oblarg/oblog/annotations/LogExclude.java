package io.github.oblarg.oblog.annotations;

import io.github.oblarg.oblog.Loggable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Prevents the logging of a {@link Loggable} field.  Use to suppress unwanted repetition of objects
 * if the object graph is not a tree.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LogExclude {
}
