import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Displays an accelerometer with a number bar displaying the magnitude of the acceleration and
 * text displaying the exact value.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.AnalogAccelerometer}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Min</td><td>Number</td><td>-1</td>
 * <td>The minimum acceleration value to display</td></tr>
 * <tr><td>Max</td><td>Number</td><td>1</td>
 * <td>The maximum acceleration value to display</td></tr>
 * <tr><td>Show text</td><td>Boolean</td><td>true</td>
 * <td>Show or hide the acceleration values</td></tr>
 * <tr><td>Precision</td><td>Number</td><td>2</td>
 * <td>How many numbers to display after the decimal point</td></tr>
 * <tr><td>Show tick marks</td><td>Boolean</td><td>false</td>
 * <td>Show or hide the tick marks on the number bars</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogAccelerometer {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The minimum acceleration value to display.
    double min() default -1;

    //The maximum acceleration value to display.
    double max() default 1;

    //Whether to show or hide the acceleration values.
    boolean showText() default true;

    //How many digits to display after the decimal point.
    int precision() default 2;

    //Whether to show the tick marks on the number bars.
    boolean showTicks() default false;
}
