import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a boolean value as a large colored box.
 * <br>Supported types:
 * <ul>
 * <li>Boolean</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Color when true</td><td>Color</td><td>"green"</td>
 * <td>Can be specified as a string ({@code "#00FF00"}) or a rgba integer ({@code 0x00FF0000})
 * </td></tr>
 * <tr><td>Color when false</td><td>Color</td><td>"red"</td>
 * <td>Can be specified as a string or a number</td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogBooleanBox {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //The color of the box when true.
    String colorWhenTrue() default "green";

    //The color of the box when false.
    String colorWhenFalse() default "red";
}
