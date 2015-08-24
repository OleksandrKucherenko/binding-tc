package com.artfulbits.binding.exceptions;

/**
 * Exception often raised when manager detects wrong binding configuration.
 * <p/>
 * Main reasons of existing is:<br/> - 'late binding' has dependency to reflection,<br/> - obfuscation,<br/> - no
 * compilation time checks.<br/>
 */
public class WrongConfigurationError extends ConfigurationError {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Serialization identifier. */
  private static final long serialVersionUID = 498019655467980342L;

	/* [ CONSTRUCTORS ] ============================================================================================== */

  public WrongConfigurationError(final String msg) {
    super(msg);
  }

  public WrongConfigurationError(final String msg, final Throwable inner) {
    super(msg, inner);
  }

	/* [ GETTER / SETTER METHODS ] =================================================================================== */

  /** Create a long message from inner exceptions. */
  @Override
  public String getMessage() {
    final StringBuilder sb = new StringBuilder();
    final String separator = "\n";

    for (final Throwable t : getReasons()) {
      sb.append(separator).append(t.getMessage());
    }

    return super.getMessage() + sb.toString();
  }
}
