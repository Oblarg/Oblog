package io.github.oblarg.logexample.commands;

import edu.wpi.first.wpilibj.command.Command;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class LoggedCommand extends Command implements Loggable {

    private double timeout;

    @Config
    private Command thisCommand = this;

    //logs a graph of time since initialized
    @Log.Graph(name = "Time Elapsed", visibleTime = 15)
    private double time;

    //configures the name of this loggable programmatically
    //necessary if there will be more than one of the same object to avoid namespace collision on Shuffleboard
    @Override
    public String configureLogName() {
        return "LoggedCommand " + timeout;
    }

    //runs for specified amount of time

    public LoggedCommand(double timeout) {
        super(timeout);
        this.timeout = timeout;

        setRunWhenDisabled(true);
    }

    @Override
    public void execute() {
        time = timeSinceInitialized();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }
}
