package com.artfulbits.sample;

import android.support.annotation.NonNull;

import java.util.logging.Level;

/** interface for sharing logger between  several instances. */
public interface ILogger {
  /** Get logs storage. */
  @NonNull
  StringBuilder getRawLogger();

  /** Assign a logs storage. */
  void setRawLogger(@NonNull final StringBuilder logger);

  /** put message into Log. */
  void log(final Level level, final String tag, final String msg);
}
