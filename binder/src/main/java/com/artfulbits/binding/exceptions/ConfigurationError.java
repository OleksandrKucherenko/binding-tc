package com.artfulbits.binding.exceptions;

import android.support.annotation.NonNull;

/** Runtime configuration error. */
public class ConfigurationError extends Error {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Serialization ID. */
  private static final long serialVersionUID = -4372495279488009466L;

	/* [ MEMBERS ] =================================================================================================== */

  /** List of exceptions that are the reason of the throwing. */
  private final Throwable[] mReasons;

	/* [ CONSTRUCTORS ] ============================================================================================== */

  /**
   * Instantiates a new Configuration error with message.
   *
   * @param message the message
   */
  public ConfigurationError(final String message) {
    super(message);

    mReasons = new Throwable[0];
  }

  /**
   * Instantiates a new Configuration error with message and reason.
   *
   * @param message the message
   * @param reason  the reason
   */
  public ConfigurationError(final String message, @NonNull final Throwable reason) {
    super(message, reason);

    mReasons = new Throwable[]{reason};
  }

  /**
   * Instantiates a new Configuration error with message and multiple reasons.
   *
   * @param message the message
   * @param reasons the reasons
   */
  public ConfigurationError(final String message, @NonNull final Throwable... reasons) {
    super(message, reasons[0]);

    mReasons = reasons;
  }

	/* [ GETTER / SETTER METHODS ] =================================================================================== */

  /**
   * Get array of reasons.
   *
   * @return the throwable [ ]
   */
  @NonNull
  public Throwable[] getReasons() {
    return mReasons;
  }
}
