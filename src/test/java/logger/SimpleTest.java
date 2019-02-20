package logger;

import annotations.Log;
import annotations.LogExclude;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SimpleTest {

    private static List<NetworkTableEntry> mockedEntries = new ArrayList<>();

    private static NetworkTableEntry newMockEntry() {
        NetworkTableEntry entry = mock(NetworkTableEntry.class);
        mockedEntries.add(entry);
        return entry;
    }

    public static void main(String[] args) {
        ShuffleboardWrapper mockedShuffleboard = mock(WrappedShuffleboard.class);
        ShuffleboardContainerWrapper mockedShuffleboardContainer = mock(WrappedShuffleboardContainer.class);
        ShuffleboardLayoutWrapper mockedShuffleboardLayout = mock(WrappedShuffleboardLayout.class);
        ShuffleboardWidgetWrapper mockedShuffleboardWidget = mock(WrappedShuffleboardWidget.class);


        when(mockedShuffleboard.getTab(any())).thenReturn(mockedShuffleboardContainer);
        when(mockedShuffleboardContainer.getLayout(any(), any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardContainer.add(any(), any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardLayout.getLayout(any(),any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardLayout.add(any(), any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardLayout.withProperties(any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardWidget.withProperties(any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardWidget.withWidget(any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardWidget.getEntry()).thenReturn(newMockEntry());


        TestRootContainer rootContainer = new TestRootContainer();
        rootContainer.init();

        Logger.configureLoggingTest(rootContainer, mockedShuffleboard);

        verify(mockedShuffleboard).getTab("TestCycleOuter");
        verify(mockedShuffleboardContainer).getLayout("TestCycleInner", BuiltInLayouts.kList);
        verify(mockedShuffleboardContainer).add("s", "outer");
        verify(mockedShuffleboardLayout).add("s", "inner");


        Logger.updateEntries();

        for (NetworkTableEntry entry: mockedEntries) {
            verify(entry).setValue(any());
        }

        System.out.println("Success!");
    }
}

class TestRootContainer {

    TestCycleOuter outer = new TestCycleOuter();

    void init(){
        TestCycleInner inner = new TestCycleInner();
        inner.setOuter(outer);
        outer.setInner(inner);
    }

}