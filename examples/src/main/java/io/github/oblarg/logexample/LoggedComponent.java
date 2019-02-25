package io.github.oblarg.logexample;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class LoggedComponent implements Loggable {

    @Log
    private int i = 1;

    private double d;

    @Config.NumberSlider
    private void setD (double d) {
        this.d = d;
        System.out.println("d set to: " + d);
    }
}
