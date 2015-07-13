package com.artfulbits.ui.binding;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.reflection.Property;
import com.artfulbits.ui.binding.toolbox.Formatter;

import org.hamcrest.CoreMatchers;

/**
 * Base class for all binding rules keeping.
 *
 * @param <TLeft>  the type of View field
 * @param <TRight> the type of Model field
 */
@SuppressWarnings("unused")
public class Binder<TLeft, TRight> {
  /** Reference on binding owner. */
  private BindingsManager mManager;
  /** Reference on view instance. */
  private Selector<?, TLeft> mView;
  /** Reference on storage instance. */
  private Selector<?, TRight> mModel;
  /** View changes listener. */
  private Listener mOnView;
  /** Data model changes listener. */
  private Listener mOnModel;
  /** Data type converter. */
  private Formatting<TLeft, TRight> mFormatting;
  /** Data validation. */
  private org.hamcrest.Matcher<TRight> mValidation;
  /** Value used in last evaluated/extracted/exchange operation. View side. */
  private TLeft mLastLeft;
  /** Value used in last evaluated/extracted/exchange operation. Model side. */
  private TRight mLastRight;
  /** Callback that we raise on validation success. */
  private Success mOnSuccess;
  /** Callback that we raise on validation failure. */
  private Failure mOnFailure;

  /* ============================================================================================================== */

  /* package */ Binder() {
  }

  /* package */ Binder<TLeft, TRight> attachToManager(final BindingsManager manager) {
    mManager = manager;

    // do self registration
    mManager.getBindings().add(this);

    return this;
  }

  /* ============================================================================================================== */

  /** Assign View selector to a binder. */
  public Binder<TLeft, TRight> view(@NonNull final Selector<?, TLeft> view) {
    mView = view;

    onView(mOnView);

    return this;
  }

  /** Assign Model selector to a binder. */
  public Binder<TLeft, TRight> model(@NonNull final Selector<?, TRight> model) {
    mModel = model;

    onModel(mOnModel);

    return this;
  }

  /** Attach to binder a formatting provider. */
  public Binder<TLeft, TRight> format(@NonNull final Formatting<TLeft, TRight> formatting) {
    mFormatting = formatting;

    return this;
  }

  /** Attach to binder a validation expression. */
  public Binder<TLeft, TRight> validate(@NonNull final org.hamcrest.Matcher<TRight> validator) {
    mValidation = validator;

    return this;
  }

  /** Attach listener to View. Listener can force data exchange. */
  public Binder<TLeft, TRight> onView(@NonNull final Listener listener) {
    mOnView = listener;

    if (null != mView) {
      listener.binding(mView);

      listener.willNotify(new Notifications() {
        @Override
        public void onChanged() {
          mView.onChanged();

          onViewChanged();
        }
      });
    }

    return this;
  }

  /** Attach listener to Model. Listener can force data exchange. */
  public Binder<TLeft, TRight> onModel(@NonNull final Listener listener) {
    mOnModel = listener;

    if (null != mModel) {
      listener.binding(mModel);

      listener.willNotify(new Notifications() {
        @Override
        public void onChanged() {
          mModel.onChanged();

          onModelChanged();
        }
      });
    }

    return this;
  }

  /**
   * Attach callback/listener 'on validation success'.
   *
   * @param ok the instance of listener
   * @return this binder, for chained calls
   */
  public Binder<TLeft, TRight> onSuccess(@NonNull final Success ok) {
    mOnSuccess = ok;

    return this;
  }

  /**
   * Atach callback/listnere 'On validation failure'.
   *
   * @param failure the instance of listener
   * @return this binder, for chained calls
   */
  public Binder<TLeft, TRight> onFailure(@NonNull final Failure failure) {
    mOnFailure = failure;

    return this;
  }

  /* ============================================================================================================== */

  /** Notify manager that binder detects view side changes. */
  protected void onViewChanged() {
    if (null != mManager)
      mManager.notifyOnViewChanged(this);
  }

  /** Notify manager that binder detects model side changes. */
  protected void onModelChanged() {
    if (null != mManager)
      mManager.notifyOnModelChanged(this);
  }

  /** Notify manager that validation successfully passed. */
  protected void onValidationSuccess() {
    if (null != mManager)
      mManager.notifyOnSuccess(this);
  }

  /** Notify manager that validation failed. */
  protected void onValidationFailure() {
    if (null != mManager)
      mManager.notifyOnFailure(this);
  }

  /* ============================================================================================================== */

  @NonNull
  public Property<TLeft> resolveView() {
    return null;
  }

  @NonNull
  public Property<TRight> resolveModel() {
    return null;
  }

  @NonNull
  public Formatting<TLeft, TRight> resolveFormatting() {
    if (null == mFormatting)
      mFormatting = Formatter.direct();

    return mFormatting;
  }

  @NonNull
  public org.hamcrest.Matcher<TRight> resolveValidation() {
    if (null == mValidation) {
      // by default we validating only data type
      return CoreMatchers.isA(getModelType());
    }

    return mValidation;
  }

  /**
   * Do data exchange in direction: View --> Model.
   * <p>
   * Data flow: View --> IsChanged --> Formatter --> Validator --> Is Changed --> Model;
   */
  public void pop() {
    // get value from View
    final TLeft lValue = resolveView().get(getRuntimeView());

    // if no changes since last request
    if (mLastLeft == lValue) return;

    // store Value in cache
    mLastLeft = lValue;

    // formatter
    final TRight rValue = resolveFormatting().toIn(lValue);

    // validation
    if (resolveValidation().matches(rValue)) {
      onValidationSuccess();
    } else {
      onValidationFailure();
      return;  // no other steps needed in pop
    }

    // if no changes since last request
    if (mLastRight == rValue) return;

    // store value in cache
    mLastRight = rValue;

    // update Model
    resolveModel().set(getRuntimeModel(), rValue);
  }

  /**
   * Do data exchange in direction: Model --> View.
   * <p>
   * Data flow: Model --> Is Changed --> Validator --> Formatter --> Is Changed --> View.
   */
  public void push() {
    // extract the value
    final TRight rValue = resolveModel().get(getRuntimeModel());

    // is changed?
    if (mLastRight == rValue) return;

    // update value in cache
    mLastRight = rValue;

    // validation passed?
    if (resolveValidation().matches(rValue)) {
      onValidationSuccess();
    } else {
      onValidationFailure();
      return; // no other steps needed in push
    }

    // do formatting
    final TLeft lValue = resolveFormatting().toOut(rValue);

    // is changed?
    if (mLastLeft == lValue) return;

    // update cache
    mLastLeft = lValue;

    // update View
    resolveView().set(getRuntimeView(), lValue);
  }

  /* ============================================================================================================== */

  public TLeft getRuntimeModel() {
    return (TLeft) mModel.getRuntimeInstance();
  }

  public TRight getRuntimeView() {
    return (TRight) mView.getRuntimeInstance();
  }

  /* package */ Success getOnSuccess() {
    return mOnSuccess;
  }

  /* package */ Failure getOnFailure() {
    return mOnFailure;
  }

  protected final Class<TRight> getModelType() {
    return (Class<TRight>) mModel.getInstanceType();
  }

  protected final Class<TLeft> getViewType() {
    return (Class<TLeft>) mView.getInstanceType();
  }
}
