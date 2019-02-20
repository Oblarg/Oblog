package io.github.oblarg.oblog;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.List;

import static org.mockito.Mockito.*;

class ShuffleboardMocks {

    List<NetworkTableEntry> mockedEntries;

    private ShuffleboardWrapper mockedShuffleboard = mock(WrappedShuffleboard.class);
    private ShuffleboardContainerWrapper mockedContainer = mock(WrappedShuffleboardContainer.class);
    private ShuffleboardLayoutWrapper mockedLayout = mock(WrappedShuffleboardLayout.class);
    private ShuffleboardWidgetWrapper mockedWidget = mock(WrappedShuffleboardWidget.class);

    ShuffleboardMocks(List<NetworkTableEntry> mockedEntries) {
        this.mockedEntries = mockedEntries;
        when(mockedShuffleboard.getTab(any())).thenReturn(mockedContainer);
        when(mockedContainer.getLayout(any(), any())).thenReturn(mockedLayout);
        when(mockedContainer.add(any(), any())).thenReturn(mockedWidget);
        when(mockedLayout.getLayout(any(), any())).thenReturn(mockedLayout);
        when(mockedLayout.add(any(), any())).thenReturn(mockedWidget);
        when(mockedLayout.withProperties(any())).thenReturn(mockedLayout);
        when(mockedWidget.withProperties(any())).thenReturn(mockedWidget);
        when(mockedWidget.withWidget(any())).thenReturn(mockedWidget);
        when(mockedWidget.getEntry()).thenReturn(newMockEntry(mockedEntries));
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

    ShuffleboardWidgetWrapper getMockedWidget() {
        return mockedWidget;
    }

    private static NetworkTableEntry newMockEntry(List<NetworkTableEntry> mockedEntries) {
        NetworkTableEntry entry = mock(NetworkTableEntry.class);
        mockedEntries.add(entry);
        return entry;
    }
}