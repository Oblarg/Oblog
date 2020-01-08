package io.github.oblarg.oblog.annotations;

import io.github.oblarg.oblog.Loggable;

import java.lang.annotation.*;


/**
 * Binds a setter or interactive object to its default interactive widget on Shuffleboard.  Defaults to a toggle
 * button for booleans, and a text field for numerics.  Numerics default to a value of 0, booleans to false.
 * <p>
 * For multi-argument setters, constructs a list containing the default widget type for each argument.  Names for each
 * argument's widget are taken from the method's parameter names.  Multi-argument setters are only supported through default
 * widgets - there is no support for specifying per-argument widget types.  If more detailed control is desired, write
 * individual setters.
 */
@Repeatable(Config.Configs.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
  /**
   * @return The name of the value on Shuffleboard; defaults to field or method name.
   */
  String name() default "NO_NAME";

  /**
   * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
   * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
   * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
   * currently supported for NT-only mode!
   */
  String tabName() default "DEFAULT";

  /**
   * @return Optional name of a method to call on the field (or return value of the method) to
   * obtain the actual setter that will be bound.  Useful if one does not desire to make an
   * entire object Loggable, but still wants to bind one of its setters.
   */
  String methodName() default "DEFAULT";

  /**
   * @return Parameter types of named method, if method name is provided.
   */
  Class<?>[] methodTypes() default {};

  /**
   * @return The default value for this setter if it is a single-argument boolean setter.
   */
  boolean defaultValueBoolean() default false;

  /**
   * @return The default value for this setter if it is a single-argument numeric setter.
   */
  double defaultValueNumeric() default 0;

  /**
   * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
   * in an object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int rowIndex() default -1;

  /**
   * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
   * in an object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int columnIndex() default -1;

  /**
   * @return The width of this widget.  WARNING: If position/size is specified for one widget
   * in an object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int width() default -1;

  /**
   * @return The height of this widget.  WARNING: If position/size is specified for one widget
   * in an object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int height() default -1;

  /**
   * @return The type of layout to use if the annotation target is a multi-parameter setter.  Must be either
   * "listLayout" or "gridLayout."
   */
  String multiArgLayoutType() default "listLayout";

  /**
   * @return The number of grid columns if the annotation target is a multi-parameter setter and the layout type is
   * set to grid.
   */
  int numGridColumns() default 3;

  /**
   * @return The number of grid rows if the annotation target is a multi-parameter setter and the layout type is
   * set to grid.
   */
  int numGridRows() default 3;

  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface Configs {
    Config[] value();
  }

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
  @Repeatable(NumberSliders.class)
  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface NumberSlider {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     * obtain the actual setter that will be bound.  Useful if one does not desire to make an
     * entire object Loggable, but still wants to bind one of its setters.
     */
    String methodName() default "DEFAULT";

    /**
     * @return Parameter types of named method, if method name is provided.
     */
    Class<?>[] methodTypes() default {};

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

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface NumberSliders {
    NumberSlider[] value();
  }

  /**
   * Displays a boolean with a large interactive toggle button.
   * <br>Supported types:
   * <ul>
   * <li>Boolean</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(ToggleButtons.class)
  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface ToggleButton {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     * obtain the actual setter that will be bound.  Useful if one does not desire to make an
     * entire object Loggable, but still wants to bind one of its setters.
     */
    String methodName() default "DEFAULT";

    /**
     * @return Parameter types of named method, if method name is provided.
     */
    Class<?>[] methodTypes() default {};

    /**
     * @return The default value that the setter will return prior to manipulation on the dashboard.
     */
    boolean defaultValue() default false;

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface ToggleButtons {
    ToggleButton[] value();
  }

  /**
   * Displays a boolean with a fixed-size toggle switch.
   * <br>Supported types:
   * <ul>
   * <li>Boolean</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(ToggleSwitches.class)
  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface ToggleSwitch {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     * obtain the actual setter that will be bound.  Useful if one does not desire to make an
     * entire object Loggable, but still wants to bind one of its setters.
     */
    String methodName() default "DEFAULT";

    /**
     * @return Parameter types of named method, if method name is provided.
     */
    Class<?>[] methodTypes() default {};

    /**
     * @return The default value that the setter will return prior to manipulation on the dashboard.
     */
    boolean defaultValue() default false;

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @interface ToggleSwitches {
    ToggleSwitch[] value();
  }

  /**
   * Displays a command with a toggle button. Pressing the button will start the command, and the
   * button will automatically release when the command completes.
   * <br>Supported types:
   * <ul>
   * <li>{@code Command}</li>
   * <li>{@code CommandGroup}</li>
   * <li>Any custom subclass of {@code Command} or {@code CommandGroup}</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(Commands.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface Command {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface Commands {
    Command[] value();
  }

  /**
   * Displays a PID command with a checkbox and an editor for the PIDF constants. Selecting the
   * checkbox will start the command, and the checkbox will automatically deselect when the command
   * completes.
   * <br>Supported types:
   * <ul>
   * <li>{@code PIDCommand}</li>
   * <li>Any custom subclass of {@code PIDCommand}</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(PIDCommands.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface PIDCommand {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface PIDCommands {
    PIDCommand[] value();
  }

  /**
   * Displays a PID controller with an editor for the PIDF constants and a toggle switch for
   * enabling and disabling the controller.
   * <br>Supported types:
   * <ul>
   * <li>{@code PIDController}</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(PIDControllers.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface PIDController {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface PIDControllers {
    PIDController[] value();
  }

  /**
   * Displays a relay with toggle buttons for each supported mode (off, on, forward, reverse).
   * <br>Supported types:
   * <ul>
   * <li>{@code Relay}</li>
   * </ul>
   * <br>This widget has no custom properties.
   */
  @Repeatable(Relays.class)
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @interface Relay {
    /**
     * @return The name of the value on Shuffleboard; defaults to field or method name.
     */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout is not desired.
     * Users should be careful to avoid namespace collisions if the default tab is not used.  Note that Log and
     * config annotations can be repeated to place widgets on multiple tabs.  Note also that this feature is NOT
     * currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget.  WARNING: If position/size is specified for one widget
     * in an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @interface Relays {
    Relay[] value();
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
