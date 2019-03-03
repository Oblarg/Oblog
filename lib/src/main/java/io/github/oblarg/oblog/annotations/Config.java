package io.github.oblarg.oblog.annotations;

import io.github.oblarg.oblog.Loggable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Binds a setter or interactive object to its default interactive widget on Shuffleboard.  Defaults to a toggle
 * button for booleans, and a text field for numerics.  Numerics default to a value of 0, booleans to false.
 * <p>
 * For multi-argument setters, constructs a list containing the default widget type for each argument.  Names for each
 * argument's widget are taken from the method's parameter names.  Multi-argument setters are only supported through default
 * widgets - there is no support for specifying per-argument widget types.  If more detailed control is desired, write
 * individual setters.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";


    /**
     * Displays a setter with a controllable slider.
     * <br>Supported types:
     * <ul>
     * <li>Number</li>
     * </ul>
     * <br>Custom properties:
     * <table>
     * <caption></caption>
     * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
     * <tr><td>Min</td><td>Number</td><td>-1.0</td><td>The minimum value of the slider</td></tr>
     * <tr><td>Max</td><td>Number</td><td>1.0</td><td>The maximum value of the slider</td></tr>
     * <tr><td>Block increment</td><td>Number</td><td>0.0625</td>
     * <td>How much to move the slider by with the arrow keys</td></tr>
     * </table>
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface NumberSlider {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";

        /**
         * @return The minimum value of the slider.
         */
        double min() default -1;

        /**
         * @return The maximum value of the slider.
         */
        double max() default 1;

        /**
         * @return The increment by which the arrow keys move the slider.
         */
        double blockIncrement() default .0625;

        /**
         * @return The default value that the setter will return prior to manipulation on the dashboard.
         */
        double defaultValue() default 0;
    }


    /**
     * Displays a boolean with a large interactive toggle button.
     * <br>Supported types:
     * <ul>
     * <li>Boolean</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ToggleButton {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";

        /**
         * @return The default value that the setter will return prior to manipulation on the dashboard.
         */
        boolean defaultValue() default false;
    }


    /**
     * Displays a boolean with a fixed-size toggle switch.
     * <br>Supported types:
     * <ul>
     * <li>Boolean</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ToggleSwitch {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";

        /**
         * @return The default value that the setter will return prior to manipulation on the dashboard.
         */
        boolean defaultValue() default false;
    }


    /**
     * Displays a command with a toggle button. Pressing the button will start the command, and the
     * button will automatically release when the command completes.
     * <br>Supported types:
     * <ul>
     * <li>{@link edu.wpi.first.wpilibj.command.Command}</li>
     * <li>{@link edu.wpi.first.wpilibj.command.CommandGroup}</li>
     * <li>Any custom subclass of {@code Command} or {@code CommandGroup}</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Command {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";
    }


    /**
     * Displays a PID command with a checkbox and an editor for the PIDF constants. Selecting the
     * checkbox will start the command, and the checkbox will automatically deselect when the command
     * completes.
     * <br>Supported types:
     * <ul>
     * <li>{@link edu.wpi.first.wpilibj.command.PIDCommand}</li>
     * <li>Any custom subclass of {@code PIDCommand}</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface PIDCommand {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";
    }


    /**
     * Displays a PID controller with an editor for the PIDF constants and a toggle switch for
     * enabling and disabling the controller.
     * <br>Supported types:
     * <ul>
     * <li>{@link edu.wpi.first.wpilibj.PIDController}</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface PIDController {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";
    }


    /**
     * Displays a relay with toggle buttons for each supported mode (off, on, forward, reverse).
     * <br>Supported types:
     * <ul>
     * <li>{@link edu.wpi.first.wpilibj.Relay}</li>
     * </ul>
     * <br>This widget has no custom properties.
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Relay {
        /**
         * @return The name of the value on Shuffleboard; defaults to field or method name.
         */
        String name() default "NO_NAME";
    }


    /**
     * Suppresses the config tab/layout corresponding to this {@link Loggable} instance (or all instances of the type).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @interface Exclude {
    }

    /**
     * Overrides a class-level Exclude annotation for an individual field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Include {
    }
}
