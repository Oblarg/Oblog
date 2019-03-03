package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;

interface SimpleWidgetWrapper {
    NetworkTableEntry getEntry();

    SimpleWidgetWrapper withProperties(Map<String, Object> properties);

    SimpleWidgetWrapper withWidget(String widgetType);
}
