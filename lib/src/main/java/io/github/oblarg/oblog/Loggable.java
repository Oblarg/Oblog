package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

import java.util.HashMap;
import java.util.Map;

/**
 * Marks a class as loggable.  Loggable classes will define the tabs/layouts/sublayouts of your shuffleboard.  Fields or getters of
 * Loggable classes that you wish to log should be annotated with {@link Log} or one of its widget-specific sub-annotations.
 * Any children (i.e. fields) of a Loggable class that are themselves Loggable will be recursively given a layout within the tab/layout defined
 * by the parent.
 *
 * Note that Loggable classes that are not reachable from the root container passed to {@link Logger#configureLogging(Object)}
 * through a sequence of Loggable ancestors will not be logged, as the Logger will not be able to see them.
 */
public interface Loggable {

    /**
     * Set the name the object will take in Shuffleboard.  Must be overridden to disambiguate between
     * multiple fields of the same type, or else an error will be thrown.
     *
     * @return The name the object will take when logged on Shuffleboard.  Defaults to the class name.
     */
    default String configureLogName(){
        return this.getClass().getSimpleName();
    }

    /**
     * Set the type of layout of the object in Shuffleboard if it is not a root (roots define
     * their own tabs, instead).
     *
     * @return the type of layout of the object in shuffleboard.  Defaults to list.
     */
    default LayoutType configureLayoutType(){
        return BuiltInLayouts.kList;
    }

    /**
     * Set the properties of the layout of the object in Shuffleboard if it is not a root (roots define their own
     * tabs, instead).
     *
     * @return A map of property keys and values.  Defaults to null (will yield default Shuffleboard properties).
     */
    default Map<String, Object> configureLayoutProperties(){
        return new HashMap<>();
    }

    /**
     * Override to add custom logging not supported through the standard {@link Log} options.  Called by the logger
     * on each io.github.oblarg.oblog.Loggable after the handling of annotated fields and methods.
     */
    default void addCustomLogging(){}

}