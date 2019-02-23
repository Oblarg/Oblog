package io.github.oblarg.oblog;

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
     * instantiated, e.g. at the end of robotInit.
     *
     * @param rootContainer The root of the tree of loggable objects - for most teams, this is Robot.java.
     *                      To send an instance of Robot.java to this method from robotInit, call "configureLogging(this)"
     *                      Loggable fields of this object will have their own shuffleboard tabs.
     */

    public static void configureLogging(Object rootContainer) {
        configureLogging(widgetHandler,
                rootContainer,
                new WrappedShuffleboard());
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
        configureLogging(widgetHandler,
                rootContainer,
                new NTShuffleboard(rootName));
    }

    private static void configureLogging(Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler,
                                         Object rootContainer,
                                         ShuffleboardWrapper shuffleboard) {

        Consumer<Loggable> log = (toLog) -> logLoggable(widgetHandler,
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


        /*for (Field field : rootContainer.getClass().getDeclaredFields()) {
            if (Loggable.class.isAssignableFrom(field.getType()) &&
                    field.getAnnotation(Log.Exclude.class) == null) {
                field.setAccessible(true);
                try {
                    Loggable toLog = (Loggable) field.get(rootContainer);
                    logLoggable(widgetHandler,
                            toLog,
                            toLog.getClass(),
                            new HashSet<>(),
                            new HashSet<>(),
                            new HashSet<>(),
                            shuffleboard,
                            null,
                            new HashSet<>());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    static void configureLoggingTest(Object rootContainer, ShuffleboardWrapper shuffleboard) {
        configureLogging(widgetHandler,
                rootContainer,
                shuffleboard);
    }

    /**
     * A map of the suppliers that are used to update each entry.
     */
    private static final Map<NetworkTableEntry, Supplier<Object>> entrySupplierMap = new HashMap<>();

    /**
     * Updates all entries.  Must be called periodically from the main robot loop.
     */
    public static void updateEntries() {
        entrySupplierMap.forEach((entry, supplier) -> entry.setValue(supplier.get()));
    }

    /**
     * Registers a new entry.  To be called during initial logging configuration for any value that will
     * change during runtime.
     *
     * @param entry    The entry to be updated.
     * @param supplier The supplier with which to update the entry.
     */
    public static void registerEntry(NetworkTableEntry entry, Supplier<Object> supplier) {
        entrySupplierMap.put(entry, supplier);
    }

    @FunctionalInterface
    private interface WidgetProcessor {
        void processWidget(Supplier<Object> supplier, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    private static final Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler = Map.ofEntries(
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
            entry(Log.Command.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.Command params = (Log.Command) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCommand.getWidgetName());
                    }),
            entry(Log.PIDCommand.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.PIDCommand params = (Log.PIDCommand) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDCommand.getWidgetName());
                    }),
            entry(Log.PIDController.class,
                    (supplier, rawParams, bin, name) -> {
                        Log.PIDController params = (Log.PIDController) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDController.getWidgetName());
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


    private static void registerFieldsAndMethods(Loggable loggable,
                                                 Class loggableClass,
                                                 ShuffleboardContainerWrapper bin,
                                                 Set<Field> registeredFields,
                                                 Set<Method> registeredMethods,
                                                 Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler) {

        Set<Field> fields = Set.of(loggableClass.getDeclaredFields());
        Set<Method> methods = Set.of(loggableClass.getDeclaredMethods());

        for (Field field : fields) {
            field.setAccessible(true);
            if (!registeredFields.contains(field)) {
                registeredFields.add(field);
                for (Annotation annotation : field.getAnnotations()) {
                    WidgetProcessor process = widgetHandler.get(annotation.annotationType());
                    if (process != null) {
                        process.processWidget(
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
                        WidgetProcessor process = widgetHandler.get(annotation.annotationType());
                        if (process != null) {
                            process.processWidget(
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

    private static void logLoggable(Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler,
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

        if (parentContainer == null) {
            bin = shuffleboard.getTab(loggable.configureLogName());
        } else {
            bin = parentContainer.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                    .withProperties(loggable.configureLayoutProperties());
        }

        registerFieldsAndMethods(loggable,
                loggableClass,
                bin,
                registeredFields,
                registeredMethods,
                widgetHandler);

        //only call on the actual class, to avoid multiple calls if overridden

        if (loggableClass == loggable.getClass()) {
            loggable.addCustomLogging();
        }

        Consumer<Loggable> log = (toLog) -> logLoggable(widgetHandler,
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
            logLoggable(widgetHandler,
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
            if(b){
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


}
