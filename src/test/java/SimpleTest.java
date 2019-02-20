import Annotations.Log;
import Annotations.LogExclude;
import edu.wpi.first.networktables.NetworkTableEntry;

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

class TestLoggableArray implements Loggable {
    TestLoggableBasic[] loggables = {new TestLoggableBasic(1), new TestLoggableBasic(2)};
}

class TestLoggableList implements  Loggable{
    List<TestLoggableBasic> loggables = List.of(new TestLoggableBasic(1), new TestLoggableBasic(2));

    @LogExclude
    TestLoggableBasic excluded = new TestLoggableBasic(5);
}

class TestLoggableBasic implements Loggable{

    TestLoggableBasic(int a){
        this.a = a;
    }

    @Override
    public String configureLogName(){
        return "TestLoggableBasic" + a;
    }

    @Log
    int a;

    @Log
    private int getB(){
        return 2;
    }
}

class TestRecursionBase {

    int a = 1;

    int b = 2;

}

class TestRecursionSuper extends TestRecursionBase implements Loggable{

    @Log
    int b = 10;

    int c = 3;
}

class TestRecursionSub extends TestRecursionSuper {
    @Log
    int d = 4;
}

class TestRootContainer {

    TestLoggableList test = new TestLoggableList();
}