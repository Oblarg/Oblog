package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import java.util.Map;

import static io.github.oblarg.oblog.Util.logErrorCheck;

class WrappedShuffleboardLayout implements ShuffleboardLayoutWrapper {

  private ShuffleboardLayout layout;

  WrappedShuffleboardLayout(ShuffleboardLayout layout) {
    this.layout = layout;
  }

  @Override
  public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
    return new WrappedShuffleboardLayout(layout.getLayout(title, type));
  }

  @Override
  public SimpleWidgetWrapper add(String title, Object defaultValue) {
    logErrorCheck(defaultValue, title, layout.getTitle());
    try {
      return new WrappedSimpleWidget(layout.add(title, defaultValue));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error!  Attempted to log duplicate widget " + title + " in container " +
          layout.getTitle());
    }
  }

  @Override
  public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
    logErrorCheck(defaultValue, title, layout.getTitle());
    try {
      return new WrappedComplexWidget(layout.add(title, defaultValue));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error!  Attempted to log duplicate widget " + title + " in container " +
          layout.getTitle());
    }
  }

  @Override
  public ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties) {
    layout.withProperties(properties);
    return this;
  }

  @Override
  public ShuffleboardLayoutWrapper withPosition(int columnIndex, int rowIndex) {
    layout.withPosition(columnIndex, rowIndex);
    return this;
  }

  @Override
  public ShuffleboardLayoutWrapper withSize(int width, int height) {
    layout.withSize(width, height);
    return this;
  }
}
