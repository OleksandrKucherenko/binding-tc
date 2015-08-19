package com.artfulbits.binding;

import android.support.annotation.NonNull;

/**
 * Basic listener that controls value changes in attached instance. Also it triggers binding on capturing value change
 * or user actions - depends on implementation and logic that developer wants to keep.
 */
public interface Listener {
  /** Do listener binding to corresponding instance of model or view. */
  Listener binding(@NonNull final Selector<?, ?> instance);

  /** Give a promise to 'Notify' specific listener when value changed. */
  void willNotify(@NonNull final Notifications listener);

  /** Detach specific listener from notifications loop. */
  void detach(@NonNull final Notifications listener);
}
