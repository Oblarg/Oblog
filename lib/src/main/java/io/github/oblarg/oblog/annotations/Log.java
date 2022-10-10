package io.github.oblarg.oblog.annotations;

import io.github.oblarg.oblog.Loggable;

import java.lang.annotation.*;

/**
 * Displays a value as its default widget type on Shuffleboard. Provides no configuration options.
 */
@Repeatable(Log.Logs.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
  /** @return The name of the value on Shuffleboard; defaults to field or method name. */
  String name() default "NO_NAME";

  /**
   * @return The name of the tab in which to place this widget, if the default inferred tab/layout
   *     is not desired. Users should be careful to avoid namespace collisions if the default tab is
   *     not used. Note that Log and config annotations can be repeated to place widgets on multiple
   *     tabs. Note also that this feature is NOT currently supported for NT-only mode!
   */
  String tabName() default "DEFAULT";

  /**
   * @return Optional name of a method to call on the field (or return value of the method) to
   *     obtain the actual value that will be logged. Useful if one does not desire to make an
   *     entire object Loggable, but still wants to log a value from it.
   */
  String methodName() default "DEFAULT";

  /**
   * @return The row in which this widget should be placed. WARNING: If position/size is specified
   *     for one widget in an object, it should be specified for all widgets in that object to avoid
   *     overlaps.
   */
  int rowIndex() default -1;

  /**
   * @return The column in which this widget should be placed. WARNING: If position/size is
   *     specified for one widget in an object, it should be specified for all widgets in that
   *     object to avoid overlaps.
   */
  int columnIndex() default -1;

  /**
   * @return The width of this widget. WARNING: If position/size is specified for one widget in an
   *     object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int width() default -1;

  /**
   * @return The height of this widget. WARNING: If position/size is specified for one widget in an
   *     object, it should be specified for all widgets in that object to avoid overlaps.
   */
  int height() default -1;

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Logs {
    Log[] value();
  }

  /**
   * Displays a number with a view-only bar. <br>
   * Supported types:
   *
   * <ul>
   *   <li>Number
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>
   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Min</td><td>Number</td><td>-1.0</td><td>The minimum value of the bar</td></tr>
   * <tr><td>Max</td><td>Number</td><td>1.0</td><td>The maximum value of the bar</td></tr>
   * <tr><td>Center</td><td>Number</td><td>0</td><td>The center ("zero") value of the bar</td></tr>
   * </table>
   */
  @Repeatable(NumberBars.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface NumberBar {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs. Note also that this feature is NOT currently supported for NT-only mode!
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The minimum value of the number bar. */
    double min() default -1;

    /** @return The maximum value of the number bar. */
    double max() default 1;

    /** @return The center value of the number bar. */
    double center() default 0;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface NumberBars {
    NumberBar[] value();
  }

  /**
   * Displays a number with a view-only dial. Displayed values are rounded to the nearest integer.
   * <br>
   * Supported types:
   *
   * <ul>
   *   <li>Number
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Min</td><td>Number</td><td>0</td><td>The minimum value of the dial</td></tr>
   * <tr><td>Max</td><td>Number</td><td>100</td><td>The maximum value of the dial</td></tr>
   * <tr><td>Show value</td><td>Boolean</td><td>true</td>
   * <td>Whether or not to show the value as text</td></tr>
   * </table>
   */
  @Repeatable(Dials.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Dial {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The minimum value of the dial. */
    double min() default 0;

    /** @return The maximum value of the dial. */
    double max() default 100;

    /** @return Whether or not to display the numeric value. */
    boolean showValue() default true;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Dials {
    Dial[] value();
  }

  /**
   * Displays a number with a graph. <strong>NOTE:</strong> graphs can be taxing on the computer
   * running the dashboard. Keep the number of visible data points to a minimum. Making the widget
   * smaller also helps with performance, but may cause the graph to become difficult to read. <br>
   * Supported types:
   *
   * <ul>
   *   <li>Number
   *   <li>Number array
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Visible time</td><td>Number</td><td>30</td>
   * <td>How long, in seconds, should past data be visible for</td></tr>
   * </table>
   */
  @Repeatable(Graphs.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Graph {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Window length of past data displayed in the graph. */
    double visibleTime() default 30;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Graphs {
    Graph[] value();
  }

  /**
   * Displays a boolean value as a large colored box. <br>
   * Supported types:
   *
   * <ul>
   *   <li>Boolean
   * </ul>
   *
   * <br>
   * Custom properties:
   *
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
  @Repeatable(BooleanBoxes.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface BooleanBox {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The color of the box when true. */
    String colorWhenTrue() default "green";

    /** @return The color of the box when false. */
    String colorWhenFalse() default "red";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface BooleanBoxes {
    BooleanBox[] value();
  }

  /**
   * Displays an analog input or a raw number with a number bar. <br>
   * Supported types:
   *
   * <ul>
   *   <li>Number
   *   <li>{@link edu.wpi.first.wpilibj.AnalogInput}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

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
  @Repeatable(VoltageViews.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface VoltageView {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The minimum value of the bar. */
    double min() default 0;

    /** @return The maximum value of the bar. */
    double max() default 5;

    /** @return The center ("zero") value of the bar. */
    double center() default 0;

    /** @return The orientation of the bar. Either "HORIZONTAL" or "VERTICAL". */
    String orientation() default "HORIZONTAL";

    /** @return Number of tick marks */
    int numTicks() default 5;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface VoltageViews {
    VoltageView[] value();
  }

  /**
   * Displays a {@link edu.wpi.first.wpilibj.PowerDistribution PowerDistributionPanel}. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.PowerDistribution}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>
   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Show voltage and current values</td><td>Boolean</td><td>true</td>
   * <td>Whether or not to display the voltage and current draw</td></tr>
   * </table>
   */
  @Repeatable(PDPs.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface PowerDistribution {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Whether to show the voltage and current values. */
    boolean showVoltageAndCurrent() default true;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface PDPs {
    PowerDistribution[] value();
  }

  /**
   * Displays an {@link edu.wpi.first.wpilibj.Encoder} displaying its speed, total travelled
   * distance, and its distance per tick. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.Encoder}
   * </ul>
   *
   * <br>
   * This widget has no custom properties.
   */
  @Repeatable(Encoders.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Encoder {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Encoders {
    Encoder[] value();
  }

  /**
   * Displays a {@link edu.wpi.first.wpilibj.motorcontrol.MotorController MotorController}. The speed controller
   * will be controllable from the dashboard when test mode is enabled, but will otherwise be
   * view-only. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.PWMMotorController}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.DMC60}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.Jaguar}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.SD540}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.Spark}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.Talon}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.Victor}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.VictorSP}
   *   <li>{@link edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Orientation</td><td>String</td><td>"HORIZONTAL"</td>
   * <td>One of {@code ["HORIZONTAL", "VERTICAL"]}</td></tr>
   * </table>
   */
  @Repeatable(MotorControllers.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface MotorController {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The orientation of the bar. Either "HORIZONTAL" or "VERTICAL". */
    String orientation() default "HORIZONTAL";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface MotorControllers {
    MotorController[] value();
  }

  /**
   * Displays an accelerometer with a number bar displaying the magnitude of the acceleration and
   * text displaying the exact value. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.AnalogAccelerometer}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

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
  @Repeatable(Accelerometers.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Accelerometer {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The minimum acceleration value to display. */
    double min() default -1;

    /** @return The maximum acceleration value to display. */
    double max() default 1;

    /** @return Whether to show or hide the acceleration values. */
    boolean showValue() default true;

    /** @return How many digits to display after the decimal point. */
    int precision() default 2;

    /** @return Whether to show the tick marks on the number bars. */
    boolean showTicks() default false;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Accelerometers {
    Accelerometer[] value();
  }

  /**
   * Displays a 3-axis accelerometer with a number bar for each axis' acceleration. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.ADXL345_I2C}
   *   <li>{@link edu.wpi.first.wpilibj.ADXL345_SPI}
   *   <li>{@link edu.wpi.first.wpilibj.ADXL362}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Range</td><td>{@link edu.wpi.first.wpilibj.interfaces.Accelerometer.Range}</td><td>k16G</td><td>The accelerometer range</td></tr>
   * <tr><td>Show value</td><td>Boolean</td><td>true</td>
   * <td>Show or hide the acceleration values</td></tr>
   * <tr><td>Precision</td><td>Number</td><td>2</td>
   * <td>How many numbers to display after the decimal point</td></tr>
   * <tr><td>Show tick marks</td><td>Boolean</td><td>false</td>
   * <td>Show or hide the tick marks on the number bars</td></tr>
   * </table>
   */
  @Repeatable(ThreeAxisAccelerometers.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface ThreeAxisAccelerometer {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return The range of the accelerometer. */
    edu.wpi.first.wpilibj.interfaces.Accelerometer.Range range() default
        edu.wpi.first.wpilibj.interfaces.Accelerometer.Range.k16G;

    /** @return Whether to show or hide the acceleration values. */
    boolean showValue() default true;

    /** @return How many digits to display after the decimal point. */
    int precision() default 2;

    /** @return Whether to show the tick marks on the number bars. */
    boolean showTicks() default false;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface ThreeAxisAccelerometers {
    ThreeAxisAccelerometer[] value();
  }

  /**
   * Displays a gyro with a dial from 0 to 360 degrees. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.ADXRS450_Gyro}
   *   <li>{@link edu.wpi.first.wpilibj.AnalogGyro}
   *   <li>Any custom subclass of {@code GyroBase} (such as a MXP gyro)
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Major tick spacing</td><td>Number</td><td>45</td><td>Degrees</td></tr>
   * <tr><td>Starting angle</td><td>Number</td><td>180</td>
   * <td>How far to rotate the entire dial, in degrees</td></tr>
   * <tr><td>Show tick mark ring</td><td>Boolean</td><td>true</td></tr>
   * </table>
   */
  @Repeatable(Gyros.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Gyro {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Spacing of the major ticks on the compass. */
    double majorTickSpacing() default 45;

    /** @return Offset of the dial, in degrees. */
    double startingAngle() default 180;

    /** @return Whether to show the tick marks. */
    boolean showTicks() default true;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Gyros {
    Gyro[] value();
  }

  /**
   * Displays a differential drive with a widget that displays the speed of each side of the
   * drivebase and a vector for the direction and rotation of the drivebase. The widget will be
   * controllable if the robot is in test mode. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.drive.DifferentialDrive}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Number of wheels</td><td>Number</td><td>4</td><td>Must be a positive even integer
   * </td></tr>
   * <tr><td>Wheel diameter</td><td>Number</td><td>80</td><td>Pixels</td></tr>
   * <tr><td>Show velocity vectors</td><td>Boolean</td><td>true</td></tr>
   * </table>
   */
  @Repeatable(DifferentialDrives.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface DifferentialDrive {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Number of wheels. Must be a positive, even integer. */
    int numWheels() default 4;

    /** @return Wheel diameter in pixels. */
    double wheelDiameter() default 80;

    /** @return Whether to show the velocity vectors. */
    boolean showVel() default true;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface DifferentialDrives {
    DifferentialDrive[] value();
  }

  /**
   * Displays a mecanum drive with a widget that displays the speed of each wheel, and vectors for
   * the direction and rotation of the drivebase. The widget will be controllable if the robot is in
   * test mode. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.drive.MecanumDrive}
   * </ul>
   *
   * <br>
   * Custom properties:
   *
   * <table>
   * <caption></caption>

   * <tr><th>Name</th><th>Type</th><th>Default Value</th><th>Notes</th></tr>
   * <tr><td>Show velocity vectors</td><td>Boolean</td><td>true</td></tr>
   * </table>
   */
  @Repeatable(MecanumDrives.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface MecanumDrive {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Whether to show the velocity vectors. */
    boolean showVel() default true;

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface MecanumDrives {
    MecanumDrive[] value();
  }

  /**
   * Displays a camera stream. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@code VideoSource} (as long as it is streaming on an MJPEG server)
   * </ul>
   *
   * <br>
   * Custom properties:
   *
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
  @Repeatable(CameraStreams.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface CameraStream {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /** @return Whether to show the crosshairs. */
    boolean showCrosshairs() default true;

    /** @return Color of the crosshairs. */
    String crosshairColor() default "white";

    /** @return Whether to show the stream controls. */
    boolean showControls() default true;

    /**
     * @return Rotate the displayed image. Must be one of: "NONE", "QUARTER_CW", "QUARTER_CCW",
     *     "HALF".
     */
    String rotation() default "NONE";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface CameraStreams {
    CameraStream[] value();
  }

  /**
   * Logs an object as the value returned by its toString method. Useful for logging object types
   * not natively supported by Shuffleboard without having to implement {@code Sendable}.
   */
  @Repeatable(ToStrings.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface ToString {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface ToStrings {
    ToString[] value();
  }

  /**
   * Suppresses the log tab/layout corresponding to this {@link Loggable} instance (or all instances
   * of the type).
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.TYPE})
  @interface Exclude {}

  /** Overrides a class-level Exclude annotation for an individual field. */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  @interface Include {}
    /**
   * Displays the position of robot on the playing field. <br>
   * Supported types:
   *
   * <ul>
   *   <li>{@link edu.wpi.first.wpilibj.smartdashboard.Field2d}
   * </ul>
   *
   * <br>
   */
  @Repeatable(Field2ds.class)
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Field2d {
    /** @return The name of the value on Shuffleboard; defaults to field or method name. */
    String name() default "NO_NAME";

    /**
     * @return The name of the tab in which to place this widget, if the default inferred tab/layout
     *     is not desired. Users should be careful to avoid namespace collisions if the default tab
     *     is not used. Note that Log and config annotations can be repeated to place widgets on
     *     multiple tabs.
     */
    String tabName() default "DEFAULT";

    /**
     * @return Optional name of a method to call on the field (or return value of the method) to
     *     obtain the actual value that will be logged. Useful if one does not desire to make an
     *     entire object Loggable, but still wants to log a value from it.
     */
    String methodName() default "DEFAULT";

    /**
     * @return The row in which this widget should be placed. WARNING: If position/size is specified
     *     for one widget in an object, it should be specified for all widgets in that object to
     *     avoid overlaps.
     */
    int rowIndex() default -1;

    /**
     * @return The column in which this widget should be placed. WARNING: If position/size is
     *     specified for one widget in an object, it should be specified for all widgets in that
     *     object to avoid overlaps.
     */
    int columnIndex() default -1;

    /**
     * @return The width of this widget. WARNING: If position/size is specified for one widget in an
     *     object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int width() default -1;

    /**
     * @return The height of this widget. WARNING: If position/size is specified for one widget in
     *     an object, it should be specified for all widgets in that object to avoid overlaps.
     */
    int height() default -1;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  @interface Field2ds {
    Field2d[] value();
  }
}
