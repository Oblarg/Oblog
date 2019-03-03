package io.github.oblarg.logexample.subsystems;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import io.github.oblarg.logexample.LoggedComponent;
import io.github.oblarg.logexample.LoggedComponentSubclass;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class LoggedSubsystem extends Subsystem implements Loggable {

    private int i;
    private boolean b;

    private double kP;
    private double kI;
    private double kD;

    //Example of logged field.
    @Log
    private double exampleValue = 1;

    //Example of logged getter.
    @Log
    private double exampleGetter() {
        return 2;
    }

    private LoggedComponent component = new LoggedComponentSubclass();

    @Config.NumberSlider(min = -10, max = 10)
    private void setI(int i) {
        this.i = i;
        System.out.println("i set to: " + this.i);
    }

    @Config.ToggleSwitch
    public void setB(boolean b) {
        this.b = b;
        System.out.println("b set to: " + this.b);
    }

    @Config()
    public void setPID(double p, double i, double d) {
        kP = p;
        kI = i;
        kD = d;

        System.out.println("PID: " + kP + " " + kI + " " + kD);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
