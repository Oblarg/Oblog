package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import io.github.oblarg.oblog.annotations.Config;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

public class ConfigMethodNameTester {
  @Test
  public void testConfigMethodName() {
    List<NetworkTableEntry> mockedEntries = new ArrayList<>();

    TestRootContainer rootContainer = new TestRootContainer();

    ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

    Logger.configureLoggingTest(Logger.LogType.CONFIG, rootContainer, mocks.getMockedShuffleboard(),
                                mocks.getMockedNTInstance());

    verify(mocks.getMockedShuffleboard()).getTab("TestRootContainer");
    verify(mocks.getMockedContainer()).add("setFoo", 0d);
    verify(mocks.getMockedContainer()).getLayout("setFooBar", BuiltInLayouts.kList);
    verify(mocks.getMockedLayout()).add("foo", 0d);
    verify(mocks.getMockedLayout()).add("bar", 0d);
  }

  private class TestRootContainer {
    @Config(methodName = "setFoo",
            methodTypes = {double.class})
    @Config(methodName = "setFooBar",
            methodTypes = {double.class, double.class})
    HasSetters test = new HasSetters();
  }

  private class HasSetters {
    void setFoo(double foo) {
    }

    void setFooBar(double foo, double bar) {
    }
  }
}
