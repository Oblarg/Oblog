package logger;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class SimpleTester {
    @Test
    public void testSimple() {
        List<NetworkTableEntry> mockedEntries = new ArrayList<>();

        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardMocks mocks = new ShuffleboardMocks(mockedEntries);

        Logger.configureLoggingTest(rootContainer, mocks.getMockedShuffleboard());

        verify(mocks.getMockedShuffleboard()).getTab("TestLoggableBasic1");
        verify(mocks.getMockedShuffleboard()).getTab("TestLoggableBasic2");

        verify(mocks.getMockedContainer()).add("a", 1);
        verify(mocks.getMockedContainer()).add("getA", 1);

        verify(mocks.getMockedContainer()).add("a", 2);
        verify(mocks.getMockedContainer()).add("getA", 2);



        Logger.updateEntries();

        for (NetworkTableEntry entry: mockedEntries) {
            verify(entry).setValue(any());
        }
    }

    private class TestRootContainer {

        TestLoggableBasic test1 = new TestLoggableBasic(1);
        TestLoggableBasic test2 = new TestLoggableBasic(2);

    }
}
