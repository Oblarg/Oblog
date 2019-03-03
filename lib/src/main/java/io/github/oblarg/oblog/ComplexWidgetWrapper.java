package io.github.oblarg.oblog;

import java.util.Map;

interface ComplexWidgetWrapper {
    ComplexWidgetWrapper withProperties(Map<String, Object> properties);

    ComplexWidgetWrapper withWidget(String widgetType);
}
