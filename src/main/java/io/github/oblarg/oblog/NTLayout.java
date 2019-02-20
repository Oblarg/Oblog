package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

import java.util.Map;

class NTLayout implements ShuffleboardLayoutWrapper {

    NetworkTable table;

    NTLayout(NetworkTable table){
        this.table = table;
    }

    @Override
    public ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties) {
        return this;
    }

    @Override
    public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
        return new NTLayout(table.getSubTable(title));
    }

    @Override
    public ShuffleboardWidgetWrapper add(String title, Object defaultValue) {
        return new NTWidget(table.getEntry(title), defaultValue);
    }
}
