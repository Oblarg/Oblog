package logger;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CyclicDetectionTester {

    @Test
    public void testCyclic() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);


        Logger.configureLoggingTest(rootContainer, mocks.getMockedShuffleboard());
        verify(mocks.getMockedShuffleboard()).getTab("TestCycleOuter");
        verify(mocks.getMockedContainer()).getLayout("TestCycleInner", BuiltInLayouts.kList);
        verify(mocks.getMockedContainer()).add("s", "outer");
        verify(mocks.getMockedLayout()).add("s", "inner");


        Logger.updateEntries();

        for (NetworkTableEntry entry: mockedEntries) {
            verify(entry).setValue(any());
        }
    }

    private class TestRootContainer {

        private TestCycleOuter outer = new TestCycleOuter();

        TestRootContainer(){
            TestCycleInner inner = new TestCycleInner();
            inner.setOuter(outer);
            outer.setInner(inner);
        }

    }
}

