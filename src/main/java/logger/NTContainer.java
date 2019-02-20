package Logger;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

class NTContainer implements ShuffleboardContainerWrapper {

    NetworkTable table;

    NTContainer(NetworkTable table){
        this.table = table;
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
