package logger;

import java.util.Map;

interface ShuffleboardLayoutWrapper extends  ShuffleboardContainerWrapper{
    ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties);
}
