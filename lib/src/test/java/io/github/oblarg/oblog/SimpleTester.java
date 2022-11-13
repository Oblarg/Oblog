package io.github.oblarg.oblog;

import edu.wpi.first.networktables.GenericEntry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SimpleTester {
  @Test
  public void testSimple() {
    List<GenericEntry> mockedEntries = new ArrayList<>();

    TestRootContainer rootContainer = new TestRootContainer();

    ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

    Logger.configureLoggingTest(Logger.LogType.LOG, rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

    verify(mocks.getMockedShuffleboard()).getTab("TestLoggableBasic1: Log");
    verify(mocks.getMockedShuffleboard()).getTab("TestLoggableBasic2: Log");

    verify(mocks.getMockedContainer()).add("a", 1);
    verify(mocks.getMockedContainer()).add("getA", 1);

    verify(mocks.getMockedContainer()).add("a", 2);
    verify(mocks.getMockedContainer()).add("getA", 2);


    Logger.updateEntries();

    for (GenericEntry entry : mockedEntries) {
      verify(entry).setValue(any());
    }
  }

  private class TestRootContainer {

    TestLoggableBasic test1 = new TestLoggableBasic(1);
    TestLoggableBasic test2 = new TestLoggableBasic(2);

  }
}
