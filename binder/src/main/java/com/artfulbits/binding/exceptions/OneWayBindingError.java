package com.artfulbits.binding.exceptions;

/**
 * Formatter can raise this error in case of One-way allowed binding. Binding library will skip the binding instance
 * with such exception.
 */
public class OneWayBindingError extends Error {
  /** Serialization id. */
  private static final long serialVersionUID = -3510027916462788278L;
}
