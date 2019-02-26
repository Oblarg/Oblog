package io.github.oblarg.oblog;

import java.util.Map;

public class NTComplexWidget implements ComplexWidgetWrapper {
    @Override
    public ComplexWidgetWrapper withProperties(Map<String, Object> properties) {
        return this;
    }

    @Override
    public ComplexWidgetWrapper withWidget(String widgetType) {
        return this;
    }
}
