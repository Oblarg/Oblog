package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Config;

public class TestConfigInteger implements Loggable {


    int i;

    @Config
    void setI(int i) {
        this.i = i;
    }
}
