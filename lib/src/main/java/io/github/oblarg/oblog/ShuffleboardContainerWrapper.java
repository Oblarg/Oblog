package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

/**
 * A wrapper for the WPILib ShuffleboardContainer class; wrapped to allow the Logger to substitute a NT-only implementation
 * if desired.
 */
public interface ShuffleboardContainerWrapper {
  ShuffleboardLayoutWrapper getLayout(String title, LayoutType type);

  SimpleWidgetWrapper add(String title, Object defaultValue);

  ComplexWidgetWrapper add(String title, Sendable defaultValue);
}
