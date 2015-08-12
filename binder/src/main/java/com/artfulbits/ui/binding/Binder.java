package com.artfulbits.ui.binding;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.artfulbits.ui.binding.exceptions.ConfigurationError;
import com.artfulbits.ui.binding.exceptions.OneWayBindingError;
import com.artfulbits.ui.binding.exceptions.WrongConfigurationError;
import com.artfulbits.ui.binding.reflection.Property;
import com.artfulbits.ui.binding.toolbox.Molds;
import com.artfulbits.ui.binding.toolbox.Ridges;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

  /** PUSH and POP state flags. */
  private interface Flags {
    /** POP operation validation passed. */
    int STATUS_FAIL_GET_PUSH = 1;
    /** POP operation validation failed. */
    int STATUS_FAIL_PUSH = 2;
    /** Mask for filtering POP operation status. */
    int MASK_PUSH = STATUS_FAIL_GET_PUSH | STATUS_FAIL_PUSH;
    /** PUSH operation validation passed. */
    int STATUS_FAIL_GET_POP = 4;
    /** PUSH operation validation failed. */
    int STATUS_FAIL_POP = 8;
    /** Mask for filtering PUSH operation status. */
    int MASK_POP = STATUS_FAIL_GET_POP | STATUS_FAIL_POP;
  }

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
  /** Value used in last evaluated/extracted/exchange operation. Model side. */
  private Ridge<TRight> mRidge;
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
  /** Tags associated with current binder. */
  private Map<Integer, Object> mTags;

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
    return String.format(Locale.US,
        " PUSH: %s ==> %s\n  POP: %s <== %s\nRIDGE: %s",
        mView.toGetterString(), mModel.toSetterString(),
        mView.toSetterString(), mModel.toGetterString(),
        mRidge);
  }

  /* ============================================================================================================== */

  /** Assign View selector to a binder. */
  @NonNull
  public Binder<TLeft, TRight> view(@NonNull final Selector<?, TLeft> view) {
    mView = view;

    if (null != mOnView) {
      onView(mOnView);
    }

    return this;
  }

  /** Assign Model selector to a binder. */
  @NonNull
  public Binder<TLeft, TRight> model(@NonNull final Selector<?, TRight> model) {
    mModel = model;

    if (null != mOnModel) {
      onModel(mOnModel);
    }

    return this;
  }

  /** Attach to binder a formatting provider. */
  @NonNull
  public Binder<TLeft, TRight> format(@NonNull final Formatting<TLeft, TRight> formatting) {
    mFormatting = formatting;

    return this;
  }

  /** Attach to binder a validation expression. */
  @NonNull
  public Binder<TLeft, TRight> validate(@NonNull final org.hamcrest.Matcher<TRight> validator) {
    mValidation = validator;

    return this;
  }

  /** Set new instance of ridge strategy for binder. */
  @NonNull
  protected Binder<TLeft, TRight> ridge(@NonNull final Ridge<TRight> ridge) {
    mRidge = ridge;

    return this;
  }

  /** Attach listener to View. Listener can force data exchange. */
  @NonNull
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
  @NonNull
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
  @NonNull
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
  @NonNull
  public Binder<TLeft, TRight> onFailure(@NonNull final Failure failure) {
    mOnFailure = failure;

    return this;
  }

  /**
   * Get tag assigned to the Binder.
   *
   * @param id unique identifier of the tag.
   */
  @Nullable
  public Object getTag(@IdRes final int id) {
    if (null == mTags) return null;

    return mTags.get(id);
  }

  /**
   * Set tag with specified ID to binder.
   *
   * @param id    unique identifier of the tag
   * @param value tag value.
   */
  @NonNull
  public Binder<TLeft, TRight> setTag(@IdRes final int id, @NonNull final Object value) {
    if (null == mTags) {
      mTags = new HashMap<>();
    }

    mTags.put(id, value);

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
  protected Property<TLeft> resolveView() {
    return mView.getProperty();
  }

  @NonNull
  protected Property<TRight> resolveModel() {
    return mModel.getProperty();
  }

  /** Resolve formatting to instance that can be executed. */
  @NonNull
  protected Formatting<TLeft, TRight> resolveFormatting() {
    if (null == mFormatting) {
      mFormatting = Molds.direct();
    }

    return mFormatting;
  }

  /** Resolve validator to instance that can be executed. */
  @NonNull
  protected org.hamcrest.Matcher<TRight> resolveValidation() {
    // by default we do not validating, excepted anything
    if (null == mValidation) {
      return (Matcher<TRight>) CoreMatchers.is(CoreMatchers.anything());
    }

    return mValidation;
  }

  /** Create instance of ridge for binders. */
  @NonNull
  protected Ridge<TRight> resolveRidge() {
    if (null == mRidge) {
      mRidge = Ridges.simplest();
    }

    return mRidge;
  }

  /**
   * Do data exchange in direction: View --> Model.
   * <p/>
   * Data flow: View --> IsChanged --> Formatter --> Validator --> Ridge --> Model;<br/> Logic is: 'on button push
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
      mStatus = (mStatus & Flags.MASK_POP) | Flags.STATUS_FAIL_PUSH | Flags.STATUS_FAIL_GET_PUSH;
      onValidationFailure();
      return;
    }

    // formatter, with respect to ONE-WAY binding
    final TRight rValue;
    try {
      rValue = resolveFormatting().toModel(lValue);
    } catch (final OneWayBindingError ignored) {
      return;
    }

    // validation
    if (resolveValidation().matches(rValue)) {
      mStatus &= Flags.MASK_POP; // save PUSH status, reset POP status
      onValidationSuccess();
    } else {
      mStatus = (mStatus & Flags.MASK_POP) | Flags.STATUS_FAIL_PUSH;
      onValidationFailure();
      return;  // no other steps needed in pop
    }

    // if no changes since last request
    if (resolveRidge().isChanged(rValue)) {
      final TRight toSet = resolveRidge().clone(rValue);

      // update Model
      mModel.set(toSet);
    }
  }

  /**
   * Do data exchange in direction: Model --> View.
   * <p/>
   * Data flow: Model --> Ridge --> Validator --> Formatter --> Is Changed --> View.<br/> Logic is: 'on data change
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
      mStatus = (mStatus & Flags.MASK_POP) | Flags.STATUS_FAIL_PUSH | Flags.STATUS_FAIL_GET_POP;
      onValidationFailure();
      return;
    }

    // is no changed?
    if (!resolveRidge().isChanged(rValue)) return;

    // update value in cache only for model
    final TRight rClone = resolveRidge().clone(rValue);

    // validation passed?
    if (resolveValidation().matches(rClone)) {
      mStatus &= Flags.MASK_PUSH; // save POP status, reset PUSH status
      onValidationSuccess();
    } else {
      mStatus = (mStatus & Flags.MASK_PUSH) | Flags.STATUS_FAIL_POP;
      onValidationFailure();
      return; // no other steps needed in push
    }

    // do formatting with respect to ONE-WAY binding
    final TLeft lValue;
    try {
      lValue = resolveFormatting().toView(rClone);
    } catch (final OneWayBindingError ignored) {
      return;
    }

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
    return (mStatus & Flags.MASK_POP) == 0;
  }

  /** Is push operation validation passed? */
  public boolean isPushOk() {
    return (mStatus & Flags.MASK_PUSH) == 0;
  }

  /** Get reference on model instance. */
  @SuppressWarnings("unchecked")
  public <T> T getRuntimeModel() {
    return (T) mModel.getRuntimeInstance();
  }

  /** Get reference on view instance. */
  @SuppressWarnings("unchecked")
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

  /* ============================================================================================================== */

  /** Implement this interface if you want to implement advanced ridge strategy. */
  public interface Ridge<T> {
    /** True - value updated, otherwise nothing to process. */
    boolean isChanged(final T value);

    /** Update value in cache. */
    T clone(final T value);
  }
}
