package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

import java.util.Map;

public class NTComplexWidget implements ComplexWidgetWrapper {

    public NTComplexWidget (NetworkTable parent, String title, Sendable sendable) {
        SendableBuilderImpl builder = new SendableBuilderImpl();
        builder.setTable(parent.getSubTable(title));
        sendable.initSendable(builder);
        builder.startListeners();
        builder.updateTable();
    }

    @Override
    public ComplexWidgetWrapper withProperties(Map<String, Object> properties) {
        return this;
    }

    @Override
    public ComplexWidgetWrapper withWidget(String widgetType) {
        return this;
    }
}
