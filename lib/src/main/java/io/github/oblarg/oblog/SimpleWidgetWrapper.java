package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;

/**
 * A wrapper for WPILib's SimpleWidget; wrapped to allow the Logger to substitute a NT-only implementation
 * if desired.
 */
public interface SimpleWidgetWrapper {
  NetworkTableEntry getEntry();

  SimpleWidgetWrapper withProperties(Map<String, Object> properties);

  SimpleWidgetWrapper withWidget(String widgetType);

  SimpleWidgetWrapper withPosition(int columnIndex, int rowIndex);

  SimpleWidgetWrapper withSize(int width, int height);
}
