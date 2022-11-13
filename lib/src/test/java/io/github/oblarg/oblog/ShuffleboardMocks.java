package io.github.oblarg.oblog;

import edu.wpi.first.networktables.*;
import edu.wpi.first.util.sendable.Sendable;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class ShuffleboardMocks {

  List<GenericEntry> mockedEntries;

  private ShuffleboardWrapper mockedShuffleboard = mock(WrappedShuffleboard.class);
  private ShuffleboardContainerWrapper mockedContainer = mock(WrappedShuffleboardContainer.class);
  private ShuffleboardLayoutWrapper mockedLayout = mock(WrappedShuffleboardLayout.class);
  private SimpleWidgetWrapper mockedSimpleWidget = mock(WrappedSimpleWidget.class);
  private ComplexWidgetWrapper mockedComplexWidget = mock(WrappedComplexWidget.class);

  private NetworkTableInstance mockedNTInstance = mock(NetworkTableInstance.class);

  private NetworkTableValue mockedNTValue = mock(NetworkTableValue.class);

  private Consumer<NetworkTableEvent> listenerCallback;

  ShuffleboardMocks(List<GenericEntry> mockedEntries) {
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
    when(mockedSimpleWidget.getEntry()).thenAnswer(invocation -> newMockEntry(mockedEntries));
    when(mockedComplexWidget.withProperties(any())).thenReturn(mockedComplexWidget);
    when(mockedComplexWidget.withWidget(any())).thenReturn(mockedComplexWidget);
    when(mockedComplexWidget.withPosition(anyInt(), anyInt())).thenReturn(mockedComplexWidget);
    when(mockedComplexWidget.withSize(anyInt(), anyInt())).thenReturn(mockedComplexWidget);
    when(mockedNTInstance.addListener(any(Topic.class),
                                      any(),
                                      any())).thenAnswer(
        invocation -> {
          System.out.println("hello");
          listenerCallback = (Consumer<NetworkTableEvent>) invocation.getArguments()[2];
          return 0;
        });
  }

  Consumer<NetworkTableEvent> getListenerCallback() {
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

  private static GenericEntry newMockEntry(List<GenericEntry> mockedEntries) {
    GenericEntry entry = mock(GenericEntry.class);
    when(entry.getTopic()).thenReturn(mock(Topic.class));
    mockedEntries.add(entry);
    return entry;
  }
}