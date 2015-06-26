package com.artfulbits.ui.binding;

/**
 * Exception often raised when manager detects wrong binding configuration.
 * <p/>
 * Main reason of existing is:
 * - 'late binding' has dependency to reflection,
 * - obfuscation,
 * - no compilation time check.
 */
public class WrongConfigurationException extends Exception {

  public WrongConfigurationException(final String msg) {
    super(msg);
  }

  public WrongConfigurationException(final String msg, final Throwable inner) {
    super(msg, inner);
  }
}
