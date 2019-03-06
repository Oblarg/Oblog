package io.github.oblarg.logexample;

import io.github.oblarg.oblog.annotations.Config;

public class LoggedComponentSubclass extends LoggedComponent {

    public LoggedComponentSubclass(int columnIndex, int rowIndex){
        super(columnIndex, rowIndex);
    }

    boolean toggle;

    @Config.ToggleButton
    private void setToggle(boolean toggle) {
        this.toggle = toggle;
        System.out.println("Toggle set to: " + this.toggle);
    }
}
