package com.artfulbits.ui.binding;

/**
 * Exception often raised when manager detects wrong binding configuration.
 * <p/>
 * Main reason of existing is: - 'late binding' has dependency to reflection, - obfuscation, - no compilation time
 * check.
 */
public class WrongConfigurationException extends Error {
  /** Serialization identifier. */
  private static final long serialVersionUID = 498019655467980342L;

  public WrongConfigurationException(final String msg) {
    super(msg);
  }

  public WrongConfigurationException(final String msg, final Throwable inner) {
    super(msg, inner);
  }
}
