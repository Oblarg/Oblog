package io.github.oblarg.oblog;

import static org.mockito.Mockito.*;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import io.github.oblarg.oblog.annotations.Log;

class TestSendable implements Loggable {

  @Log.DifferentialDrive
  private DifferentialDrive drive = mock(DifferentialDrive.class);
}
