package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

import org.hamcrest.CoreMatchers;

/** Base class for all binding rules keeping. */
public class Binder<Left, Right> {

  private org.hamcrest.Matcher<Right>  mValidation;
  private Selector<?, Property<Left>>  mView;
  private Selector<?, Property<Right>> mStorage;
  private Converter<Left, Right>       mFormatter;

  /* ============================================================================================================== */

  public Binder() {
  }

  /* ============================================================================================================== */

  public <T> Binder<Left, Right> view(final Selector<T, Property<Left>> view) {
    mView = view;

    return this;
  }

  public <T> Binder<Left, Right> model(final Selector<T, Property<Right>> storage) {
    mStorage = storage;

    return this;
  }

  public Binder<Left, Right> format(final Converter<Left, Right> converter) {
    mFormatter = converter;

    return this;
  }

  public Binder<Left, Right> validate(final org.hamcrest.Matcher<Right> validator) {
    mValidation = validator;

    return this;
  }

  /* ============================================================================================================== */

  public Property<Left> resolveView() {
    return null;
  }

  public Property<Right> resolveModel() {
    return null;
  }

  public Converter<Left, Right> resolveFormatter() {
    return mFormatter;
  }

  public org.hamcrest.Matcher<Right> resolveValidation() {
    if (null == mValidation) {
      // by default we validating only data type
      return CoreMatchers.isA(getModelType());
    }

    return mValidation;
  }

  /* ============================================================================================================== */

  protected final Class<Right> getModelType() {
    return null;
  }

  protected final Class<Left> getViewType() {
    return null;
  }
}
