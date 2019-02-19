import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

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
        TestRootContainer rootContainer = new TestRootContainer();

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

        Logger.configureLoggingTest(rootContainer, mockedShuffleboard);

        verify(mockedShuffleboard).getTab("TestLoggableChildren");
        verify(mockedShuffleboardContainer).getLayout("TestLoggableBasic1", BuiltInLayouts.kList);
        verify(mockedShuffleboardContainer).getLayout("TestLoggableBasic2", BuiltInLayouts.kList);

        verify(mockedShuffleboardLayout).add("a", 1);
        verify(mockedShuffleboardLayout).add("a", 2);


        Logger.updateEntries();

        for (NetworkTableEntry entry: mockedEntries) {
            verify(entry).setValue(any());
        }

        System.out.println("Success!");
    }
}

class TestLoggableChildren implements Loggable {
    TestLoggableBasic firstChild = new TestLoggableBasic(1);
    TestLoggableBasic secondChild = new TestLoggableBasic(2);
}

class TestLoggableBasic implements Loggable{

    TestLoggableBasic(int a){
        this.a = a;
    }

    @Override
    public String configureLogName(){
        return "TestLoggableBasic" + a;
    }

    @LogDefault
    int a;

    @LogDefault
    private int getB(){
        return 2;
    }
}

class TestRecursionBase {

    int a = 1;

    int b = 2;

}

class TestRecursionSuper extends TestRecursionBase implements Loggable{

    @LogDefault
    int b = 10;

    int c = 3;
}

class TestRecursionSub extends TestRecursionSuper {
    @LogDefault int d = 4;
}

class TestRootContainer {

    TestLoggableChildren test = new TestLoggableChildren();
}