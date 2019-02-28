package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class ArraysAndListsTester {
    @Test
    public void testArraysAndLists() {
        long startTime = System.currentTimeMillis();

        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(Logger.LogType.LOG,rootContainer, mocks.getMockedShuffleboard(), mocks.getMockedNTInstance());

        verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestLoggableBasic1: Log");
        verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestLoggableBasic2: Log");
        verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestLoggableBasic3: Log");
        verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestLoggableBasic4: Log");

        verify(mocks.getMockedContainer()).add("a", 1);
        verify(mocks.getMockedContainer()).add("a", 2);
        verify(mocks.getMockedContainer()).add("a", 3);
        verify(mocks.getMockedContainer()).add("a", 4);

        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic5", BuiltInLayouts.kList);
        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic6", BuiltInLayouts.kList);
        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic7", BuiltInLayouts.kList);
        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic8", BuiltInLayouts.kList);

        verify(mocks.getMockedLayout()).add("a", 5);
        verify(mocks.getMockedLayout()).add("a", 6);
        verify(mocks.getMockedLayout()).add("a", 7);
        verify(mocks.getMockedLayout()).add("a", 8);


        Logger.updateEntries();

        for (NetworkTableEntry entry : mockedEntries) {
            verify(entry).setValue(any());
        }

    }

    private class TestRootContainer {

        TestLoggableBasic[] arrayTest = {new TestLoggableBasic(1), new TestLoggableBasic(2)};

        List<TestLoggableBasic> listTest = List.of(new TestLoggableBasic(3), new TestLoggableBasic(4));

        TestLoggableArray arrayChildTest = new TestLoggableArray(5, 6);

        TestLoggableList listChildTest = new TestLoggableList(7, 8);

    }
}
