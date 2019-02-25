package io.github.oblarg.logexample;

import edu.wpi.first.wpilibj.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class LoggedSubsystem extends Subsystem implements Loggable {

    private int i;
    private boolean b;

    //Example of logged field.
    @Log
    private double exampleValue = 1;

    //Example of logged getter.
    @Log
    private double exampleGetter() {
        return 2;
    }

    @Config.NumberSlider(min=-10, max=10)
    private void setI(int i) {
        this.i = i;
        System.out.println("i set to: " + i);
    }

    @Config.ToggleSwitch
    public void setB(boolean b) {
        this.b = b;
        System.out.println("b set to: " + b);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
