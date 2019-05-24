package io.github.oblarg.oblog;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.shuffleboard.*;
import io.github.oblarg.oblog.annotations.*;
import edu.wpi.first.networktables.NetworkTableEntry;

import static java.util.Map.entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("Duplicates")
public class Logger {

    private static boolean cycleWarningsEnabled = true;

    /**
     * Enables or disables warnings for cyclic references of loggables.  Kotlin users will likely want to disable these,
     * as kotlin singletons compile to cycles in the bytecode which cannot be explicitly broken with
     * {@link Log.Exclude}.
     *
     * @param enabled Whether to print a warning on configuration when cyclic references of loggables are detected.
     */
    public static void setCycleWarningsEnabled(boolean enabled) {
        cycleWarningsEnabled = enabled;
    }

    /**
     * Configure shuffleboard logging for the robot.  Should be called after all loggable objects have been
     * instantiated, e.g. at the end of robotInit.  Tabs for logging will be separate from tabs for config.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLogging(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     */

    public static void configureLogging(Object rootContainer) {
        configureLogging(LogType.LOG, true, rootContainer, new WrappedShuffleboard(), NetworkTableInstance.getDefault());
    }

    /**
     * Configure shuffleboard config for the robot.  Should be called after all loggable objects have been
     * instantiated, e.g. at the end of robotInit.  Tabs for config will be separate from tabs for logging.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureConfig(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     */
    public static void configureConfig(Object rootContainer) {
        configureLogging(LogType.CONFIG, true, rootContainer, new WrappedShuffleboard(), NetworkTableInstance.getDefault());
    }

    /**
     * Configure shuffleboard logging and config for the robot.  Should be called after all loggable objects have beeen
     * instantiated, e.g. at the end of robotInit.  Config and logging can either be given separate tabs, or all widgets
     * can be placed in the same tabs.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLoggingAndConfig(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     * @param separate      Whether to generate separate tabs for config and logging.  If true, log widgets will be placed in
     *                      tabs labeled "Log", and config widgets will be placed in tabs labeled "Config".
     */
    public static void configureLoggingAndConfig(Object rootContainer, boolean separate) {
        WrappedShuffleboard shuffleboard = new WrappedShuffleboard();
        configureLogging(LogType.LOG, separate, rootContainer, shuffleboard, NetworkTableInstance.getDefault());
        configureLogging(LogType.CONFIG, separate, rootContainer, shuffleboard, NetworkTableInstance.getDefault());
    }

    /**
     * Configures logging to send values over NetworkTables, but not to add widgets to Shuffleboard.  Use is the same
     * as {@link Logger#configureLogging(Object)}.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLogging(this)"
     * @param rootName      Name of the root NetworkTable.  io.github.oblarg.oblog.Loggable fields of rootContainer will be subtables.
     */
    public static void configureLoggingNTOnly(Object rootContainer, String rootName) {
        configureLogging(LogType.LOG, true, rootContainer, new NTShuffleboard(rootName), NetworkTableInstance.getDefault());
    }

    /**
     * Updates all entries.  Must be called periodically from the main robot loop.
     */
    public static void updateEntries() {
        entrySupplierMap.forEach((entry, supplier) -> entry.setValue(supplier.get()));
        setterRunner.runSynchronous();
    }

    /**
     * Registers a new entry.  To be called during initial logging configuration for any value that will
     * change during runtime.  Mostly for internal use, but can be used by advanced users in {@link Loggable#addCustomLogging(ShuffleboardContainerWrapper)}.
     *
     * @param entry    The entry to be updated.
     * @param supplier The supplier with which to update the entry.
     */
    public static void registerEntry(NetworkTableEntry entry, Supplier<Object> supplier) {
        entrySupplierMap.put(entry, supplier);
    }

    private static void configureLogging(LogType logType,
                                         boolean separate,
                                         Object rootContainer,
                                         ShuffleboardWrapper shuffleboard,
                                         NetworkTableInstance nt) {

        ShuffleboardContainerWrapper bin;

        if (rootContainer instanceof Loggable) {
            bin = shuffleboard.getTab(((Loggable) rootContainer).configureLogName());
        } else {
            bin = shuffleboard.getTab(rootContainer.getClass().getSimpleName());
        }

        switch (logType) {
            case LOG:
                logFieldsAndMethods(rootContainer,
                        rootContainer.getClass(),
                        bin,
                        new HashSet<>(),
                        new HashSet<>());
                break;
            case CONFIG:
                configFieldsAndMethods(rootContainer,
                        rootContainer.getClass(),
                        bin,
                        nt,
                        new HashSet<>(),
                        new HashSet<>());
                break;
        }

        Consumer<Loggable> log = (toLog) -> logLoggable(logType,
                separate,
                toLog,
                toLog.getClass(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                shuffleboard,
                nt,
                null,
                new HashSet<>(Collections.singletonList(toLog)));

        for (Field field : rootContainer.getClass().getDeclaredFields()) {
            if (isLoggableClassOrArrayOrCollection(field, rootContainer) && isIncluded(field, logType)) {
                field.setAccessible(true);
                if (field.getType().isArray()) {
                    Loggable[] toLogs;
                    try {
                        toLogs = (Loggable[]) field.get(rootContainer);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        toLogs = new Loggable[0];
                    }
                    for (Loggable toLog : toLogs) {
                        {
                            log.accept(toLog);
                        }
                    }
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    Collection<Loggable> toLogs;
                    try {
                        toLogs = (Collection) field.get(rootContainer);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        toLogs = new HashSet<>();
                    }
                    for (Loggable toLog : toLogs) {
                        log.accept(toLog);
                    }
                } else {
                    Loggable toLog;
                    try {
                        toLog = (Loggable) field.get(rootContainer);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        toLog = null;
                    }
                    log.accept(toLog);
                }
            }
        }
    }

    static void configureLoggingTest(LogType logType, Object rootContainer, ShuffleboardWrapper shuffleboard, NetworkTableInstance nt) {
        configureLogging(logType, true, rootContainer, shuffleboard, nt);
    }

    /**
     * A map of the suppliers that are used to update each entry.
     */
    private static final Map<NetworkTableEntry, Supplier<Object>> entrySupplierMap = new HashMap<>();

    enum LogType {
        LOG, CONFIG
    }

    private static SetterRunner setterRunner = new SetterRunner();

    @FunctionalInterface
    private interface FieldProcessor {
        void processField(Supplier<Object> supplier, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    @FunctionalInterface
    private interface BooleanSetterProcessor {
        void processBooleanSetter(Consumer<Boolean> setter, Annotation params, ShuffleboardContainerWrapper bin, NetworkTableInstance nt, String name);
    }

    @FunctionalInterface
    private interface NumericSetterProcessor {
        void processNumericSetter(Consumer<Number> setter, Annotation params, ShuffleboardContainerWrapper bin, NetworkTableInstance nt, String name);
    }

    @FunctionalInterface
    private interface SetterProcessor {
        void processSetter(Consumer<Object> setter, Annotation params, ShuffleboardContainerWrapper bin, NetworkTableInstance nt, String name, boolean isBoolean);
    }

    private static final Map<Class<? extends Annotation>, SetterProcessor> configSetterHandler = Map.ofEntries(
            entry(Config.class, (setter, rawParams, bin, nt, name, isBoolean) -> {
                if (isBoolean) {
                    Config params = (Config) rawParams;
                    bin = params.tabName().equals("DEFAULT") ? bin :
                            new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                    NetworkTableEntry entry = bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValueBoolean())
                            .withWidget(BuiltInWidgets.kToggleButton.getWidgetName())
                            .withPosition(params.columnIndex(), params.rowIndex())
                            .withSize(params.width(), params.height()).getEntry();
                    nt.addEntryListener(
                            entry,
                            (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                            EntryListenerFlags.kUpdate
                    );
                    setter.accept(params.defaultValueBoolean());
                } else {
                    Config params = (Config) rawParams;
                    bin = params.tabName().equals("DEFAULT") ? bin :
                            new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                    NetworkTableEntry entry = bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValueNumeric())
                            .withWidget(BuiltInWidgets.kTextView.getWidgetName())
                            .withPosition(params.columnIndex(), params.rowIndex())
                            .withSize(params.width(), params.height()).getEntry();
                    nt.addEntryListener(
                            entry,
                            (entryNotification) -> setterRunner.execute(() -> setter.accept((Number) entryNotification.value.getValue())),
                            EntryListenerFlags.kUpdate
                    );
                    setter.accept(params.defaultValueNumeric());
                }
            }),
            entry(Config.ToggleButton.class, (setter, rawParams, bin, nt, name, isBoolean) -> {
                Config.ToggleButton params = (Config.ToggleButton) rawParams;
                bin = params.tabName().equals("DEFAULT") ? bin :
                        new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                NetworkTableEntry entry = bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                        .withWidget(BuiltInWidgets.kToggleButton.getWidgetName())
                        .withPosition(params.columnIndex(), params.rowIndex())
                        .withSize(params.width(), params.height()).getEntry();
                nt.addEntryListener(
                        entry,
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            }),
            entry(Config.ToggleSwitch.class, (setter, rawParams, bin, nt, name, isBoolean) -> {
                Config.ToggleSwitch params = (Config.ToggleSwitch) rawParams;
                bin = params.tabName().equals("DEFAULT") ? bin :
                        new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                NetworkTableEntry entry = bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                        .withWidget(BuiltInWidgets.kToggleSwitch.getWidgetName())
                        .withPosition(params.columnIndex(), params.rowIndex())
                        .withSize(params.width(), params.height()).getEntry();
                nt.addEntryListener(
                        entry,
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            }),
            entry(Config.NumberSlider.class, (setter, rawParams, bin, nt, name, isBoolean) -> {
                Config.NumberSlider params = (Config.NumberSlider) rawParams;
                bin = params.tabName().equals("DEFAULT") ? bin :
                        new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                NetworkTableEntry entry = bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                        .withWidget(BuiltInWidgets.kNumberSlider.getWidgetName())
                        .withProperties(Map.of(
                                "min", params.min(),
                                "max", params.max(),
                                "blockIncrement", params.blockIncrement()))
                        .withPosition(params.columnIndex(), params.rowIndex())
                        .withSize(params.width(), params.height())
                        .getEntry();
                nt.addEntryListener(
                        entry,
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((Number) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            })
    );


    private static final Map<Class<? extends Annotation>, FieldProcessor> configFieldHandler = Map.ofEntries(
            entry(Config.class,
                    (supplier, rawParams, bin, name) -> {
                        Config params = (Config) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Config.Command.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.Command params = (Config.Command) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kCommand.getWidgetName())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Config.PIDCommand.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.PIDCommand params = (Config.PIDCommand) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kPIDCommand.getWidgetName())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Config.PIDController.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.PIDController params = (Config.PIDController) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kPIDController.getWidgetName())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Config.Relay.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.Relay params = (Config.Relay) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kRelay.getWidgetName())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    })
    );

    private static final Map<Class<? extends Annotation>, FieldProcessor> logHandler = Map.ofEntries(
            entry(Log.class,
                    (supplier, rawParams, bin, name) -> {
                        Log params = (Log) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        if (getFromMethod(supplier, params.methodName()).get() instanceof Sendable) {
                            bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                    (Sendable) getFromMethod(supplier, params.methodName()).get())
                                    .withPosition(params.rowIndex(), params.columnIndex())
                                    .withSize(params.width(), params.height())
                                    .withPosition(params.columnIndex(), params.rowIndex())
                                    .withSize(params.width(), params.height());
                        } else {
                            Logger.registerEntry(
                                    bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                            getFromMethod(supplier, params.methodName()).get())
                                            .withPosition(params.columnIndex(), params.rowIndex())
                                            .withSize(params.width(), params.height()).getEntry(),
                                    () -> getFromMethod(supplier, params.methodName()).get());
                        }
                    }),
            entry(Log.NumberBar.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.NumberBar params = (Log.NumberBar) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                        getFromMethod(supplier, params.methodName()).get())
                                        .withWidget(BuiltInWidgets.kNumberBar.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "center", params.center()))
                                        .withPosition(params.columnIndex(), params.rowIndex())
                                        .withSize(params.width(), params.height())
                                        .getEntry(),
                                () -> getFromMethod(supplier, params.methodName()).get());
                    }),
            entry(Log.Dial.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Dial params = (Log.Dial) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                        getFromMethod(supplier, params.methodName()).get())
                                        .withWidget(BuiltInWidgets.kDial.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "showValue", params.showValue()))
                                        .withPosition(params.columnIndex(), params.rowIndex())
                                        .withSize(params.width(), params.height())
                                        .getEntry(),
                                () -> getFromMethod(supplier, params.methodName()).get());
                    }),
            entry(Log.Graph.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Graph params = (Log.Graph) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                        getFromMethod(supplier, params.methodName()).get())
                                        .withWidget(BuiltInWidgets.kGraph.getWidgetName())
                                        .withProperties(Map.of(
                                                "Visible time", params.visibleTime()))
                                        .withPosition(params.columnIndex(), params.rowIndex())
                                        .withSize(params.width(), params.height())
                                        .getEntry(),
                                () -> getFromMethod(supplier, params.methodName()).get());
                    }),
            entry(Log.BooleanBox.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.BooleanBox params = (Log.BooleanBox) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                        getFromMethod(supplier, params.methodName()).get())
                                        .withWidget(BuiltInWidgets.kBooleanBox.getWidgetName())
                                        .withProperties(Map.of(
                                                "colorWhenTrue", params.colorWhenTrue(),
                                                "colorWhenFalse", params.colorWhenFalse()))
                                        .withPosition(params.columnIndex(), params.rowIndex())
                                        .withSize(params.width(), params.height())
                                        .getEntry(),
                                () -> getFromMethod(supplier, params.methodName()).get());
                    }),
            entry(Log.VoltageView.class,
                    (supplier, rawParams, bin, name) -> {
                        if (supplier.get() instanceof AnalogInput) {
                            Log.VoltageView params = (Log.VoltageView) rawParams;
                            bin = params.tabName().equals("DEFAULT") ? bin :
                                    new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                            supplier = getFromMethod(supplier, params.methodName());
                            bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                    .withWidget(BuiltInWidgets.kVoltageView.getWidgetName())
                                    .withProperties(Map.of(
                                            "min", params.min(),
                                            "max", params.max(),
                                            "center", params.center(),
                                            "orientation", params.orientation(),
                                            "numberOfTickMarks", params.numTicks()))
                                    .withPosition(params.columnIndex(), params.rowIndex())
                                    .withSize(params.width(), params.height());
                        } else {
                            Log.VoltageView params = (Log.VoltageView) rawParams;
                            bin = params.tabName().equals("DEFAULT") ? bin :
                                    new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                            final Supplier<Object> supplierFinal = supplier;
                            Logger.registerEntry(
                                    bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                            getFromMethod(supplier, params.methodName()).get())
                                            .withWidget(BuiltInWidgets.kVoltageView.getWidgetName())
                                            .withProperties(Map.of(
                                                    "min", params.min(),
                                                    "max", params.max(),
                                                    "center", params.center(),
                                                    "orientation", params.orientation(),
                                                    "numberOfTickMarks", params.numTicks()))
                                            .withPosition(params.columnIndex(), params.rowIndex())
                                            .withSize(params.width(), params.height())
                                            .getEntry(),
                                    () -> getFromMethod(supplierFinal, params.name()));
                        }
                    }),
            entry(Log.PDP.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.PDP params = (Log.PDP) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kPowerDistributionPanel.getWidgetName())
                                .withProperties(Map.of(
                                        "showVoltageAndCurrentValues", params.showVoltageAndCurrent()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.Encoder.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Encoder params = (Log.Encoder) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kEncoder.getWidgetName())
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.SpeedController.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.SpeedController params = (Log.SpeedController) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kSpeedController.getWidgetName())
                                .withProperties(Map.of(
                                        "orientation", params.orientation()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.Accelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Accelerometer params = (Log.Accelerometer) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showText", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.ThreeAxisAccelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.ThreeAxisAccelerometer params = (Log.ThreeAxisAccelerometer) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.k3AxisAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "range", params.range(),
                                        "showValue", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.Gyro.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Gyro params = (Log.Gyro) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kGyro.getWidgetName())
                                .withProperties(Map.of(
                                        "majorTickSpacing", params.majorTickSpacing(),
                                        "startingAngle", params.startingAngle(),
                                        "showTickMarkRing", params.showTicks()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.DifferentialDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.DifferentialDrive params = (Log.DifferentialDrive) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kDifferentialDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "numberOfWheels", params.numWheels(),
                                        "wheelDiameter", params.wheelDiameter(),
                                        "showVelocityVectors", params.showVel()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.MecanumDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.MecanumDrive params = (Log.MecanumDrive) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), (Sendable) supplier.get())
                                .withWidget(BuiltInWidgets.kMecanumDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "showVelocityVectors", params.showVel()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.CameraStream.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.CameraStream params = (Log.CameraStream) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        supplier = getFromMethod(supplier, params.methodName());
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                SendableCameraWrapper.wrap((VideoSource) supplier.get()))
                                .withWidget(BuiltInWidgets.kCameraStream.getWidgetName())
                                .withProperties(Map.of(
                                        "showCrosshair", params.showCrosshairs(),
                                        "crosshairColor", params.crosshairColor(),
                                        "showControls", params.showControls(),
                                        "rotation", params.rotation()))
                                .withPosition(params.columnIndex(), params.rowIndex())
                                .withSize(params.width(), params.height());
                    }),
            entry(Log.ToString.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.ToString params = (Log.ToString) rawParams;
                        bin = params.tabName().equals("DEFAULT") ? bin :
                                new WrappedShuffleboardContainer(Shuffleboard.getTab(params.tabName()));
                        final Supplier supplierFinal = supplier;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(),
                                        getFromMethod(supplier, params.methodName()).get().toString())
                                        .withPosition(params.columnIndex(), params.rowIndex())
                                        .withSize(params.width(), params.height())
                                        .getEntry(),
                                () -> getFromMethod(supplierFinal, params.methodName()).get().toString());
                    })
    );

    private static final Map<Class, Function<Object, Object>> setterCaster = Map.ofEntries(
            entry(Integer.TYPE, (value) -> ((Number) value).intValue()),
            entry(Integer.class, (value) -> ((Number) value).intValue()),
            entry(Double.TYPE, (value) -> ((Number) value).doubleValue()),
            entry(Double.class, (value) -> ((Number) value).doubleValue()),
            entry(Float.TYPE, (value) -> ((Number) value).floatValue()),
            entry(Float.class, (value) -> ((Number) value).floatValue()),
            entry(Long.TYPE, (value) -> ((Number) value).longValue()),
            entry(Long.class, (value) -> ((Number) value).longValue()),
            entry(Short.TYPE, (value) -> ((Number) value).shortValue()),
            entry(Short.class, (value) -> ((Number) value).shortValue()),
            entry(Byte.TYPE, (value) -> ((Number) value).byteValue()),
            entry(Byte.class, (value) -> ((Number) value).byteValue()),
            entry(Boolean.TYPE, (value) -> value),
            entry(Boolean.class, (value) -> value)
    );

    private static final Map<Class, Object> setterDefaults = Map.ofEntries(
            entry(Integer.TYPE, 0),
            entry(Integer.class, 0),
            entry(Double.TYPE, 0.),
            entry(Double.class, 0.),
            entry(Float.TYPE, 0.f),
            entry(Float.class, 0.f),
            entry(Long.TYPE, 0L),
            entry(Long.class, 0L),
            entry(Short.TYPE, (short) 0),
            entry(Short.class, (short) 0),
            entry(Byte.TYPE, (byte) 0),
            entry(Byte.class, (byte) 0),
            entry(Boolean.TYPE, false),
            entry(Boolean.class, false)
    );

    private static void configFieldsAndMethods(Object loggable,
                                               Class loggableClass,
                                               ShuffleboardContainerWrapper bin,
                                               NetworkTableInstance nt,
                                               Set<Field> registeredFields,
                                               Set<Method> registeredMethods) {

        Set<Field> fields = Set.of(loggableClass.getDeclaredFields());
        Set<Method> methods = Set.of(loggableClass.getDeclaredMethods());

        for (Field field : fields) {
            field.setAccessible(true);
            //System.out.println(loggableClass + " " + field.getName() + " " + registeredFields.contains(field));
            if (!registeredFields.contains(field)) {
                registeredFields.add(field);
                for (Class type : configFieldHandler.keySet()) {
                    for (Annotation annotation : field.getAnnotationsByType(type)) {
                        FieldProcessor process = configFieldHandler.get(annotation.annotationType());
                        if (process != null) {
                            process.processField(
                                    () -> {
                                        try {
                                            return field.get(loggable);
                                        } catch (IllegalAccessException e) {
                                            return null;
                                        }
                                    },
                                    annotation,
                                    bin,
                                    field.getName());
                        }
                    }
                }
            }
        }

        for (Method method : methods) {
            if (method.getReturnType().equals(Void.TYPE) &&
                    method.getParameterTypes().length == 1 &&
                    setterCaster.containsKey(method.getParameterTypes()[0]) &&
                    !registeredMethods.contains(method)) {
                method.setAccessible(true);
                registeredMethods.add(method);
                for (Class type : configSetterHandler.keySet()) {
                    for (Annotation annotation : method.getAnnotationsByType(type)) {
                        SetterProcessor process = configSetterHandler.get(annotation.annotationType());
                        if (process != null) {
                            process.processSetter(
                                    (value) -> {
                                        try {
                                            method.invoke(loggable, setterCaster.get(method.getParameterTypes()[0]).apply(value));
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    annotation,
                                    bin,
                                    nt,
                                    method.getName(),
                                    method.getParameterTypes()[0].equals(Boolean.TYPE) || method.getParameterTypes()[0].equals(Boolean.class));
                        }
                    }
                }
            } else if (method.getReturnType().equals(Void.TYPE) &&
                    method.getParameterTypes().length > 1 &&
                    containsKeys(setterCaster, List.of((Object[]) method.getParameterTypes())) &&
                    !registeredMethods.contains(method)) {
                method.setAccessible(true);
                registeredMethods.add(method);
                for (Config annotation : method.getAnnotationsByType(Config.class)) {
                    if (annotation != null) {
                        ShuffleboardContainerWrapper list = bin.getLayout(
                                annotation.name().equals("NO_NAME") ? method.getName() : annotation.name(),
                                annotation.multiArgLayoutType().equals("listLayout") ? BuiltInLayouts.kList : BuiltInLayouts.kGrid)
                                .withPosition(annotation.columnIndex(), annotation.rowIndex())
                                .withSize(annotation.width(), annotation.height())
                                .withProperties(Map.ofEntries(
                                        entry("numberOfColumns", annotation.numGridColumns()),
                                        entry("numberOfRows", annotation.numGridRows())
                                ));
                        int numParams = method.getParameterCount();
                        List<Object> values = new ArrayList<>(numParams);
                        for (int i = 0; i < numParams; i++) {
                            Parameter parameter = method.getParameters()[i];
                            values.add(setterCaster.get(parameter.getType())
                                    .apply(setterDefaults.get(parameter.getType())));
                        }
                        for (int i = 0; i < numParams; i++) {
                            final int ii = i;
                            Parameter parameter = method.getParameters()[i];
                            Annotation paramAnnotation = getParameterAnnotation(parameter);
                            SetterProcessor process = configSetterHandler.get(paramAnnotation.annotationType());
                            process.processSetter(
                                    (value) -> {
                                        values.set(ii, setterCaster.get(parameter.getType()).apply(value));
                                        try {
                                            method.invoke(loggable, values.toArray());
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    paramAnnotation,
                                    list,
                                    nt,
                                    parameter.getName(),
                                    parameter.getType().equals(Boolean.TYPE) || parameter.getType().equals(Boolean.class));
                        }
                    }
                }
            }
        }
    }


    private static void logFieldsAndMethods(Object loggable,
                                            Class loggableClass,
                                            ShuffleboardContainerWrapper bin,
                                            Set<Field> registeredFields,
                                            Set<Method> registeredMethods) {

        Set<Field> fields = Set.of(loggableClass.getDeclaredFields());
        Set<Method> methods = Set.of(loggableClass.getDeclaredMethods());

        for (Field field : fields) {
            if (registeredFields.contains(field)) {
                continue;
            }
            field.setAccessible(true);
            registeredFields.add(field);

            for (Class type : logHandler.keySet()) {
                for (Annotation annotation : field.getAnnotationsByType(type)) {
                    FieldProcessor process = logHandler.get(annotation.annotationType());
                    if (process != null) {
                        process.processField(
                                () -> {
                                    try {
                                        return field.get(loggable);
                                    } catch (IllegalAccessException e) {
                                        return null;
                                    }
                                },
                                annotation,
                                bin,
                                field.getName());
                    }
                }
            }

        }

        for (Method method : methods) {

            if (method.getReturnType().equals(Void.TYPE) || registeredMethods.contains(method)) {
                continue;
            }

            method.setAccessible(true);
            registeredMethods.add(method);
            for (Class type : logHandler.keySet()) {
                for (Annotation annotation : method.getAnnotationsByType(type)) {

                    FieldProcessor process = logHandler.get(annotation.annotationType());
                    if (process != null) {
                        process.processField(
                                () -> {
                                    try {
                                        return method.invoke(loggable);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                },
                                annotation,
                                bin,
                                method.getName());
                    }

                }
            }
        }

    }

    private static void logLoggable(LogType logType,
                                    boolean separate,
                                    Loggable loggable,
                                    Class loggableClass,
                                    Set<Field> loggedFields,
                                    Set<Field> registeredFields,
                                    Set<Method> registeredMethods,
                                    ShuffleboardWrapper shuffleboard,
                                    NetworkTableInstance nt,
                                    ShuffleboardContainerWrapper parentContainer,
                                    Set<Object> ancestors) {

        ancestors.add(loggable);

        ShuffleboardContainerWrapper bin;

        switch (logType) {
            case LOG:
                if (parentContainer == null) {
                    bin = shuffleboard.getTab(separate ? loggable.configureLogName() + ": Log" : loggable.configureLogName());
                } else {
                    if (loggable.skipLayout()) {
                        bin = parentContainer;
                    } else {
                        bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                                .withSize(loggable.configureLayoutSize()[0],
                                        loggable.configureLayoutSize()[1])
                                .withPosition(loggable.configureLayoutPosition()[0],
                                        loggable.configureLayoutPosition()[1])
                                .withProperties(loggable.configureLayoutProperties());
                    }
                }

                logFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        registeredFields,
                        registeredMethods);
                break;
            case CONFIG:
                if (parentContainer == null) {
                    bin = shuffleboard.getTab(separate ? loggable.configureLogName() + ": Config" : loggable.configureLogName());
                } else {
                    if (loggable.skipLayout()) {
                        bin = parentContainer;
                    } else {
                        bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                                .withSize(loggable.configureLayoutSize()[0],
                                        loggable.configureLayoutSize()[1])
                                .withPosition(loggable.configureLayoutPosition()[0],
                                        loggable.configureLayoutPosition()[1])
                                .withProperties(loggable.configureLayoutProperties());
                    }
                }

                configFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        nt,
                        registeredFields,
                        registeredMethods);
                break;
            default:
                bin = shuffleboard.getTab("ERROR");
                break;
        }

        //only call on the actual class, to avoid multiple calls if overridden

        if (loggableClass == loggable.getClass()) {
            loggable.addCustomLogging(bin);
        }

        Consumer<Loggable> log = (toLog) -> logLoggable(logType,
                separate,
                toLog,
                toLog.getClass(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                shuffleboard,
                nt,
                bin,
                new HashSet<>(ancestors));

        //recurse on Loggable fields

        for (Field field : loggableClass.getDeclaredFields()) {

            if (!isLoggableClassOrArrayOrCollection(field, loggable)
                    || !isIncluded(field, logType)
                    || loggedFields.contains(field)
                    || isAncestor(field, loggable, ancestors)) {
                continue;
            }

            field.setAccessible(true);
            loggedFields.add(field);

            if (field.getType().isArray()) {
                List<Loggable> toLogs = new ArrayList<>();
                try {
                    if (!Object.class.isAssignableFrom(field.get(loggable).getClass().getComponentType())) {
                        continue;
                    }
                    for (Object obj : (Object[]) field.get(loggable)) {
                        if (obj instanceof Loggable) {
                            toLogs.add((Loggable) obj);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (Loggable toLog : toLogs) {
                    log.accept(toLog);
                }
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                List<Loggable> toLogs = new ArrayList<>();
                try {
                    for (Object obj : (Collection) field.get(loggable)) {
                        if (obj instanceof Loggable) {
                            toLogs.add((Loggable) obj);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (Loggable toLog : toLogs) {
                    log.accept(toLog);
                }
            } else {
                Loggable toLog;
                try {
                    toLog = (Loggable) field.get(loggable);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    toLog = null;
                }
                log.accept(toLog);
            }
        }

        //recurse on superclass

        if (Loggable.class.isAssignableFrom(loggableClass.getSuperclass())) {
            logLoggable(
                    logType,
                    separate,
                    loggable,
                    loggableClass.getSuperclass(),
                    loggedFields,
                    registeredFields,
                    registeredMethods,
                    shuffleboard,
                    nt,
                    parentContainer,
                    ancestors);
        }
    }

    private static boolean isAncestor(Field field, Object loggable, Set<Object> ancestors) {
        try {
            boolean b = ancestors.contains(field.get(loggable));
            if (b && cycleWarningsEnabled) {
                System.out.println("CAUTION: Cyclic reference of Loggables detected!  Recursion terminated after one cycle.");
                System.out.println(field.getName() + " in " + loggable.getClass().getName() +
                        " is itself an ancestor of " + loggable.getClass().getName());
                System.out.println("Please verify that this is intended.");
            }
            return b;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static boolean isLoggableClassOrArrayOrCollection(Field field, Object container) {
        field.setAccessible(true);
        Object object = null;
        try {
            object = field.get(container);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Loggable.class.isAssignableFrom(object.getClass()) ||
                object.getClass().isArray() ||
                Collection.class.isAssignableFrom(object.getClass());
    }

    private static boolean isIncluded(Field field, LogType logType) {
        boolean included = true;
        switch (logType) {
            case LOG:
                included = (field.getAnnotation(Log.Exclude.class) == null &&
                        field.getType().getAnnotation(Log.Exclude.class) == null) ||
                        field.getAnnotation(Log.Include.class) != null;
                break;
            case CONFIG:
                included = (field.getAnnotation(Config.Exclude.class) == null &&
                        field.getType().getAnnotation(Config.Exclude.class) == null) ||
                        field.getAnnotation(Config.Include.class) != null;
                break;
        }
        return included;
    }

    private static boolean containsKeys(Map map, List<Object> keys) {
        boolean containsKeys = true;
        for (Object key : keys) {
            containsKeys &= map.containsKey(key);
        }
        return containsKeys;
    }

    private static Config getDefaultConfig() {

        class DefaultHolder {
            @Config
            private int defaultField;
        }

        try {
            return DefaultHolder.class.getDeclaredField("defaultField").getAnnotation(Config.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Log getDefaultLog() {

        class DefaultHolder {
            @Log
            private int defaultField;
        }

        try {
            return DefaultHolder.class.getDeclaredField("defaultField").getAnnotation(Log.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static Annotation getParameterAnnotation(Parameter parameter) {
        Annotation annotation = getDefaultConfig();

        for (Annotation a : parameter.getAnnotations()) {
            if (configSetterHandler.containsKey(a.annotationType())) {
                annotation = a;
            }
        }

        return annotation;
    }

    private static Supplier<Object> getFromMethod(Supplier<Object> supplier, String methodName) {
        if (methodName.equals("DEFAULT")) {
            return supplier;
        }

        Object obj = supplier.get();
        final Method method;

        try {
            method = obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No method of name " + methodName + " found for object " +
                    obj.getClass().getName() + "!");
        }

        method.setAccessible(true);
        return () -> {
            try {
                return method.invoke(obj);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
