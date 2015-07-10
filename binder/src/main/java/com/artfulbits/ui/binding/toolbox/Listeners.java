package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.artfulbits.ui.binding.Listener;
import com.artfulbits.ui.binding.Notifications;
import com.artfulbits.ui.binding.Selector;

import java.util.HashSet;
import java.util.Set;

/** Implementation of common listeners. */
@SuppressWarnings("unused")
public final class Listeners {
  /** Generic EMPTY listener. */
  public final static Listener NONE = new Listener() {
    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      // do nothing...

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications selector) {
      // do nothing
    }

    @Override
    public void detach(@NonNull final Notifications selector) {
      // do nothing
    }
  };

  /** hidden constructor. */
  private Listeners() {
    throw new AssertionError();
  }

  /**
   * Join multiple listeners into one expression.
   *
   * @param listeners the array of listeners
   * @return the joined listeners instance.
   */
  @NonNull
  public static Listener anyOf(@NonNull final Listener... listeners) {
    return new Listener() {
      @Override
      public Listener binding(@NonNull final Selector<?, ?> instance) {
        for (Listener listener : listeners) {
          listener.binding(instance);
        }

        return this;
      }

      @Override
      public void willNotify(@NonNull final Notifications selector) {
        for (Listener listener : listeners) {
          listener.willNotify(selector);
        }
      }

      @Override
      public void detach(@NonNull final Notifications selector) {
        for (Listener listener : listeners) {
          listener.detach(selector);
        }
      }
    };
  }

  /**
   * Get 'empty listener' instance. this is simply a stub that can be used in expressions but it doing absolutely
   * nothing.
   */
  @NonNull
  public static Listener none() {
    return NONE;
  }

  /** Detect text changes and raise data exchange on that. */
  public static Listener onTextChanged() {
    return new TextWatcherListener();
  }

  /** Detect focus loss and raise data exchange on that. */
  public static Listener onFocusLost() {
    return new FocusLostListener();
  }

  /** Listen to text changes of the TextView control. */
  private static class TextWatcherListener implements Listener, TextWatcher {
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public void afterTextChanged(final Editable s) {

    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      final Object view = instance.getRuntimeInstance();

      if (view instanceof TextView) {
        final TextView tv = (TextView) view;

        tv.addTextChangedListener(this);
      }

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }
  }

  /** Listen to focus loss of the View. */
  private static class FocusLostListener implements Listener, View.OnFocusChangeListener {
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();

    @Override
    public void onFocusChange(final View v, final boolean hasFocus) {
      // we listen only focus loss
      if (hasFocus) return;

      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      final Object view = instance.getRuntimeInstance();

      if (view instanceof View) {
        final View tv = (View) view;

        tv.setOnFocusChangeListener(this);
      }

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }
  }
}
