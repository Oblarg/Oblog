package io.github.oblarg.oblog;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

import java.util.Map;

class WrappedSimpleWidget implements SimpleWidgetWrapper {

  private SimpleWidget widget;

  WrappedSimpleWidget(SimpleWidget component) {
    this.widget = component;
  }

  @Override
  public GenericEntry getEntry() {
    return widget.getEntry();
  }

  @Override
  public SimpleWidgetWrapper withProperties(Map<String, Object> properties) {
    widget.withProperties(properties);
    return this;
  }

  @Override
  public SimpleWidgetWrapper withWidget(String widgetType) {
    widget = widget.withWidget(widgetType);
    return this;
  }

  @Override
  public SimpleWidgetWrapper withPosition(int columnIndex, int rowIndex) {
    widget.withPosition(columnIndex, rowIndex);
    return this;
  }

  @Override
  public SimpleWidgetWrapper withSize(int width, int height) {
    widget.withSize(width, height);
    return this;
  }
}
