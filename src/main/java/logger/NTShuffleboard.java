package logger;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

class NTShuffleboard implements ShuffleboardWrapper {

    private NetworkTable table;

    NTShuffleboard(String rootName){
        table = NetworkTableInstance.getDefault().getTable(rootName);
    }

    @Override
    public ShuffleboardContainerWrapper getTab(String title) {
        return new NTContainer(table);
    }
}
