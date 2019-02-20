package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

class WrappedShuffleboardContainer implements ShuffleboardContainerWrapper {
    
    private ShuffleboardContainer container;
    
    WrappedShuffleboardContainer(ShuffleboardContainer container){
        this.container = container;
    }

    @Override
    public ShuffleboardLayoutWrapper getLayout(String title, LayoutType type) {
        return new WrappedShuffleboardLayout(container.getLayout(title, type));
    }

    @Override
    public ShuffleboardWidgetWrapper add(String title, Object defaultValue) {
        return new WrappedShuffleboardWidget (container.add(title, defaultValue));
    }
}
