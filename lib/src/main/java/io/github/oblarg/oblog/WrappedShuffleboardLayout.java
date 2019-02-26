package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import java.util.Map;

class WrappedShuffleboardLayout implements ShuffleboardLayoutWrapper {

    private ShuffleboardLayout layout;

    WrappedShuffleboardLayout (ShuffleboardLayout layout){
        this.layout = layout;
    }

    @Override
    public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
        return new WrappedShuffleboardLayout(layout.getLayout(title, type));
    }

    @Override
    public SimpleWidgetWrapper add(String title, Object defaultValue) {
        return new WrappedSimpleWidget(layout.add(title, defaultValue));
    }

    @Override
    public ComplexWidgetWrapper add(String title, Sendable defaultValue) {
        return new WrappedComplexWidget(layout.add(title, defaultValue));
    }

    @Override
    public ShuffleboardLayoutWrapper withProperties(Map<String, Object> properties) {
        layout = layout.withProperties(properties);
        return this;
    }
}
