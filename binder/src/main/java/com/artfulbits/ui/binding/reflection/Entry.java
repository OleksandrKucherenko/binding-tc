package com.artfulbits.ui.binding.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

/** Generic facade interface. */
@SuppressWarnings("unused")
public interface Entry {
  /** Accessable instance name. */
  String getName();

  /** Raw type that we wrap. */
  AccessibleObject getRawType();

  /** Invoke get or set. If args length more than zero - we invoke SET, otherwise GET. */
  Object invoke(final Object receiver, final Object... args) throws IllegalAccessException, InvocationTargetException;
}
