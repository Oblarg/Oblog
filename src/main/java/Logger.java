import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import static java.util.Map.entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class Logger {

    @FunctionalInterface
    private interface WidgetProcessor {
        void processWidget(Supplier<Object> supplier, Annotation params, ShuffleboardContainerWrapper bin, String name);
    }

    private static Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler = Map.ofEntries(
            entry(LogDefault.class,
                    (supplier, rawParams, bin, name) -> {
                        LogDefault params = (LogDefault) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get()).getEntry(),
                                supplier);
                    }),
            entry(LogNumberBar.class,
                    (supplier, rawParams, bin, name) -> {
                        LogNumberBar params = (LogNumberBar) rawParams;
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
            entry(LogDial.class,
                    (supplier, rawParams, bin, name) -> {
                        LogDial params = (LogDial) rawParams;
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
            entry(LogGraph.class,
                    (supplier, rawParams, bin, name) -> {
                        LogGraph params = (LogGraph) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kGraph.getWidgetName())
                                        .withProperties(Map.of(
                                                "Visible time", params.visibleTime()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogBooleanBox.class,
                    (supplier, rawParams, bin, name) -> {
                        LogBooleanBox params = (LogBooleanBox) rawParams;
                        Logger.registerEntry(
                                bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kBooleanBox.getWidgetName())
                                        .withProperties(Map.of(
                                                "colorWhenTrue", params.colorWhenTrue(),
                                                "colorWhenFalse", params.colorWhenFalse()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogVoltageView.class,
                    (supplier, rawParams, bin, name) -> {
                        LogVoltageView params = (LogVoltageView) rawParams;
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
            entry(LogPDP.class,
                    (supplier, rawParams, bin, name) -> {
                        LogPDP params = (LogPDP) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPowerDistributionPanel.getWidgetName())
                                .withProperties(Map.of(
                                        "showVoltageAndCurrentValues", params.showVoltageAndCurrent()));
                    }),
            entry(LogEncoder.class,
                    (supplier, rawParams, bin, name) -> {
                        LogEncoder params = (LogEncoder) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kEncoder.getWidgetName());
                    }),
            entry(LogSpeedController.class,
                    (supplier, rawParams, bin, name) -> {
                        LogSpeedController params = (LogSpeedController) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kSpeedController.getWidgetName())
                                .withProperties(Map.of(
                                        "orientation", params.orientation()));
                    }),
            entry(LogCommand.class,
                    (supplier, rawParams, bin, name) -> {
                        LogCommand params = (LogCommand) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCommand.getWidgetName());
                    }),
            entry(LogPIDCommand.class,
                    (supplier, rawParams, bin, name) -> {
                        LogPIDCommand params = (LogPIDCommand) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDCommand.getWidgetName());
                    }),
            entry(LogPIDController.class,
                    (supplier, rawParams, bin, name) -> {
                        LogPIDController params = (LogPIDController) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDController.getWidgetName());
                    }),
            entry(LogAccelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        LogAccelerometer params = (LogAccelerometer) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showText", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(Log3AxisAccelerometer.class,
                    (supplier, rawParams, bin, name) -> {
                        Log3AxisAccelerometer params = (Log3AxisAccelerometer) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.k3AxisAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "range", params.range(),
                                        "showValue", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(LogGyro.class,
                    (supplier, rawParams, bin, name) -> {
                        LogGyro params = (LogGyro) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kGyro.getWidgetName())
                                .withProperties(Map.of(
                                        "majorTickSpacing", params.majorTickSpacing(),
                                        "startingAngle", params.startingAngle(),
                                        "showTickMarkRing", params.showTicks()));
                    }),
            entry(LogDifferentialDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        LogDifferentialDrive params = (LogDifferentialDrive) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kDifferentialDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "numberOfWheels", params.numWheels(),
                                        "wheelDiameter", params.wheelDiameter(),
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(LogMecanumDrive.class,
                    (supplier, rawParams, bin, name) -> {
                        LogMecanumDrive params = (LogMecanumDrive) rawParams;
                        bin.add((params.name().equals("NO_NAME")) ? name : params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kMecanumDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(LogCameraStream.class,
                    (supplier, rawParams, bin, name) -> {
                        LogCameraStream params = (LogCameraStream) rawParams;
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

        System.out.println("Registering Fields!");

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

        //recurse on superclass

        /*if (Loggable.class.isAssignableFrom(loggableClass.getSuperclass())) {
            registerFieldsAndMethods(loggable,
                    loggableClass.getSuperclass(),
                    bin,
                    registeredFields,
                    registeredMethods,
                    widgetHandler);
        }*/

    }

    private static void logLoggable(Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler,
                                    Loggable loggable,
                                    Class loggableClass,
                                    Set<Field> loggedFields,
                                    Set<Object> loggedObjects,
                                    Set<Field> registeredFields,
                                    Set<Method> registeredMethods,
                                    ShuffleboardWrapper shuffleboard,
                                    ShuffleboardContainerWrapper parent) {

        ShuffleboardContainerWrapper bin;

        if (parent == null) {
            bin = shuffleboard.getTab(loggable.configureLogName());
        } else {
            bin = parent.getLayout(loggable.configureLogName(), loggable.configureLayoutType())
                    .withProperties(loggable.configureLayoutProperties());
        }

        registerFieldsAndMethods(loggable,
                loggableClass,
                bin,
                registeredFields,
                registeredMethods,
                widgetHandler);

        //recurse on Loggable fields

        for (Field field : loggable.getClass().getDeclaredFields()) {
            if (Loggable.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                if (!loggedFields.contains(field)) {
                    loggedFields.add(field);
                    Loggable toLog;
                    try {
                        toLog = (Loggable) field.get(loggable);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        toLog = null;
                    }
                    if ((!loggedObjects.contains(toLog) || (toLog.getClass().getAnnotation(AllowRepeat.class) != null))
                            && field.getAnnotation(LogExclude.class) == null) {
                        loggedObjects.add(toLog);
                        logLoggable(widgetHandler,
                                toLog,
                                toLog.getClass(),
                                new HashSet<>(),
                                loggedObjects,
                                new HashSet<>(),
                                new HashSet<>(),
                                shuffleboard,
                                bin
                        );
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
                    loggedObjects,
                    registeredFields,
                    registeredMethods,
                    shuffleboard,
                    parent);
        }
    }


    private static void configureLogging(Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler,
                                         Object rootContainer,
                                         ShuffleboardWrapper shuffleboard) {

        Set<Object> loggedObjects = new HashSet<>();

        for (Field field : rootContainer.getClass().getDeclaredFields()) {
            if (Loggable.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    Loggable toLog = (Loggable) field.get(rootContainer);
                    loggedObjects.add(toLog);
                    logLoggable(widgetHandler,
                            toLog,
                            toLog.getClass(),
                            new HashSet<>(),
                            loggedObjects,
                            new HashSet<>(),
                            new HashSet<>(),
                            shuffleboard,
                            null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void configureLoggingTest(Object rootContainer, ShuffleboardWrapper shuffleboard) {
        configureLogging(widgetHandler,
                rootContainer,
                shuffleboard);
    }

    /**
     * A map of the suppliers that are used to update each entry.
     */
    private static final Map<NetworkTableEntry, Supplier<Object>> entrySupplierMap = new HashMap<>();

    /**
     * Updates all entries.  To be called periodically from the main robot loop.
     */
    public static void updateEntries() {
        entrySupplierMap.forEach((entry, supplier) -> entry.setValue(supplier.get()));
    }

    /**
     * Registers a new entry.  To be called during initial logging configuration for any value that will
     * change during runtime.
     */
    public static void registerEntry(NetworkTableEntry entry, Supplier<Object> supplier) {
        entrySupplierMap.put(entry, supplier);
    }

}
