import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTShuffleboard implements ShuffleboardWrapper {

    private NetworkTable table;

    public NTShuffleboard(String rootName){
        table = NetworkTableInstance.getDefault().getTable(rootName);
    }

    @Override
    public ShuffleboardContainerWrapper getTab(String title) {
        return new NTContainer(table);
    }
}
