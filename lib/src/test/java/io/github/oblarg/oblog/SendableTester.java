package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Sendable;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.ShuffleboardMocks;
import io.github.oblarg.oblog.TestLoggableBasic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class SendableTester {

    @Test
    public void testSendable() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(Logger.LogType.LOG,rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

        verify(mocks.getMockedShuffleboard()).getTab("TestSendable: Log");
        verify(mocks.getMockedContainer()).add(eq("drive"), any());
    }

    private class TestRootContainer {

        TestSendable test = new TestSendable();

    }
}
