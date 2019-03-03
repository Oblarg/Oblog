package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;

class NTSimpleWidget implements SimpleWidgetWrapper {

    NetworkTableEntry entry;

    NTSimpleWidget(NetworkTableEntry entry, Object value) {
        this.entry = entry;
        entry.setValue(value);
    }

    @Override
    public NetworkTableEntry getEntry() {
        return entry;
    }

    @Override
    public SimpleWidgetWrapper withProperties(Map<String, Object> properties) {
        return this;
    }

    @Override
    public SimpleWidgetWrapper withWidget(String widgetType) {
        return this;
    }
}
