package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

/** Base class for all binding rules keeping. */
public class Binder {

  public Binder() {
  }

  public <T extends Property> Binder view(final Matcher<T> view) {
    return this;
  }

  public <T extends Property> Binder model(final Matcher<T> storage) {
    return this;
  }

  public <Out, In> Binder formatter(final Converter<Out, In> converter) {
    // TODO: store converter instance

    return this;
  }

  public <T> Binder validator(final org.hamcrest.Matcher<T> validator) {
    // TODO: store validation matcher

    return this;
  }

  /* ============================================================================================================== */

  public <T> Binder listenOnView(final Listener<T> listener) {
    // TODO: store for late binding when actually Property instances will be calculated

    return this;
  }

  public <T> Binder listenOnModel(final Listener<T> listener) {
    // TODO: store for late binding when actually Property instances will be calculated

    return this;
  }
}
