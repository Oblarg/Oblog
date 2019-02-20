package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Displays a camera stream.
 * <br>Supported types:
 * <ul>
 * <li>{@link edu.wpi.cscore.VideoSource} (as long as it is streaming on an MJPEG server)</li>
 * </ul>
 * <br>Custom properties:
 * <table>
 * <caption></caption>
 * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
 * <tr><td>Show crosshair</td><td>Boolean</td><td>true</td>
 * <td>Show or hide a crosshair on the image</td></tr>
 * <tr><td>Crosshair color</td><td>Color</td><td>"white"</td>
 * <td>Can be a string or a rgba integer</td></tr>
 * <tr><td>Show controls</td><td>Boolean</td><td>true</td><td>Show or hide the stream controls
 * </td></tr>
 * <tr><td>Rotation</td><td>String</td><td>"NONE"</td>
 * <td>Rotates the displayed image. One of {@code ["NONE", "QUARTER_CW", "QUARTER_CCW", "HALF"]}
 * </td></tr>
 * </table>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface LogCameraStream {
    //The name of the value on Shuffleboard; defaults to field or method name.
    String name() default "NO_NAME";

    //Whether to show the crosshairs.
    boolean showCrosshairs() default true;

    //Color of the crosshairs.
    String crosshairColor() default "white";

    //Whether to show the stream controls.
    boolean showControls() default true;

    //Rotate the displayed image.  Must be one of: "NONE", "QUARTER_CW", "QUARTER_CCW", "HALF".
    String rotation() default "NONE";
}
