package io.github.oblarg.oblog;


import edu.wpi.first.util.sendable.Sendable;

import static java.util.Objects.requireNonNull;

public class Util {
  public static void logErrorCheck(Object obj, String title, String containerName) {
    requireNonNull(
        obj,
        "Error!  Attempted to log null value with widget title "
            + title
            + " in container "
            + containerName);
    if (!isValidLogType(obj)) {
      throw new IllegalArgumentException(
          "Error!  Attempted to log invalid data type "
              + title
              + " in container "
              + containerName
              + ".  Data was of type "
              + obj.getClass().getTypeName()
              + "; acceptable types are "
              + "Sendable, Number, Boolean, and String");
    }
  }

  private static boolean isValidLogType(Object obj) {
    boolean isValid = false;
    isValid |= obj instanceof Sendable;
    isValid |= obj instanceof Number;
    isValid |= obj instanceof Boolean;
    isValid |= obj instanceof String;

    return isValid;
  }
}
