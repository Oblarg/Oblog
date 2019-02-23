package io.github.oblarg.logexample;

import edu.wpi.first.wpilibj.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class LoggedSubsystem extends Subsystem implements Loggable {

    @Log
    private double exampleValue = 1;

    @Log
    private double exampleGetter() {
        return 2;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
