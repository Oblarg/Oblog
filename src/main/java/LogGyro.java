import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a gyro with a dial from 0 to 360 degrees.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.ADXRS450_Gyro}</li>
 * <li>{@link edu.wpi.first.wpilibj.AnalogGyro}</li>
 * <li>Any custom subclass of {@code GyroBase} (such as a MXP gyro)</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Major tick spacing</td><td>Number</td><td>45</td><td>Degrees</td></tr>
 * <tr><td>Starting angle</td><td>Number</td><td>180</td>
 * <td>How far to rotate the entire dial, in degrees</td></tr>
 * <tr><td>Show tick mark ring</td><td>Boolean</td><td>true</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogGyro {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Spacing of the major ticks on the compass.
    double majorTickSpacing() default 45;

    //Offset of the dial, in degrees.
    double startingAngle() default 180;

    //Whether to show the tick marks.
    boolean showTicks() default true;
}
