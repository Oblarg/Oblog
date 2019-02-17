import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Displays an analog input or a raw number with a number bar.
 * <br>Supported types:
 * <ul>
 * <li>Number</li>
 * <li>{@link edu.wpi.first.wpilibj.AnalogInput}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Min</td><td>Number</td><td>0</td><td>The minimum value of the bar</td></tr>
 * <tr><td>Max</td><td>Number</td><td>5</td><td>The maximum value of the bar</td></tr>
 * <tr><td>Center</td><td>Number</td><td>0</td><td>The center ("zero") value of the bar</td></tr>
 * <tr><td>Orientation</td><td>String</td><td>"HORIZONTAL"</td>
 * <td>The orientation of the bar. One of {@code ["HORIZONTAL", "VERTICAL"]}</td></tr>
 * <tr><td>Number of tick marks</td><td>Number</td><td>5</td>
 * <td>The number of discrete ticks on the bar</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogVoltageView {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The minimum value of the bar.
    double min() default 0;

    //The maximum value of the bar.
    double max() default 5;

    //The center ("zero") value of the bar.
    double center() default 0;

    //The orientation of the bar.  Either "HORIZONTAL" or "VERTICAL".
    String orientation() default "HORIZONTAL";

    //Number of tick marks
    int numTicks() default 5;
}
