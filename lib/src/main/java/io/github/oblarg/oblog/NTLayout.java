package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;

import java.util.Map;

class NTLayout implements ShuffleboardLayoutWrapper {

  private NetworkTable table;

  NTLayout(NetworkTable table) {
    this.table = table;
  }

  @Override
  public ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties) {
    return this;
  }

  @Override
  public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
    return new NTLayout(table.getSubTable(title));
  }

  @Override
  public SimpleWidgetWrapper add(String title, Object defaultValue) {
    return new NTSimpleWidget(table.getEntry(title), defaultValue);
  }

  @Override
  public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
    return new NTComplexWidget(table, title, defaultValue);
  }

  @Override
  public ShuffleboardLayoutWrapper withSize(int width, int height) {
    return this;
  }

  @Override
  public ShuffleboardLayoutWrapper withPosition(int columnIndex, int rowIndex) {
    return this;
  }
}
