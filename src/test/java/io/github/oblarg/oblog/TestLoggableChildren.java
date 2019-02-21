package io.github.oblarg.oblog;

class TestLoggableChildren implements Loggable {

    TestLoggableBasic firstChild;
    TestLoggableBasic secondChild;

    TestLoggableChildren(int a1, int a2) {
        firstChild = new TestLoggableBasic(a1);
        secondChild = new TestLoggableBasic(a2);
    }
}
