package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ChildRecursionTester {
    @Test
    public void testChildRecursion() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(rootContainer, mocks.getMockedShuffleboard());

        verify(mocks.getMockedShuffleboard(), atLeastOnce()).getTab("TestLoggableChildren");
        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic1", BuiltInLayouts.kList);
        verify(mocks.getMockedContainer(), atLeastOnce()).getLayout("TestLoggableBasic2", BuiltInLayouts.kList);

        verify(mocks.getMockedLayout()).add("a", 1);
        verify(mocks.getMockedLayout()).add("a", 2);


        Logger.updateEntries();

        for (NetworkTableEntry entry : mockedEntries) {
            verify(entry).setValue(any());
        }
    }

    private class TestRootContainer {

        TestLoggableChildren ordinaryChildTest = new TestLoggableChildren(1, 2);

    }
}
