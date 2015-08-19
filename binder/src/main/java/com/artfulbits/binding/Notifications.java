package com.artfulbits.binding;

/** Implement interface if you needs notifications on changed values. */
public interface Notifications {
  /** raised in moment when detected changed value. */
  void onChanged();
}
