package Logger;

import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import java.util.Map;

class WrappedShuffleboardLayout implements ShuffleboardLayoutWrapper {

    private ShuffleboardLayout layout;

    WrappedShuffleboardLayout (ShuffleboardLayout layout){
        this.layout = layout;
    }

    @Override
    public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
        return new WrappedShuffleboardLayout(layout.getLayout(title, type));
    }

    @Override
    public ShuffleboardWidgetWrapper add(String title, Object defaultValue) {
        return new WrappedShuffleboardWidget(layout.add(title, defaultValue));
    }

    @Override
    public ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties) {
        return new WrappedShuffleboardLayout(layout.withProperties(properties));
    }
}
