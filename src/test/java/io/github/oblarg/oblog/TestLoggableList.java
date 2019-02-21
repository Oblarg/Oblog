package io.github.oblarg.oblog;

import io.github.oblarg.oblog.annotations.Log;

import java.util.List;

class TestLoggableList implements Loggable {

    List<TestLoggableBasic> loggables;

    public TestLoggableList(int a1, int a2) {
        loggables = List.of(new TestLoggableBasic(a1), new TestLoggableBasic(a2));
    }
}
