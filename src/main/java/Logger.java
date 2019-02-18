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
                                bin.add(((LogDefault) rawParams).name(),supplier.get()).getEntry(),
                                supplier);
                    }),
            entry(LogNumberBar.class,
                    (supplier, rawParams, bin) -> {
                        LogNumberBar params = (LogNumberBar)rawParams;
                        Logger.registerEntry(
                                bin.add(params.name(),supplier.get())
                                        .withWidget(BuiltInWidgets.kNumberBar.getWidgetName())
                                        .withProperties(Map.of(
                                                "Min", params.min(),
                                                "Max", params.max(),
                                                "Center", params.center()))
                                        .getEntry(),
                                supplier);
                    }),
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
