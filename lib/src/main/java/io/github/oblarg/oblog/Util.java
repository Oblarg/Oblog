package io.github.oblarg.oblog;

import static java.util.Objects.requireNonNull;

public class Util {
  public static void nullCheck(Object obj, String title, String containerName) {
    requireNonNull(obj, "Error!  Attempted to log null value with widget title " + title + " in container "
        + containerName);
  }
}
