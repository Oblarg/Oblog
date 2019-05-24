package io.github.oblarg.oblog;

class TestLoggableArray implements Loggable {
  TestLoggableBasic[] loggables;

  public TestLoggableArray(int a1, int a2) {
    loggables = new TestLoggableBasic[]{new TestLoggableBasic(a1), new TestLoggableBasic(a2)};
  }
}
