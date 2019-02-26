package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

interface ShuffleboardContainerWrapper {
    ShuffleboardLayoutWrapper getLayout(String title, LayoutType type);
    SimpleWidgetWrapper add(String title, Object defaultValue);
    ComplexWidgetWrapper add(String title, Sendable defaultValue);
}
