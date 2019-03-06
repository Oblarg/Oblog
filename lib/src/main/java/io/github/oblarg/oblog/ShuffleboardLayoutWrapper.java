package io.github.oblarg.oblog;

import java.util.Map;

/**
 * A wrapper for WPILib's ShuffleboardLayout; wrapped to allow the Logger to substitute a NT-only implementation
 * if desired.
 */
public interface ShuffleboardLayoutWrapper extends ShuffleboardContainerWrapper {
    ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties);
    ShuffleboardLayoutWrapper withSize(int width, int height);
    ShuffleboardLayoutWrapper withPosition(int columnIndex, int rowIndex);
}
