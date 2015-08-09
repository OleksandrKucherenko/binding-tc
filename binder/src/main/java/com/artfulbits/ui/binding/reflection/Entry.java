package com.artfulbits.ui.binding.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

/** Generic facade interface. */
@SuppressWarnings("unused")
public interface Entry {
  /** Entry name. */
  String getName();

  /** Entry name with all data types. */
  String getFullName();

  /** Raw type that we wrap. */
  AccessibleObject getRawType();

  /** Invoke get or set. If args length more than zero - we invoke SET, otherwise GET. */
  Object invoke(final Object receiver, final Object... args) throws IllegalAccessException, InvocationTargetException;
}
