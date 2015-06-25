package android.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

public final class Log {
  public static final int ASSERT = 7;
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int INFO = 4;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  Log() {
  }

  public static int v(String tag, String msg) {
    return -1;
  }

  public static int v(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static int d(String tag, String msg) {
    return -1;
  }

  public static int d(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static int i(String tag, String msg) {
    return -1;
  }

  public static int i(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static int w(String tag, String msg) {
    return -1;
  }

  public static int w(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static boolean isLoggable(String var0, int var1) {
    return true;
  }

  public static int w(String tag, Throwable tr) {
    return -1;
  }

  public static int e(String tag, String msg) {
    return -1;
  }

  public static int e(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static int wtf(String tag, String msg) {
    return -1;
  }

  public static int wtf(String tag, Throwable tr) {
    return -1;
  }

  public static int wtf(String tag, String msg, Throwable tr) {
    return -1;
  }

  public static String getStackTraceString(Throwable tr) {
    if (tr == null) {
      return "";
    }

    // This is to reduce the amount of log spew that apps do in the non-error
    // condition of the network being unavailable.
    Throwable t = tr;
    while (t != null) {
      if (t instanceof UnknownHostException) {
        return "";
      }
      t = t.getCause();
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    tr.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  public static int println(int priority, String tag, String msg) {
    return -1;
  }
}