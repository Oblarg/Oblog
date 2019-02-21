package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

interface ShuffleboardContainerWrapper {
    ShuffleboardLayoutWrapper getLayout(String title, LayoutType type);
    ShuffleboardWidgetWrapper add(String title, Object defaultValue);
}
