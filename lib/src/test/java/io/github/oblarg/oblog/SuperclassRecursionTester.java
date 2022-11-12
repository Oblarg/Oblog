package io.github.oblarg.oblog;

import edu.wpi.first.networktables.GenericEntry;
import io.github.oblarg.oblog.annotations.Log;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SuperclassRecursionTester {
  @Test
  public void testSuperclassRecursion() {
    List<GenericEntry> mockedEntries = new ArrayList<>();

    TestRootContainer rootContainer = new TestRootContainer();

    ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

    Logger.configureLoggingTest(Logger.LogType.LOG, rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

    verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestRecursionSub: Log");
    verify(mocks.getMockedShuffleboard(), never()).getTab("TestRecursionSuper");
    verify(mocks.getMockedShuffleboard(), never()).getTab("TestRecursionBase");


    verify(mocks.getMockedContainer()).add("d", 4);
    verify(mocks.getMockedContainer(), never()).add("d", 10);

    verify(mocks.getMockedContainer(), never()).add("c", 3);

    verify(mocks.getMockedContainer()).add("b", 2);
    verify(mocks.getMockedContainer(), never()).add("b", 10);

    verify(mocks.getMockedContainer(), never()).add("a", 1);


    Logger.updateEntries();

    for (GenericEntry entry : mockedEntries) {
      verify(entry).setValue(any());
    }
  }

  private class TestRootContainer {

    TestRecursionSub test = new TestRecursionSub();

  }
}
