package io.github.oblarg.oblog;

import java.util.Map;

interface ShuffleboardLayoutWrapper extends  ShuffleboardContainerWrapper{
    ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties);
}
