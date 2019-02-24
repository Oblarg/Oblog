package io.github.oblarg.oblog;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import io.github.oblarg.oblog.annotations.*;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import static java.util.Map.entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Logger {

    /**
     * Configure shuffleboard logging for the robot.  Should be called after all loggable objects have been
     * instantiated, e.g. at the end of robotInit.  Tabs for logging will be separate from tabs for config.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLogging(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     */

    public static void configureLogging(Object rootContainer) {
        configureLogging(LogType.LOG, rootContainer, new WrappedShuffleboard());
    }

    /**
     * Configure shuffleboard config for the robot.  Should be called after all loggable objects have been
     * instantiated, e.g. at the end of robotInit.  Tabs for config will be separate from tabs for logging.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureConfig(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     */
    public static void configureConfig(Object rootContainer){
        configureLogging(LogType.CONFIG, rootContainer, new WrappedShuffleboard());
    }

    /**
     * Configure shuffleboard logging and config for the robot.  Should be called after all loggable objects have beeen
     * instantiated, e.g. at the end of robotInit.  Config and logging can either be given separate tabs, or all widgets
     * can be placed in the same tabs.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLoggingAndConfig(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     * @param separate Whether to generate separate tabs for config and logging.  If true, log widgets will be placed in
     *                 tabs labeled "Log", and config widgets will be placed in tabs labeled "Config".
     */
    public static void configureLoggingAndConfig(Object rootContainer, boolean separate){
        WrappedShuffleboard shuffleboard = new WrappedShuffleboard();
        if (separate) {
            configureLogging(LogType.LOG, rootContainer, shuffleboard);
            configureLogging(LogType.CONFIG, rootContainer, shuffleboard);
        } else {
            configureLogging(LogType.BOTH, rootContainer, shuffleboard);
        }
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
        configureLogging(LogType.LOG, rootContainer, new NTShuffleboard(rootName));
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
     * change during runtime.  Mostly for internal use, but can be used by advanced users in {@link Loggable#addCustomLogging()}.
     *
     * @param entry    The entry to be updated.
     * @param supplier The supplier with which to update the entry.
     */
    public static void registerEntry(NetworkTableEntry entry, Supplier<Object> supplier) {
        entrySupplierMap.put(entry, supplier);
    }

    private static void configureLogging(LogType logType,
                                         Object rootContainer,
                                         ShuffleboardWrapper shuffleboard) {

        Consumer<Loggable> log = (toLog) -> logLoggable(logType,
                toLog,
                toLog.getClass(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                shuffleboard,
                null,
                new HashSet<>(Collections.singletonList(toLog)));

        for (Field field : rootContainer.getClass().getDeclaredFields()) {
            if (isLoggableClassOrArrayOrCollection(field) && field.getAnnotation(Log.Exclude.class) == null) {
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

    static void configureLoggingTest(LogType logType, Object rootContainer, ShuffleboardWrapper shuffleboard) {
        configureLogging(logType, rootContainer, shuffleboard);
    }

    /**
     * A map of the suppliers that are used to update each entry.
     */
    private static final Map<NetworkTableEntry, Supplier<Object>> entrySupplierMap = new HashMap<>();

    enum LogType{
        LOG, CONFIG, BOTH
    }

    private static SetterRunner setterRunner = new SetterRunner();

    @FunctionalInterface
    private interface FieldProcessor {
        void processField(Supplier<Object> supplier, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    @FunctionalInterface
    private interface BooleanSetterProcessor {
        void processBooleanSetter(Consumer<Boolean> setter, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    @FunctionalInterface
    private interface NumericSetterProcessor {
        void processNumericSetter(Consumer<Number> setter, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    private static final Map<Class<? extends Annotation>, BooleanSetterProcessor> configBooleanSetterHandler = Map.ofEntries(
            entry(Config.class, (setter, rawParams, bin, name) -> {
                Config params = (Config) rawParams;
                NetworkTableInstance.getDefault().addEntryListener(
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), true)
                                .withWidget(BuiltInWidgets.kToggleButton.getWidgetName()).getEntry(),
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(true);
            }),
            entry(Config.ToggleButton.class, (setter, rawParams, bin, name) -> {
                Config.ToggleButton params = (Config.ToggleButton) rawParams;
                NetworkTableInstance.getDefault().addEntryListener(
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                                .withWidget(BuiltInWidgets.kToggleButton.getWidgetName()).getEntry(),
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            }),
            entry(Config.ToggleSwitch.class, (setter, rawParams, bin, name) -> {
                Config.ToggleSwitch params = (Config.ToggleSwitch) rawParams;
                NetworkTableInstance.getDefault().addEntryListener(
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                                .withWidget(BuiltInWidgets.kToggleSwitch.getWidgetName()).getEntry(),
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((boolean) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            })
    );

    private static final Map<Class<? extends Annotation>, NumericSetterProcessor> configNumericSetterHandler = Map.ofEntries(
            entry(Config.class, (setter, rawParams, bin, name) -> {
                Config params = (Config) rawParams;
                NetworkTableInstance.getDefault().addEntryListener(
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), 0)
                                .withWidget(BuiltInWidgets.kTextView.getWidgetName()).getEntry(),
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((Number) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(0);
            }),
            entry(Config.NumberSlider.class, (setter, rawParams, bin, name) -> {
                Config.NumberSlider params = (Config.NumberSlider) rawParams;
                NetworkTableInstance.getDefault().addEntryListener(
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), params.defaultValue())
                                .withWidget(BuiltInWidgets.kNumberSlider.getWidgetName())
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "blockIncrement", params.blockIncrement()))
                                .getEntry(),
                        (entryNotification) -> setterRunner.execute(() -> setter.accept((Number) entryNotification.value.getValue())),
                        EntryListenerFlags.kUpdate
                );
                setter.accept(params.defaultValue());
            })
    );


    private static final Map<Class<? extends Annotation>, FieldProcessor> configFieldHandler = Map.ofEntries(
            entry(Config.Command.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.Command params = (Config.Command) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCommand.getWidgetName());
                    }),
            entry(Config.PIDCommand.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.PIDCommand params = (Config.PIDCommand) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDCommand.getWidgetName());
                    }),
            entry(Config.PIDController.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.PIDController params = (Config.PIDController) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDController.getWidgetName());
                    }),
            entry(Config.Relay.class,
                    (supplier, rawParams, bin, name) -> {
                        Config.Relay params = (Config.Relay) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kRelay.getWidgetName());
                    })
    );

    private static final Map<Class<? extends Annotation>, FieldProcessor> logHandler = Map.ofEntries(
            entry(Log.class,
                    (supplier, rawParams, bin, name) -> {
                        Log params = (Log) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get()).getEntry(),
                                supplier);
                    }),
            entry(Log.NumberBar.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.NumberBar params = (Log.NumberBar) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kNumberBar.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "center", params.center()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(Log.Dial.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Dial params = (Log.Dial) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kDial.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "showValue", params.showValue()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(Log.Graph.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Graph params = (Log.Graph) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kGraph.getWidgetName())
                                        .withProperties(Map.of(
                                                "Visible time", params.visibleTime()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(Log.BooleanBox.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.BooleanBox params = (Log.BooleanBox) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kBooleanBox.getWidgetName())
                                        .withProperties(Map.of(
                                                "colorWhenTrue", params.colorWhenTrue(),
                                                "colorWhenFalse", params.colorWhenFalse()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(Log.VoltageView.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.VoltageView params = (Log.VoltageView) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kVoltageView.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "center", params.center(),
                                                "orientation", params.orientation(),
                                                "numberOfTickMarks", params.numTicks()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(Log.PDP.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.PDP params = (Log.PDP) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPowerDistributionPanel.getWidgetName())
                                .withProperties(Map.of(
                                        "showVoltageAndCurrentValues", params.showVoltageAndCurrent()));
                    }),
            entry(Log.Encoder.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Encoder params = (Log.Encoder) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kEncoder.getWidgetName());
                    }),
            entry(Log.SpeedController.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.SpeedController params = (Log.SpeedController) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kSpeedController.getWidgetName())
                                .withProperties(Map.of(
                                        "orientation", params.orientation()));
                    }),
            entry(Log.Accelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Accelerometer params = (Log.Accelerometer) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showText", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(Log.ThreeAxisAccelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.ThreeAxisAccelerometer params = (Log.ThreeAxisAccelerometer) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.k3AxisAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "range", params.range(),
                                        "showValue", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(Log.Gyro.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Gyro params = (Log.Gyro) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kGyro.getWidgetName())
                                .withProperties(Map.of(
                                        "majorTickSpacing", params.majorTickSpacing(),
                                        "startingAngle", params.startingAngle(),
                                        "showTickMarkRing", params.showTicks()));
                    }),
            entry(Log.DifferentialDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.DifferentialDrive params = (Log.DifferentialDrive) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kDifferentialDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "numberOfWheels", params.numWheels(),
                                        "wheelDiameter", params.wheelDiameter(),
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(Log.MecanumDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.MecanumDrive params = (Log.MecanumDrive) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kMecanumDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(Log.CameraStream.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.CameraStream params = (Log.CameraStream) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCameraStream.getWidgetName())
                                .withProperties(Map.of(
                                        "showCrosshair", params.showCrosshairs(),
                                        "crosshairColor", params.crosshairColor(),
                                        "showControls", params.showControls(),
                                        "rotation", params.rotation()));
                    })
    );

    private static void configFieldsAndMethods(Loggable loggable,
                                               Class loggableClass,
                                               ShuffleboardContainerWrapper bin,
                                               Set<Field> registeredFields,
                                               Set<Method> registeredMethods) {

        Set<Field> fields = Set.of(loggableClass.getDeclaredFields());
        Set<Method> methods = Set.of(loggableClass.getDeclaredMethods());

        for (Field field : fields) {
            field.setAccessible(true);
            if (!registeredFields.contains(field)) {
                registeredFields.add(field);
                for (Annotation annotation : field.getAnnotations()) {
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

        for (Method method : methods) {
            if (method.getReturnType().equals(Void.TYPE) &&
                    method.getParameterTypes().length == 1 &&
                    takesBoolean(method)) {
                method.setAccessible(true);
                if (!registeredMethods.contains(method)) {
                    registeredMethods.add(method);
                    for (Annotation annotation : method.getAnnotations()) {
                        BooleanSetterProcessor process = configBooleanSetterHandler.get(annotation.annotationType());
                        if (process != null) {
                            process.processBooleanSetter(
                                    (value) -> {
                                        try {
                                            method.invoke(loggable, value);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    annotation,
                                    bin,
                                    method.getName());
                        }
                    }
                }
            } else if (method.getReturnType().equals(Void.TYPE) &&
                    method.getParameterTypes().length == 1 &&
                    takesNumeric(method)){
                method.setAccessible(true);
                if (!registeredMethods.contains(method)) {
                    registeredMethods.add(method);
                    for (Annotation annotation : method.getAnnotations()) {
                        NumericSetterProcessor process = configNumericSetterHandler.get(annotation.annotationType());
                        if (process != null) {
                            process.processNumericSetter(
                                    (value) -> {
                                        try {
                                            method.invoke(loggable, value);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
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


    }


    private static void logFieldsAndMethods(Loggable loggable,
                                            Class loggableClass,
                                            ShuffleboardContainerWrapper bin,
                                            Set<Field> registeredFields,
                                            Set<Method> registeredMethods) {

        Set<Field> fields = Set.of(loggableClass.getDeclaredFields());
        Set<Method> methods = Set.of(loggableClass.getDeclaredMethods());

        for (Field field : fields) {
            field.setAccessible(true);
            if (!registeredFields.contains(field)) {
                registeredFields.add(field);
                for (Annotation annotation : field.getAnnotations()) {
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
            if (!method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 0) {
                method.setAccessible(true);
                if (!registeredMethods.contains(method)) {
                    registeredMethods.add(method);
                    for (Annotation annotation : method.getAnnotations()) {
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

    }

    private static void logLoggable(LogType logType,
                                    Loggable loggable,
                                    Class loggableClass,
                                    Set<Field> loggedFields,
                                    Set<Field> registeredFields,
                                    Set<Method> registeredMethods,
                                    ShuffleboardWrapper shuffleboard,
                                    ShuffleboardContainerWrapper parentContainer,
                                    Set<Object> ancestors) {

        ancestors.add(loggable);

        ShuffleboardContainerWrapper bin;

        switch(logType) {
            case LOG:
                if (parentContainer == null) {
                    bin = shuffleboard.getTab(loggable.configureLogName() + ": Log");
                } else {
                    bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                            .withProperties(loggable.configureLayoutProperties());
                }

                logFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        registeredFields,
                        registeredMethods);
                break;
            case CONFIG:
                if (parentContainer == null) {
                    bin = shuffleboard.getTab(loggable.configureLogName() + ": Config");
                } else {
                    bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                            .withProperties(loggable.configureLayoutProperties());
                }

                configFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        registeredFields,
                        registeredMethods);
                break;
            case BOTH:
                if (parentContainer == null) {
                    bin = shuffleboard.getTab(loggable.configureLogName());
                } else {
                    bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                            .withProperties(loggable.configureLayoutProperties());
                }

                logFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        registeredFields,
                        registeredMethods);


                configFieldsAndMethods(loggable,
                        loggableClass,
                        bin,
                        registeredFields,
                        registeredMethods);
                break;
            default:
                bin = shuffleboard.getTab("ERROR");
                break;
        }

        //only call on the actual class, to avoid multiple calls if overridden

        if (loggableClass == loggable.getClass()) {
            loggable.addCustomLogging();
        }

        Consumer<Loggable> log = (toLog) -> logLoggable(logType,
                toLog,
                toLog.getClass(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                shuffleboard,
                bin,
                new HashSet<>(ancestors));

        //recurse on Loggable fields

        for (Field field : loggableClass.getDeclaredFields()) {
            if (isLoggableClassOrArrayOrCollection(field) && field.getAnnotation(Log.Exclude.class) == null) {
                field.setAccessible(true);
                if (!loggedFields.contains(field) && !isAncestor(field, loggable, ancestors)) {
                    loggedFields.add(field);
                    if (field.getType().isArray()) {
                        Loggable[] toLogs;
                        try {
                            toLogs = (Loggable[]) field.get(loggable);
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
                            toLogs = (Collection) field.get(loggable);
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
                            toLog = (Loggable) field.get(loggable);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            toLog = null;
                        }
                        log.accept(toLog);
                    }
                }
            }
        }

        //recurse on superclass

        if (Loggable.class.isAssignableFrom(loggableClass.getSuperclass())) {
            logLoggable(
                    logType,
                    loggable,
                    loggableClass.getSuperclass(),
                    loggedFields,
                    registeredFields,
                    registeredMethods,
                    shuffleboard,
                    parentContainer,
                    ancestors);
        }
    }

    private static boolean isAncestor(Field field, Object loggable, Set<Object> ancestors) {
        try {
            boolean b = ancestors.contains(field.get(loggable));
            if (b) {
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

    private static boolean isLoggableClassOrArrayOrCollection(Field field) {
        return Loggable.class.isAssignableFrom(field.getType()) ||
                (field.getType().isArray() && Loggable.class.isAssignableFrom(field.getType().getComponentType())) ||
                (Collection.class.isAssignableFrom(field.getType()) &&
                        Loggable.class.isAssignableFrom(
                                (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
    }

    private static boolean takesBoolean(Method method) {
        return method.getParameterTypes()[0].equals(Boolean.TYPE) || method.getParameterTypes()[0].equals(Boolean.class);
    }

    private static boolean takesNumeric(Method method) {
        return method.getParameterTypes()[0].equals(Integer.TYPE) ||
                method.getParameterTypes()[0].equals(Integer.class) ||
                method.getParameterTypes()[0].equals(Double.TYPE) ||
                method.getParameterTypes()[0].equals(Double.class);
    }
}
