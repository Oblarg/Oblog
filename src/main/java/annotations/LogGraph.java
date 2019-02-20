package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a number with a graph. <strong>NOTE:</strong> graphs can be taxing on the computer
 * running the dashboard. Keep the number of visible data points to a minimum. Making the widget
 * smaller also helps with performance, but may cause the graph to become difficult to read.
 * <br>Supported types:
 * <ul>
 * <li>Number</li>
 * <li>Number array</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Visible time</td><td>Number</td><td>30</td>
 * <td>How long, in seconds, should past data be visible for</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogGraph {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Window length of past data displayed in the graph.
    double visibleTime() default 30;
}
