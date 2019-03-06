package io.github.oblarg.oblog;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Sendable;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class ShuffleboardMocks {

    List<NetworkTableEntry> mockedEntries;

    private ShuffleboardWrapper mockedShuffleboard = mock(WrappedShuffleboard.class);
    private ShuffleboardContainerWrapper mockedContainer = mock(WrappedShuffleboardContainer.class);
    private ShuffleboardLayoutWrapper mockedLayout = mock(WrappedShuffleboardLayout.class);
    private SimpleWidgetWrapper mockedSimpleWidget = mock(WrappedSimpleWidget.class);
    private ComplexWidgetWrapper mockedComplexWidget = mock(WrappedComplexWidget.class);

    private NetworkTableInstance mockedNTInstance = mock(NetworkTableInstance.class);

    private NetworkTableValue mockedNTValue = mock(NetworkTableValue.class);

    private Consumer<EntryNotification> listenerCallback;

    ShuffleboardMocks(List<NetworkTableEntry> mockedEntries) {
        this.mockedEntries = mockedEntries;
        when(mockedShuffleboard.getTab(any())).thenReturn(mockedContainer);
        when(mockedContainer.getLayout(any(), any())).thenReturn(mockedLayout);
        when(mockedContainer.add(any(), any(Object.class))).thenReturn(mockedSimpleWidget);
        when(mockedContainer.add(any(), any(Sendable.class))).thenReturn(mockedComplexWidget);
        when(mockedLayout.getLayout(any(), any())).thenReturn(mockedLayout);
        when(mockedLayout.add(any(), any(Object.class))).thenReturn(mockedSimpleWidget);
        when(mockedLayout.add(any(), any(Sendable.class))).thenReturn(mockedComplexWidget);
        when(mockedLayout.withProperties(any())).thenReturn(mockedLayout);
        when(mockedLayout.withPosition(anyInt(), anyInt())).thenReturn(mockedLayout);
        when(mockedLayout.withSize(anyInt(), anyInt())).thenReturn(mockedLayout);
        when(mockedSimpleWidget.withProperties(any())).thenReturn(mockedSimpleWidget);
        when(mockedSimpleWidget.withWidget(any())).thenReturn(mockedSimpleWidget);
        when(mockedSimpleWidget.withPosition(anyInt(), anyInt())).thenReturn(mockedSimpleWidget);
        when(mockedSimpleWidget.withSize(anyInt(), anyInt())).thenReturn(mockedSimpleWidget);
        when(mockedSimpleWidget.getEntry()).thenReturn(newMockEntry(mockedEntries));
        when(mockedComplexWidget.withProperties(any())).thenReturn(mockedComplexWidget);
        when(mockedComplexWidget.withWidget(any())).thenReturn(mockedComplexWidget);
        when(mockedComplexWidget.withPosition(anyInt(), anyInt())).thenReturn(mockedComplexWidget);
        when(mockedComplexWidget.withSize(anyInt(), anyInt())).thenReturn(mockedComplexWidget);
        when(mockedNTInstance.addEntryListener(any(NetworkTableEntry.class), any(), eq(EntryListenerFlags.kUpdate))).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        listenerCallback = (Consumer<EntryNotification>) invocation.getArguments()[1];
                        return 0;
                    }
                });
    }

    Consumer<EntryNotification> getListenerCallback() {
        return listenerCallback;
    }

    NetworkTableValue getMockedNTValue(Object value) {
        when(mockedNTValue.getValue()).thenReturn(value);
        return mockedNTValue;
    }

    ShuffleboardWrapper getMockedShuffleboard() {
        return mockedShuffleboard;
    }

    ShuffleboardContainerWrapper getMockedContainer() {
        return mockedContainer;
    }

    ShuffleboardLayoutWrapper getMockedLayout() {
        return mockedLayout;
    }

    SimpleWidgetWrapper getMockedSimpleWidget() {
        return mockedSimpleWidget;
    }

    NetworkTableInstance getMockedNTInstance() {
        return mockedNTInstance;
    }

    private static NetworkTableEntry newMockEntry(List<NetworkTableEntry> mockedEntries) {
        NetworkTableEntry entry = mock(NetworkTableEntry.class);
        mockedEntries.add(entry);
        return entry;
    }
}