package io.github.oblarg.oblog;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;

class NTSimpleWidget implements SimpleWidgetWrapper {

  GenericEntry entry;

  NTSimpleWidget(GenericEntry entry, Object value) {
    this.entry = entry;
    entry.setValue(value);
  }

  @Override
  public GenericEntry getEntry() {
    return entry;
  }

  @Override
  public SimpleWidgetWrapper withProperties(Map<String, Object> properties) {
    return this;
  }

  @Override
  public SimpleWidgetWrapper withWidget(String widgetType) {
    return this;
  }

  @Override
  public SimpleWidgetWrapper withPosition(int columnIndex, int rowIndex) {
    return this;
  }

  @Override
  public SimpleWidgetWrapper withSize(int width, int height) {
    return this;
  }
}
