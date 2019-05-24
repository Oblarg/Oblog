package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

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
    return new WrappedSimpleWidget(container.add(title, defaultValue));
  }

  @Override
  public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
    return new WrappedComplexWidget(container.add(title, defaultValue));
  }
}
