package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

class NTContainer implements ShuffleboardContainerWrapper {

    NetworkTable table;

    NTContainer(NetworkTable table) {
        this.table = table;
    }

    @Override
    public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
        return new NTLayout(table.getSubTable(title));
    }

    @Override
    public SimpleWidgetWrapper add(String title, Object defaultValue) {
        return new NTSimpleWidget(table.getEntry(title), defaultValue);
    }

    @Override
    public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
        return new NTComplexWidget(table, title, defaultValue);
    }
}
