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
    @Log(columnIndex = 0, rowIndex = 3)
    private double exampleValue = 1;

    //Example of logged getter.
    @Log(columnIndex = 3, rowIndex = 0)
    private double exampleGetter() {
        return 2;
    }

    private LoggedComponent component = new LoggedComponentSubclass(0, 0);

    @Config.NumberSlider(min = -10, max = 10, width = 2, columnIndex = 3, rowIndex = 0)
    private void setI(int i) {
        this.i = i;
        System.out.println("i set to: " + this.i);
    }

    @Config.ToggleSwitch(columnIndex = 4, rowIndex = 0)
    public void setB(boolean b) {
        this.b = b;
        System.out.println("b set to: " + this.b);
    }

    @Config(columnIndex = 1, rowIndex = 2, width = 1, height = 3)
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
