package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

import static io.github.oblarg.oblog.Util.logErrorCheck;

class WrappedShuffleboardContainer implements ShuffleboardContainerWrapper {

  private ShuffleboardContainer container;

  WrappedShuffleboardContainer(ShuffleboardContainer container) {
    this.container = container;
  }

  @Override
  public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
    return new WrappedShuffleboardLayout(container.getLayout(title, type));
  }

  @Override
  public SimpleWidgetWrapper add(String title, Object defaultValue) {
    logErrorCheck(defaultValue, title, container.getTitle());
    try {
      return new WrappedSimpleWidget(container.add(title, defaultValue));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Error!  Attempted to log duplicate widget "
              + title
              + " in container "
              + container.getTitle());
    }
  }

  @Override
  public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
    logErrorCheck(defaultValue, title, container.getTitle());
    try {
      return new WrappedComplexWidget(container.add(title, defaultValue));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Error!  Attempted to log duplicate widget "
              + title
              + " in container "
              + container.getTitle());
    }
  }
}
