import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

import java.util.Map;

interface ShuffleboardContainerWrapper {
    ShuffleboardLayoutWrapper getLayout(String title, LayoutType type);
    ShuffleboardWidgetWrapper add(String title, Object defaultValue);
}
