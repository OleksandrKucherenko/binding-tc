package com.artfulbits.ui.binding;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.exceptions.ConfigurationError;
import com.artfulbits.ui.binding.exceptions.OneWayBindingError;
import com.artfulbits.ui.binding.exceptions.WrongConfigurationError;
import com.artfulbits.ui.binding.reflection.Property;
import com.artfulbits.ui.binding.toolbox.Formatter;

import org.hamcrest.CoreMatchers;

/**
 * Base class for all binding rules keeping.
 *
 * @param <TLeft>  the type of View field
 * @param <TRight> the type of Model field
 */
public class Binder<TLeft, TRight> {
  /* [ CONSTANTS ] ================================================================================================ */

  /** Empty instance. Can be  used instead of NULL. */
  public static final Binder<Void, Void> EMPTY = new Binder<>();

  /** POP operation validation passed. */
  private static final int STATUS_FAIL_GET_PUSH = 1;
  /** POP operation validation failed. */
  private static final int STATUS_FAIL_PUSH = 2;
  /** PUSH operation validation passed. */
  private static final int STATUS_FAIL_GET_POP = 4;
  /** PUSH operation validation failed. */
  private static final int STATUS_FAIL_POP = 8;
  /** Mask for filtering PUSH operation status. */
  private static final int MASK_POP = STATUS_FAIL_GET_POP | STATUS_FAIL_POP;
  /** Mask for filtering POP operation status. */
  private static final int MASK_PUSH = STATUS_FAIL_GET_PUSH | STATUS_FAIL_PUSH;

  /* ============================================================================================================== */

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
  /** Status of last push/pop operation. */
  private int mStatus;
  /** reference on internal instance that redirects calls to Model and BindingManager. */
  private Notifications mModelNotify = new Notifications() {
    @Override
    public void onChanged() {
      mModel.onChanged();
      onModelChanged();
    }
  };
  /** reference on internal instance that redirects calls to View and BindingManager. */
  private final Notifications mViewNotify = new Notifications() {
    @Override
    public void onChanged() {
      mView.onChanged();
      onViewChanged();
    }
  };

  /* ============================================================================================================== */

  /* package */ Binder() {
  }

  /* package */ Binder<TLeft, TRight> attachToManager(final BindingsManager manager) {
    mManager = manager;

    // do self registration
    mManager.getBindings().add(this);

    return this;
  }

  @Override
  public String toString() {
    return "POP : " + mView.toString() + " ==> " + mModel.toString() + " / " +
        "PUSH: " + mModel.toString() + " <== " + mView.toString();
  }

  /* ============================================================================================================== */

  /** Assign View selector to a binder. */
  public Binder<TLeft, TRight> view(@NonNull final Selector<?, TLeft> view) {
    mView = view;

    if (null != mOnView) {
      onView(mOnView);
    }

    return this;
  }

  /** Assign Model selector to a binder. */
  public Binder<TLeft, TRight> model(@NonNull final Selector<?, TRight> model) {
    mModel = model;

    if (null != mOnModel) {
      onModel(mOnModel);
    }

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

    // --> Binding Manager --> MAIN thread --> this.push()
    if (null != mView) {
      listener.binding(mView);
      listener.willNotify(mViewNotify);
    }

    return this;
  }

  /** Attach listener to Model. Listener can force data exchange. */
  public Binder<TLeft, TRight> onModel(@NonNull final Listener listener) {
    mOnModel = listener;

    // --> Binding Manager --> MAIN thread --> this.pop()
    if (null != mModel) {
      listener.binding(mModel);
      listener.willNotify(mModelNotify);
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
    if (null != mManager) {
      mManager.notifyOnViewChanged(this);
    }
  }

  /** Notify manager that binder detects model side changes. */
  protected void onModelChanged() {
    if (null != mManager) {
      mManager.notifyOnModelChanged(this);
    }
  }

  /** Notify manager that validation successfully passed. */
  protected void onValidationSuccess() {
    if (null != mManager) {
      mManager.notifyOnSuccess(this);
    } else {
      // no one listen to us, ignore them too
      if (null == mOnSuccess) return;

      mOnSuccess.onValidationSuccess(null, this);
    }
  }

  /** Notify manager that validation failed. */
  protected void onValidationFailure() {
    if (null != mManager) {
      mManager.notifyOnFailure(this);
    } else {
      // no one listen to us, ignore them too
      if (null == mOnFailure) return;

      mOnFailure.onValidationFailure(null, this);
    }
  }

  /* ============================================================================================================== */

  @NonNull
  public Property<TLeft> resolveView() {
    return mView.getProperty();
  }

  @NonNull
  public Property<TRight> resolveModel() {
    return mModel.getProperty();
  }

  /** Resolve formatting to instance that can be executed. */
  @NonNull
  public Formatting<TLeft, TRight> resolveFormatting() {
    if (null == mFormatting) {
      mFormatting = Formatter.direct();
    }

    return mFormatting;
  }

  /** Resolve validator to instance that can be executed. */
  @NonNull
  public org.hamcrest.Matcher<TRight> resolveValidation() {
    if (null == mValidation) {
      // by default we validating only data type
      return CoreMatchers.isA(resolveModel().getDataType());
    }

    return mValidation;
  }

  /**
   * Do data exchange in direction: View --> Model.
   * <p/>
   * Data flow: View --> IsChanged --> Formatter --> Validator --> Is Changed --> Model;<br/> Logic is: 'on button push
   * do model update, from higher level to lower'.
   */
  public void push() {
    // validate instance state
    if (null == mView) {
      throw new WrongConfigurationError("View part is not defined.");
    }

    if (null == mModel) {
      throw new WrongConfigurationError("Model part is not defined.");
    }

    // get value from View
    final TLeft lValue = mView.get();

    // getter is not resolved,
    if (null == resolveView().getGetterName()) {
      mStatus = (mStatus & MASK_POP) | STATUS_FAIL_PUSH | STATUS_FAIL_GET_PUSH;
      onValidationFailure();
      return;
    }

    // if no changes since last request
    if (mLastLeft == lValue) return;

    // store Value in cache
    mLastLeft = lValue;

    // formatter, with respect to ONE-WAY binding
    final TRight rValue;
    try {
      rValue = resolveFormatting().toModel(lValue);
    } catch (final OneWayBindingError ignored) {
      return;
    }

    // validation
    if (resolveValidation().matches(rValue)) {
      mStatus &= MASK_POP; // save PUSH status, reset POP status
      onValidationSuccess();
    } else {
      mStatus = (mStatus & MASK_POP) | STATUS_FAIL_PUSH;
      onValidationFailure();
      return;  // no other steps needed in pop
    }

    // if no changes since last request
    if (mLastRight == rValue) return;

    // store value in cache
    mLastRight = rValue;

    // update Model
    mModel.set(rValue);
  }

  /**
   * Do data exchange in direction: Model --> View.
   * <p/>
   * Data flow: Model --> Is Changed --> Validator --> Formatter --> Is Changed --> View.<br/> Logic is: 'on data change
   * do pop of updates from lower level to upper'.
   */
  public void pop() {
    // validate instance state
    if (null == mView) {
      throw new WrongConfigurationError("View part is not defined.");
    }

    if (null == mModel) {
      throw new WrongConfigurationError("Model part is not defined.");
    }

    // extract the value
    final TRight rValue = mModel.get();

    // getter is not resolved,
    if (null == resolveModel().getGetterName()) {
      mStatus = (mStatus & MASK_POP) | STATUS_FAIL_PUSH | STATUS_FAIL_GET_POP;
      onValidationFailure();
      return;
    }

    // is changed?
    if (mLastRight == rValue) return;

    // update value in cache
    mLastRight = rValue;

    // validation passed?
    if (resolveValidation().matches(rValue)) {
      mStatus &= MASK_PUSH; // save POP status, reset PUSH status
      onValidationSuccess();
    } else {
      mStatus = (mStatus & MASK_PUSH) | STATUS_FAIL_POP;
      onValidationFailure();
      return; // no other steps needed in push
    }

    // do formatting with respect to ONE-WAY binding
    final TLeft lValue;
    try {
      lValue = resolveFormatting().toView(rValue);
    } catch (final OneWayBindingError ignored) {
      return;
    }

    // is changed?
    if (mLastLeft == lValue) return;

    // update cache
    mLastLeft = lValue;

    // update View
    mView.set(lValue);
  }

  /** Validate instance configuration. */
  public void resolve() throws ConfigurationError {
    if (null == mView) {
      throw new WrongConfigurationError("View part is not defined.");
    }

    if (null == mModel) {
      throw new WrongConfigurationError("Model part is not defined.");
    }

    Throwable exModel = null, exView = null;

    try {
      mModel.resolve();
    } catch (final Throwable ignored) {
      exModel = ignored;
    }

    try {
      mView.resolve();
    } catch (final Throwable ignored) {
      exView = ignored;
    }

    // merge exceptions, if we have more than one
    if (null != exModel || null != exView) {
      if (null != exModel && null != exView) {
        throw new ConfigurationError("Model and View has wrong bindings.", exModel, exView);
      } else if (null == exModel) {
        throw new WrongConfigurationError("View issue", exView);
      } else {
        throw new WrongConfigurationError("Model issue", exModel);
      }
    }
  }

  /** Destroy all internal associations, cleanup. Optional. */
  public void destroy() {
    if (null != mOnModel) {
      mOnModel.detach(mModelNotify);
      mOnModel = null;
    }

    if (null != mOnView) {
      mOnView.detach(mViewNotify);
      mOnView = null;
    }

    if (null != mManager) {
      mManager.getBindings().remove(this);
      mManager = null;
    }
  }

  /* ============================================================================================================== */

  /** Is pop operation validation passed? */
  public boolean isPopOk() {
    return (mStatus & MASK_PUSH) == 0;
  }

  /** Is push operation validation passed? */
  public boolean isPushOk() {
    return (mStatus & MASK_POP) == 0;
  }

  /** Get reference on model instance. */
  public <T> T getRuntimeModel() {
    return (T) mModel.getRuntimeInstance();
  }

  /** Get reference on view instance. */
  public <T> T getRuntimeView() {
    return (T) mView.getRuntimeInstance();
  }

  /** Get access to validation success listener. */
  /* package */ Success getOnSuccess() {
    return mOnSuccess;
  }

  /** Get access to validation failure listener. */
  /* package */ Failure getOnFailure() {
    return mOnFailure;
  }

  /** Get model instance reflection type information. */
  protected final Class<?> getModelType() {
    return mModel.getInstanceType();
  }

  /** Get view instance reflection type information. */
  protected final Class<?> getViewType() {
    return mView.getInstanceType();
  }
}
