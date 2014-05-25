package com.truecaller.ui.binding.reflection;

import com.truecaller.ui.binding.Listener;

/** class is responsible for accessing a specific abstract 'field' by using reflection. */
public class Property {

  public <T> Property listeners(final Listener<T> listeners) {
    return this;
  }

  public <T> void trigger(final Listener<T> listener) {
  }
}
