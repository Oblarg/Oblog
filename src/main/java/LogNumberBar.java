import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Displays a number with a view-only bar.
 * <br>Supported types:
 * <ul>
 * <li>Number</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Min</td><td>Number</td><td>-1.0</td><td>The minimum value of the bar</td></tr>
 * <tr><td>Max</td><td>Number</td><td>1.0</td><td>The maximum value of the bar</td></tr>
 * <tr><td>Center</td><td>Number</td><td>0</td><td>The center ("zero") value of the bar</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogNumberBar {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The minimum value of the number bar.
    double min() default -1;

    //The maximum value of the number bar.
    double max() default 1;

    //The center value of the number bar.
    double center() default 0;
}
