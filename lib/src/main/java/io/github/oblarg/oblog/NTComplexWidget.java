package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

import java.util.Map;

class NTComplexWidget implements ComplexWidgetWrapper {

  NTComplexWidget(NetworkTable parent, String title, Sendable sendable) {

    SendableBuilderImpl builder = new SendableBuilderImpl();
    builder.setTable(parent.getSubTable(title));
    sendable.initSendable(builder);
    builder.startListeners();
    builder.update();
  }

  @Override
  public ComplexWidgetWrapper withProperties(Map<String, Object> properties) {
    return this;
  }

  @Override
  public ComplexWidgetWrapper withWidget(String widgetType) {
    return this;
  }

  @Override
  public ComplexWidgetWrapper withPosition(int columnIndex, int rowIndex) {
    return this;
  }

  @Override
  public ComplexWidgetWrapper withSize(int width, int height) {
    return this;
  }
}
