package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

/**
 * Basic listener that controls value changes in attached instance. Also it triggers binding on capturing value
 * change or user actions - depends on implementation and logic that developer wants to keep.
 */
public interface Listener<T> {
  void attach(final Property property, final T instance);

  void detach(final Property property, final T instance);
}
