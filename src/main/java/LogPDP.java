import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Displays a {@link edu.wpi.first.wpilibj.PowerDistributionPanel PowerDistributionPanel}.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.first.wpilibj.PowerDistributionPanel}</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Show voltage and current values</td><td>Boolean</td><td>true</td>
 * <td>Whether or not to display the voltage and current draw</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogPDP {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";
}