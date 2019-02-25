package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Config;

public class TestConfigSimple implements Loggable {

    boolean b;

    int i;

    @Config
    void setB(boolean b) {
        this.b = b;
    }

    @Config
    void setI(int i) {
        this.i = i;
    }
}
