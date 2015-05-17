package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

import org.hamcrest.CoreMatchers;

/** Base class for all binding rules keeping. */
public class Binder<TLeft, TRight> {
  /** Reference on binding owner. */
  private BindingManager mManager;
  /** Reference on view instance. */
  private Selector<?, Property<TLeft>> mView;
  /** Reference on storage instance. */
  private Selector<?, Property<TRight>> mStorage;
  /** View changes listener. */
  private Listener<?> mOnView;
  /** Data model changes listener. */
  private Listener<?> mOnModel;
  /** Data type converter. */
  private Converter<TLeft, TRight> mFormatter;
  /** Data validation. */
  private org.hamcrest.Matcher<TRight> mValidation;

  /* ============================================================================================================== */

  /* package */ Binder() {
  }

  /* package */ Binder<TLeft, TRight> attachToManager(final BindingManager manager) {
    mManager = manager;

    // do self registration
    mManager.getBindings().add(this);

    return this;
  }

  /* ============================================================================================================== */

  public Binder<TLeft, TRight> view(final Selector<?, Property<TLeft>> view) {
    mView = view;

    onView(mOnView);

    return this;
  }

  public Binder<TLeft, TRight> model(final Selector<?, Property<TRight>> storage) {
    mStorage = storage;

    onModel(mOnModel);

    return this;
  }

  public Binder<TLeft, TRight> format(final Converter<TLeft, TRight> converter) {
    mFormatter = converter;

    return this;
  }

  public Binder<TLeft, TRight> validate(final org.hamcrest.Matcher<TRight> validator) {
    mValidation = validator;

    return this;
  }

  public Binder<TLeft, TRight> onView(final Listener<?> listener) {
    mOnView = listener;

    if (null != mView) {
      mView.listenTo(mOnView);
    }

    return this;
  }

  public Binder<TLeft, TRight> onModel(final Listener<?> listener) {
    mOnModel = listener;

    if (null != mStorage) {
      mStorage.listenTo(mOnModel);
    }

    return this;
  }

  /* ============================================================================================================== */

  public Property<TLeft> resolveView() {
    return null;
  }

  public Property<TRight> resolveModel() {
    return null;
  }

  public Converter<TLeft, TRight> resolveFormatter() {
    return mFormatter;
  }

  public org.hamcrest.Matcher<TRight> resolveValidation() {
    if (null == mValidation) {
      // by default we validating only data type
      return CoreMatchers.isA(getModelType());
    }

    return mValidation;
  }

  /* ============================================================================================================== */

  protected final Class<TRight> getModelType() {
    return null;
  }

  protected final Class<TLeft> getViewType() {
    return null;
  }
}
