package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a number with a view-only dial. Displayed values are rounded to the nearest integer.
 * <br>Supported types:
 * <ul>
 * <li>Number</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Min</td><td>Number</td><td>0</td><td>The minimum value of the dial</td></tr>
 * <tr><td>Max</td><td>Number</td><td>100</td><td>The maximum value of the dial</td></tr>
 * <tr><td>Show value</td><td>Boolean</td><td>true</td>
 * <td>Whether or not to show the value as text</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogDial {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The minimum value of the dial.
    double min() default 0;

    //The maximum value of the dial.
    double max() default 100;

    //Whether or not to display the numeric value.
    boolean showValue() default true;
}
