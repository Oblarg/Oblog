package io.github.oblarg.oblog;

import edu.wpi.first.networktables.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IntegerConfigTester {

  @Test
  public void testIntegerConfig() {
    List<GenericEntry> mockedEntries = new ArrayList<>();

    TestRootContainer rootContainer = new TestRootContainer();

    ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

    Logger.configureLoggingTest(Logger.LogType.CONFIG, rootContainer, mocks.getMockedShuffleboard(),
                                mocks.getMockedNTInstance());

    verify(mocks.getMockedShuffleboard()).getTab("TestConfigInteger: Config");
    verify(mocks.getMockedContainer()).add("setI", 0.0d);

    verify(mocks.getMockedNTInstance()).addListener(any(Topic.class), eq(EnumSet.of(
        NetworkTableEvent.Kind.kValueAll)), any());

    assertEquals(0, rootContainer.test.i);

    mocks.getListenerCallback().accept(new NetworkTableEvent(
        mocks.getMockedNTInstance(),
        0, 0, mock(ConnectionInfo.class),
        mock(TopicInfo.class),
        new ValueEventData(
            mocks.getMockedNTInstance(), 0, 0,
            mocks.getMockedNTValue(10)),
        mock(LogMessage.class)));
    Logger.updateEntries();
    assertEquals(10, rootContainer.test.i);

    mocks.getListenerCallback().accept(
        new NetworkTableEvent(
            mocks.getMockedNTInstance(),
            0, 0, null,
            null,
            new ValueEventData(
                mocks.getMockedNTInstance(), 0, 0,
                mocks.getMockedNTValue(11.5)),
            null));
    Logger.updateEntries();
    assertEquals(11, rootContainer.test.i);

  }

  private class TestRootContainer {

    TestConfigInteger test = new TestConfigInteger();

  }
}
