import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

public class WrappedShuffleboard implements ShuffleboardWrapper {
    @Override
    public ShuffleboardContainerWrapper getTab(String title) {
        return new WrappedShuffleboardContainer(Shuffleboard.getTab(title));
    }
}
