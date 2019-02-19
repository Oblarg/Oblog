import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;

import static org.mockito.Mockito.*;

public class SimpleTest {

    public static void main(String[] args) {
        TestRootContainer rootContainer = new TestRootContainer();

        ShuffleboardWrapper mockedShuffleboard = mock(WrappedShuffleboard.class);
        ShuffleboardContainerWrapper mockedShuffleboardContainer = mock(WrappedShuffleboardContainer.class);
        ShuffleboardLayoutWrapper mockedShuffleboardLayout = mock(WrappedShuffleboardLayout.class);
        ShuffleboardWidgetWrapper mockedShuffleboardWidget = mock(WrappedShuffleboardWidget.class);
        NetworkTableEntry mockedEntry = mock(NetworkTableEntry.class);


        when(mockedShuffleboard.getTab(any())).thenReturn(mockedShuffleboardContainer);
        when(mockedShuffleboardContainer.getLayout(any(), any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardContainer.add(any(), any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardLayout.getLayout(any(),any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardLayout.add(any(), any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardLayout.withProperties(any())).thenReturn(mockedShuffleboardLayout);
        when(mockedShuffleboardWidget.withProperties(any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardWidget.withWidget(any())).thenReturn(mockedShuffleboardWidget);
        when(mockedShuffleboardWidget.getEntry()).thenReturn(mockedEntry);

        Logger.configureLoggingTest(rootContainer, mockedShuffleboard);

        verify(mockedShuffleboard).getTab("TestLoggableBasic");
        verify(mockedShuffleboardContainer).add("a", 1);

        System.out.println("Success!");
    }
}

class TestLoggableBasic implements Loggable{

    TestLoggableBasic(int a){
        this.a = a;
    }

    @LogDefault
    int a;
}

class TestRootContainer {

    TestLoggableBasic test1 = new TestLoggableBasic(1);
}