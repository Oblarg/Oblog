package io.github.oblarg.oblog;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

class WrappedShuffleboard implements ShuffleboardWrapper {
  @Override
  public ShuffleboardContainerWrapper getTab(String title) {
    return new WrappedShuffleboardContainer(Shuffleboard.getTab(title));
  }
}
