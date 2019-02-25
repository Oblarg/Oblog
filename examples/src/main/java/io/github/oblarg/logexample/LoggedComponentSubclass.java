package io.github.oblarg.logexample;

import io.github.oblarg.oblog.annotations.Config;

public class LoggedComponentSubclass extends LoggedComponent {

    boolean toggle;

    @Config.ToggleButton
    private void setToggle(boolean toggle) {
        this.toggle = toggle;
        System.out.println("Toggle set to: " + this.toggle);
    }
}
