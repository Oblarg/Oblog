package io.github.oblarg.oblog;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleConfigTester {

    @Test
    public void testSimpleConfig() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(Logger.LogType.CONFIG,rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

        verify(mocks.getMockedShuffleboard()).getTab("TestConfigSimple: Config");
        verify(mocks.getMockedContainer()).add("setB", false);
        verify(mocks.getMockedContainer()).add("setI", 0);

        verify(mocks.getMockedNTInstance(), atLeastOnce()).addEntryListener(any(NetworkTableEntry.class), any(), eq(EntryListenerFlags.kUpdate));

        assertEquals(rootContainer.test.b, false);
        assertEquals(rootContainer.test.i, 0);
    }

    private class TestRootContainer {

       TestConfigSimple test = new TestConfigSimple();

    }
}
