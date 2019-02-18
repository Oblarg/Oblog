import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;

import static java.util.Map.entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Logger {

    @FunctionalInterface
    private interface WidgetProcessor {
        void processWidget(Supplier<Object> supplier, Annotation params, ShuffleboardContainer bin);
    }

    private static Map<Class<? extends Annotation>, WidgetProcessor> widgetHandler = Map.ofEntries(
            entry(LogDefault.class,
                    (supplier, rawParams, bin) -> {
                        Logger.registerEntry(
                                bin.add(((LogDefault) rawParams).name(), supplier.get()).getEntry(),
                                supplier);
                    }),
            entry(LogNumberBar.class,
                    (supplier, rawParams, bin) -> {
                        LogNumberBar params = (LogNumberBar) rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kNumberBar.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "center", params.center()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogDial.class,
                    (supplier, rawParams, bin) -> {
                        LogDial params = (LogDial) rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kDial.getWidgetName())
                                        .withProperties(Map.of(
                                                "min", params.min(),
                                                "max", params.max(),
                                                "showValue", params.showValue()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogGraph.class,
                    (supplier, rawParams, bin) -> {
                        LogGraph params = (LogGraph) rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kGraph.getWidgetName())
                                        .withProperties(Map.of(
                                                "Visible time", params.visibleTime()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogBooleanBox.class,
                    (supplier, rawParams, bin) -> {
                        LogBooleanBox params = (LogBooleanBox) rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(), supplier.get())
                                        .withWidget(BuiltInWidgets.kBooleanBox.getWidgetName())
                                        .withProperties(Map.of(
                                                "colorWhenTrue", params.colorWhenTrue(),
                                                "colorWhenFalse", params.colorWhenFalse()))
                                        .getEntry(),
                                supplier);
                    }),
            entry(LogVoltageView.class,
                    (supplier, rawParams, bin) -> {
                        LogVoltageView params = (LogVoltageView) rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(), supplier.get())
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
                    (supplier, rawParams, bin) -> {
                        LogPDP params = (LogPDP) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPowerDistributionPanel.getWidgetName())
                                .withProperties(Map.of(
                                                "showVoltageAndCurrentValues", params.showVoltageAndCurrent()));
                    }),
            entry(LogEncoder.class,
                    (supplier, rawParams, bin) -> {
                        LogEncoder params = (LogEncoder) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kEncoder.getWidgetName());
                    }),
            entry(LogSpeedController.class,
                    (supplier, rawParams, bin) -> {
                        LogSpeedController params = (LogSpeedController) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kSpeedController.getWidgetName())
                                .withProperties(Map.of(
                                        "orientation", params.orientation()));
                    }),
            entry(LogCommand.class,
                    (supplier, rawParams, bin) -> {
                        LogCommand params = (LogCommand) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCommand.getWidgetName());
                    }),
            entry(LogPIDCommand.class,
                    (supplier, rawParams, bin) -> {
                        LogPIDCommand params = (LogPIDCommand) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDCommand.getWidgetName());
                    }),
            entry(LogPIDController.class,
                    (supplier, rawParams, bin) -> {
                        LogPIDController params = (LogPIDController) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kPIDController.getWidgetName());
                    }),
            entry(LogAccelerometer.class,
                    (supplier, rawParams, bin) -> {
                        LogAccelerometer params = (LogAccelerometer) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "min", params.min(),
                                        "max", params.max(),
                                        "showText", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(Log3AxisAccelerometer.class,
                    (supplier, rawParams, bin) -> {
                        Log3AxisAccelerometer params = (Log3AxisAccelerometer) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.k3AxisAccelerometer.getWidgetName())
                                .withProperties(Map.of(
                                        "range", params.range(),
                                        "showValue", params.showValue(),
                                        "precision", params.precision(),
                                        "showTickMarks", params.showTicks()));
                    }),
            entry(LogGyro.class,
                    (supplier, rawParams, bin) -> {
                        LogGyro params = (LogGyro) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kGyro.getWidgetName())
                                .withProperties(Map.of(
                                        "majorTickSpacing", params.majorTickSpacing(),
                                        "startingAngle", params.startingAngle(),
                                        "showTickMarkRing", params.showTicks()));
                    }),
            entry(LogDifferentialDrive.class,
                    (supplier, rawParams, bin) -> {
                        LogDifferentialDrive params = (LogDifferentialDrive) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kDifferentialDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "numberOfWheels", params.numWheels(),
                                        "wheelDiameter", params.wheelDiameter(),
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(LogMecanumDrive.class,
                    (supplier, rawParams, bin) -> {
                        LogMecanumDrive params = (LogMecanumDrive) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kMecanumDrive.getWidgetName())
                                .withProperties(Map.of(
                                        "showVelocityVectors", params.showVel()));
                    }),
            entry(LogCameraStream.class,
                    (supplier, rawParams, bin) -> {
                        LogCameraStream params = (LogCameraStream) rawParams;
                        bin.add(params.name(), supplier.get())
                                .withWidget(BuiltInWidgets.kCameraStream.getWidgetName())
                                .withProperties(Map.of(
                                        "showCrosshair", params.showCrosshairs(),
                                        "crosshairColor", params.crosshairColor(),
                                        "showControls", params.showControls(),
                                        "rotation", params.rotation()));
                    })
    );


    private static void registerFieldsAndMethods(Class loggable,
                                                 Set<Field> registeredFields,
                                                 Set<Method> registeredMethods) {
        Set<Field> fields = Set.of(loggable.getDeclaredFields());
        Set<Method> methods = Set.of(loggable.getDeclaredMethods());
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
