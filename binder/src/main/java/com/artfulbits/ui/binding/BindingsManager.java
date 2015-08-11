package com.artfulbits.ui.binding;

import android.app.Activity;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Pair;
import android.view.View;
import android.widget.BaseAdapter;

import com.artfulbits.ui.binding.exceptions.WrongConfigurationError;
import com.artfulbits.ui.binding.toolbox.Views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manager class responsible for controlling integration of the binding library into corresponding fragment or activity.
 * It controls aspects:<br/> <ul> <li>context instance extracting;</li> <li>Binding defining and configuring;</li>
 * <li></li> </ul>
 */
@SuppressWarnings("unused")
public class BindingsManager {
  /* [ CONSTANTS ] =============================================================================================== */

  /** Associated with POP action constant. */
  private final static int DO_POP = 1;
  /** Associated with PUSH action constant. */
  private final static int DO_PUSH = 0;

  /** Internal messages. */
  private interface Messages {
    /** Detected View change. */
    int ON_VIEW_CHANGED = 1;
    /** Detected Model change. */
    int ON_MODEL_CHANGED = 2;
    /** Request unfreeze operation execution in UI thread. */
    int UNFREEZE = 4;
    /** Request validation success report delivery in UI thread. */
    int SUCCESS = 8;
    /** Request validation failure report delivery in UI thread. */
    int FAILURE = 16;
    /** Request validation report delivery in UI thread. */
    int LIFECYCLE_VALIDATION = 32;
    /** Do create binding. */
    int LIFECYCLE_BINDING = 64;
  }

  /* [ MEMBERS ] ================================================================================================= */

  /** Weak references on lifecycle listeners. */
  private final Map<Lifecycle, Void> mListeners = new WeakHashMap<>();
  /** Facade For all types of the Views. */
  private final Selector<?, View> mFacade;
  /** Collection of all defined binding rules. */
  private final List<Binder<?, ?>> mRules = new LinkedList<>();
  /** Freeze counter. */
  private final AtomicInteger mFreezeCounter = new AtomicInteger(0);
  /** Set of binding exchange transactions. We store order, binder and direction (boolean: true - pop, false - push). */
  private final List<Pair<Binder<?, ?>, Integer>> mPending = new ArrayList<>();
  /** Handler for forwarding processing to MAIN UI thread. */
  private final Handler mDispatcher = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(final Message msg) {
      return onHandleMessage(msg);
    }
  });

  /* [ CONSTRUCTORS ] ============================================================================================= */

  /** Create bindings manager for activity instance. */
  public BindingsManager(@NonNull final Activity activity) {
    mFacade = Views.root(activity);
  }

  /** Create bindings manager for native OS fragment. */
  public BindingsManager(@NonNull final Fragment fragment) {
    mFacade = Views.root(fragment);
  }

  /** Create bindings manager for 'support fragment'. */
  public BindingsManager(@NonNull final android.support.v4.app.Fragment fragment) {
    mFacade = Views.root(fragment);
  }

  /** Create bindings manager for view instance. */
  public BindingsManager(@NonNull final View view) {
    mFacade = Views.root(view);
  }

  /** Create bindings manager for adapter instance. */
  public BindingsManager(@NonNull final BaseAdapter adapter) {
    // TODO: implement me
    throw new AssertionError("Implement me!");
//    mFacade = Views.root(adapter);
  }

  /* [ STATIC HELPERS ] =========================================================================================== */

  public static BindingsManager newInstance(@NonNull final Activity i, final Lifecycle listener) {
    return new BindingsManager(i).register(listener);
  }

  public static BindingsManager newInstance(@NonNull final android.support.v4.app.Fragment i, final Lifecycle listener) {
    return new BindingsManager(i).register(listener);
  }

  public static BindingsManager newInstance(@NonNull final Fragment i, final Lifecycle listener) {
    return new BindingsManager(i).register(listener);
  }

  public static BindingsManager newInstance(@NonNull final View i, final Lifecycle listener) {
    return new BindingsManager(i).register(listener);
  }

  public static BindingsManager newInstance(@NonNull final BaseAdapter i, final Lifecycle listener) {
    return new BindingsManager(i).register(listener);
  }

  /* [ OVERRIDES ] ================================================================================================ */

  /** UI THREAD! processing of messages in UI thread. */
  protected boolean onHandleMessage(final Message msg) {
    final Binder<?, ?> binder = (msg.obj instanceof Binder) ? (Binder<?, ?>) msg.obj : Binder.EMPTY;

    switch (msg.what) {
      case Messages.ON_MODEL_CHANGED:
        pop(binder);
        return true;

      case Messages.ON_VIEW_CHANGED:
        push(binder);
        return true;

      case Messages.UNFREEZE:
        if (DO_POP == msg.arg1) {
          binder.pop();
        } else {
          binder.push();
        }
        return true;

      case Messages.SUCCESS:
        binder.getOnSuccess().onValidationSuccess(this, binder);
        return true;

      case Messages.FAILURE:
        binder.getOnFailure().onValidationFailure(this, binder);
        return true;

      case Messages.LIFECYCLE_BINDING:
        for (Lifecycle lf : mListeners.keySet()) {
          lf.onCreateBinding(this);
        }
        return true;

      case Messages.LIFECYCLE_VALIDATION:
        for (Lifecycle lf : mListeners.keySet()) {
          lf.onValidationResult(this, isAllValid());
        }
        return true;
    }

    return false;
  }

  /* [ BINDING RULES DEFINING ] =================================================================================== */

  /** Get list of all binding rules. */
  public List<Binder<?, ?>> getBindings() {
    return mRules;
  }

  /** Get list of binder's that interact with specified model model. */
  public List<Binder<?, ?>> getBindingsByModel(@NonNull final Object model) {
    final List<Binder<?, ?>> result = new LinkedList<>();

    for (Binder<?, ?> b : mRules) {
      if (model.equals(b.getRuntimeModel())) {
        result.add(b);
      }
    }

    return result;
  }

  /** Get list of binder's that interact with specified view view. */
  public List<Binder<?, ?>> getBindingsByView(@NonNull final Object view) {
    final List<Binder<?, ?>> result = new LinkedList<>();

    for (Binder<?, ?> b : mRules) {
      if (view.equals(b.getRuntimeView())) {
        result.add(b);
      }
    }

    return result;
  }

  /** Is all rules successfully validated? */
  public boolean isAllValid() {
    return getFailedBindings().size() == 0;
  }

  /** Get list of all successfully validated bindings. */
  public List<Binder<?, ?>> getSuccessBindings() {
    final List<Binder<?, ?>> result = new LinkedList<>();

    // if POP or PUSH operation never performed, than we assume that they are in OK state
    for (final Binder<?, ?> b : mRules) {
      if (b.isPopOk() && b.isPushOk()) {
        result.add(b);
      }
    }

    return result;
  }

  /** Get list of failed validated bindings. */
  public List<Binder<?, ?>> getFailedBindings() {
    final List<Binder<?, ?>> result = new LinkedList<>();

    // at least one operation should be in failed state
    for (final Binder<?, ?> b : mRules) {
      if (!b.isPopOk() || !b.isPushOk()) {
        result.add(b);
      }
    }

    return result;
  }

  /* [ GENERIC BINDERS ] ========================================================================================== */

  public <TLeft, TRight> Binder<TLeft, TRight> bind() {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result.attachToManager(this);
  }

  public <TLeft, TRight> Binder<TLeft, TRight> bind(@NonNull final Selector<?, TLeft> view,
                                                    @NonNull final Selector<?, TRight> model) {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result
        .view(view)
        .model(model)
        .attachToManager(this);
  }

  public Binder<String, String> texts() {
    return bind();
  }

  public Binder<Integer, Integer> integers() {
    return bind();
  }

  public Binder<Double, Double> reals() {
    return bind();
  }

  public Binder<Boolean, Boolean> bools() {
    return bind();
  }

  public Binder<String, Integer> numeric() {
    return bind();
  }

  /* [ LIFECYCLE ] ================================================================================================ */

  /** Register lifecycle extender listener. */
  public BindingsManager register(@NonNull final Lifecycle listener) {
    mListeners.put(listener, null);

    return this;
  }

  /** Unregister lifecycle extender listener. */
  public BindingsManager unregister(@NonNull final Lifecycle listener) {
    mListeners.remove(listener);

    return this;
  }

  /** Execute from: {@link Activity#onRestart()}. Execute complete restart of Binding manager lifecycle. */
  public void doRestart() {
    doDestroy();
  }

  /**
   * Execute from: {@link Activity#onStart()} or {@link android.support.v4.app.Fragment#onStart()}, {@link
   * Fragment#onStart()}.
   */
  public void doStart() {
    notifyOnCreateBinding();
  }

  /**
   * Execute from: {@link Activity#onResume()} or {@link android.support.v4.app.Fragment#onResume()}, {@link
   * Fragment#onResume()}.
   */
  public void doResume() {
    pop();
  }

  /**
   * Execute from: {@link Activity#onPause()} or {@link android.support.v4.app.Fragment#onPause()}, {@link
   * Fragment#onPause()}.
   */
  public void doPause() {
    push();
  }

  /**
   * Execute from: {@link Activity#onDestroy()} or {@link android.support.v4.app.Fragment#onDestroy()}, {@link
   * Fragment#onDestroy()}.
   */
  public void doDestroy() {
    for (int i = mRules.size() - 1; i >= 0; i--) {
      final Binder<?, ?> b = mRules.get(i);

      // self de-registration from Binding Manager happens
      b.destroy();
    }
  }

  /* package */ void notifyOnCreateBinding() {
    mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.LIFECYCLE_BINDING));
  }

  /* package */ void notifyOnViewChanged(@NonNull final Binder<?, ?> binder) {
    mDispatcher.removeMessages(Messages.ON_VIEW_CHANGED);
    mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.ON_VIEW_CHANGED, binder));
  }

  /* package */ void notifyOnModelChanged(@NonNull final Binder<?, ?> binder) {
    mDispatcher.removeMessages(Messages.ON_MODEL_CHANGED);
    mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.ON_MODEL_CHANGED, binder));
  }

  /* package */ void notifyOnValidation(@NonNull final Binder<?, ?> binder) {
    mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.LIFECYCLE_VALIDATION));
  }

  /* package */ void notifyOnSuccess(@NonNull final Binder<?, ?> binder) {
    if (null != binder.getOnSuccess()) {
      mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.SUCCESS, binder));
    }
  }

  /* package */ void notifyOnFailure(@NonNull final Binder<?, ?> binder) {
    if (null != binder.getOnFailure()) {
      mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.FAILURE, binder));
    }
  }

	/* [ PUSH AND POP ] ============================================================================================= */

  /**
   * Evaluate all bindings. Perform on each PUSH. VIEW data delivered to MODEL.<br/> Keep in mind that this is
   * <b>ASYNC</b> operation. Results of binding you will receive in specially designed callbacks/listeners: {@link
   * BindingsManager.Lifecycle}, {@link Success} or {@link Failure}.
   *
   * @return this instance.
   */
  public BindingsManager push() {
    for (Binder<?, ?> b : mRules) {
      push(b);
    }

    return this;
  }

  /**
   * Force model modelInstance update by values from view's.
   *
   * @param modelInstance the modelInstance of model
   * @return this instance.
   */
  public BindingsManager pushTo(@NonNull final Object modelInstance) {
    for (final Binder<?, ?> bind : getBindingsByModel(modelInstance)) {
      push(bind);
    }

    return this;
  }

  /**
   * Force model instance update by value from view with respect to 'Freeze mode'.
   *
   * @param binder binding rule.
   * @return this instance.
   */
  public BindingsManager push(@NonNull final Binder<?, ?> binder) {
    if (isFrozen()) {
      mPending.add(new Pair<Binder<?, ?>, Integer>(binder, DO_PUSH));
    } else {
      binder.push();
    }

    return this;
  }

  /**
   * Force views updates that are bind to the provided model modelInstance.
   *
   * @param modelInstance the modelInstance of model
   * @return this instance.
   */
  public BindingsManager popByModel(@NonNull final Object modelInstance) {
    for (final Binder<?, ?> bind : getBindingsByModel(modelInstance)) {
      pop(bind);
    }

    return this;
  }

  /** Pop data from Model to view instance. */
  public BindingsManager popTo(@NonNull final Object viewInstance) {
    for (Binder<?, ?> b : getBindingsByView(viewInstance)) {
      pop(b);
    }

    return this;
  }

  /**
   * Evaluate all bindings. Perform on each POP. MODEL data delivered to VIEW.<br/> Keep in mind that this is
   * <b>ASYNC</b> operation. Results of binding you will receive in specially designed callbacks/listeners: {@link
   * BindingsManager.Lifecycle}, {@link Success} or {@link Failure}.
   *
   * @return this instance.
   */
  public BindingsManager pop() {
    for (Binder<?, ?> b : mRules) {
      pop(b);
    }

    return this;
  }

  /**
   * Force views updates that are bind to the provided model instance with respect to 'Freeze mode'.
   *
   * @param binder binding rule.
   * @return this instance.
   */
  public BindingsManager pop(@NonNull final Binder<?, ?> binder) {
    if (isFrozen()) {
      mPending.add(new Pair<Binder<?, ?>, Integer>(binder, DO_POP));
    } else {
      binder.pop();
    }

    return this;
  }

  /**
   * Are we in 'freeze mode' state or not?
   *
   * @return {@code true} - if we frozen, otherwise {@code false}.
   */
  public boolean isFrozen() {
    return mFreezeCounter.get() > 0;
  }

  /** Get number of pending POP/PUSH actions. */
  public int getPendingQueueSize() {
    return mPending.size();
  }

  /**
   * Stop triggering of all data push/pop operations.
   *
   * @return this instance.
   */
  public BindingsManager freeze() {
    mFreezeCounter.incrementAndGet();
    return this;
  }

  /**
   * Recover triggering of all data push/pop operations.
   *
   * @return this instance.
   */
  public BindingsManager unfreeze() {
    if (0 >= mFreezeCounter.decrementAndGet()) {
      mFreezeCounter.set(0);

      // execute pending data exchange requests
      if (!mPending.isEmpty()) {
        for (Pair<Binder<?, ?>, Integer> p : mPending) {
          // update is possible only in UI thread
          if (View.class.isAssignableFrom(p.first.getViewType())) {
            mDispatcher.sendMessage(mDispatcher.obtainMessage(Messages.UNFREEZE, p.second, -1, p.first));
            continue;
          }

          // do data exchange
          if (DO_POP == p.second) {
            p.first.pop();
          } else {
            p.first.push();
          }
        }

        mPending.clear();
      }
    }

    return this;
  }

  /**
   * Method force binding between data model and view without data exchange. This is useful for binding verification on
   * initial phase. It allows to check that all fields in data model and view exists and can be associated.
   *
   * @throws WrongConfigurationError - found mismatch. Raised on first found mismatch.
   */
  @UiThread
  public void associate() throws WrongConfigurationError {
    for (Binder<?, ?> binder : mRules) {
      binder.resolve();
    }
  }

	/* [ NESTED DECLARATIONS ] ====================================================================================== */

  /**
   * Lifecycle extending callback. Implement it if you want to enhance original lifecycle by new state, during which
   * binding operation is the most suitable.
   */
  public interface Lifecycle {
    /** Raised on moment when is the best to create bindings. Execute in MAIN thread. */
    void onCreateBinding(final BindingsManager bm);

    /** Raised on moment of validation. Executed in MAIN thread. */
    void onValidationResult(final BindingsManager bm, final boolean success);
  }
}
