package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;

import java.util.Map;

public class WrappedComplexWidget implements ComplexWidgetWrapper{

    ComplexWidget widget;

    public WrappedComplexWidget(ComplexWidget widget) {
        this.widget = widget;
    }

    @Override
    public ComplexWidgetWrapper withProperties(Map<String, Object> properties) {
        widget.withProperties(properties);
        return this;
    }

    @Override
    public ComplexWidgetWrapper withWidget(String widgetType) {
        widget = widget.withWidget(widgetType);
        return this;
    }
}
