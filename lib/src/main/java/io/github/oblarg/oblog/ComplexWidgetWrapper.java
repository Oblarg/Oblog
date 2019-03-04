package io.github.oblarg.oblog;

import java.util.Map;

/**
 * A wrapper for WPILib's ComplexWidget; wrapped to allow the Logger to substitute a NT-only implementation
 * if desired.
 */
public interface ComplexWidgetWrapper {
    ComplexWidgetWrapper withProperties(Map<String, Object> properties);

    ComplexWidgetWrapper withWidget(String widgetType);

    SimpleWidgetWrapper withPosition(int columnIndex, int rowIndex);

    SimpleWidgetWrapper withSize(int width, int height);
}
