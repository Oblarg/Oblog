package io.github.oblarg.oblog;

class TestLoggableChildren implements Loggable {
    TestLoggableBasic firstChild = new TestLoggableBasic(1);
    TestLoggableBasic secondChild = new TestLoggableBasic(2);
}
