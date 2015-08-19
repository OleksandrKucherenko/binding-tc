package com.artfulbits.binding.exceptions;

/**
 * Exception often raised when manager detects wrong binding configuration.
 * <p/>
 * Main reasons of existing is:<br/> - 'late binding' has dependency to reflection,<br/> - obfuscation,<br/> - no
 * compilation time checks.<br/>
 */
public class WrongConfigurationError extends ConfigurationError {
  /** Serialization identifier. */
  private static final long serialVersionUID = 498019655467980342L;

  public WrongConfigurationError(final String msg) {
    super(msg);
  }

  public WrongConfigurationError(final String msg, final Throwable inner) {
    super(msg, inner);
  }

  @Override
  public String getMessage() {
    final StringBuilder sb = new StringBuilder();
    final String separator = "\n";

    for (Throwable t : getReasons()) {
      sb.append(separator).append(t.getMessage());
    }

    return super.getMessage() + sb.toString();
  }
}
