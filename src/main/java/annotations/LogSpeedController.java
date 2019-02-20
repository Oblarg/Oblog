package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a {@link edu.wpi.first.wpilibj.SpeedController SpeedController}. The speed controller
 * will be controllable from the dashboard when test mode is enabled, but will otherwise be
 * view-only.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.PWMSpeedController}</li>
 * <li>{@link edu.wpi.first.wpilibj.DMC60}</li>
 * <li>{@link edu.wpi.first.wpilibj.Jaguar}</li>
 * <li>{@link edu.wpi.first.wpilibj.PWMTalonSRX}</li>
 * <li>{@link edu.wpi.first.wpilibj.PWMVictorSPX}</li>
 * <li>{@link edu.wpi.first.wpilibj.SD540}</li>
 * <li>{@link edu.wpi.first.wpilibj.Spark}</li>
 * <li>{@link edu.wpi.first.wpilibj.Talon}</li>
 * <li>{@link edu.wpi.first.wpilibj.Victor}</li>
 * <li>{@link edu.wpi.first.wpilibj.VictorSP}</li>
 * <li>{@link edu.wpi.first.wpilibj.SpeedControllerGroup}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Orientation</td><td>String</td><td>"HORIZONTAL"</td>
 * <td>One of {@code ["HORIZONTAL", "VERTICAL"]}</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogSpeedController {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The orientation of the bar.  Either "HORIZONTAL" or "VERTICAL".
    String orientation() default "HORIZONTAL";
}
