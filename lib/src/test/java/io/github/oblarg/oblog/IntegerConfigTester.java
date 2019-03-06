package io.github.oblarg.oblog;

import edu.wpi.first.networktables.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IntegerConfigTester {

    @Test
    public void testIntegerConfig() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(Logger.LogType.CONFIG, rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

        verify(mocks.getMockedShuffleboard()).getTab("TestConfigInteger: Config");
        verify(mocks.getMockedContainer()).add("setI", 0.0d);

        verify(mocks.getMockedNTInstance()).addEntryListener(any(NetworkTableEntry.class), any(), eq(EntryListenerFlags.kUpdate));

        assertEquals(0, rootContainer.test.i);

        mocks.getListenerCallback().accept(new EntryNotification(mocks.getMockedNTInstance(),
                0, 0, "test", mocks.getMockedNTValue(10), EntryListenerFlags.kUpdate));
        Logger.updateEntries();
        assertEquals(10, rootContainer.test.i);

        mocks.getListenerCallback().accept(new EntryNotification(mocks.getMockedNTInstance(),
                0, 0, "test", mocks.getMockedNTValue(11.5), EntryListenerFlags.kUpdate));
        Logger.updateEntries();
        assertEquals(11, rootContainer.test.i);

    }

    private class TestRootContainer {

        TestConfigInteger test = new TestConfigInteger();

    }
}
