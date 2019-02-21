package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;

interface ShuffleboardWidgetWrapper {
    NetworkTableEntry getEntry();
    ShuffleboardWidgetWrapper withProperties(Map<String, Object> properties);
    ShuffleboardWidgetWrapper withWidget(String widgetType);
}
