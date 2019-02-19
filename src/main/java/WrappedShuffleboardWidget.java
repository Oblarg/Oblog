import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

import java.util.Map;

class WrappedShuffleboardWidget implements ShuffleboardWidgetWrapper {

    private SimpleWidget widget;

    WrappedShuffleboardWidget(SimpleWidget component){
        this.widget = component;
    }
    @Override
    public NetworkTableEntry getEntry() {
        return widget.getEntry();
    }

    @Override
    public ShuffleboardWidgetWrapper withProperties(Map<String, Object> properties) {
        return new WrappedShuffleboardWidget(widget.withProperties(properties));
    }

    @Override
    public ShuffleboardWidgetWrapper withWidget(String widgetType) {
        return new WrappedShuffleboardWidget(widget.withWidget(widgetType));
    }
}
