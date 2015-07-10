package com.artfulbits.ui.binding;

import android.app.Activity;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.BaseAdapter;

import com.artfulbits.ui.binding.toolbox.Views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manager class responsible for controlling integration of the binding library into corresponding fragment or activity.
 * It controls aspects:<br/> <ul> <li>context instance extracting;</li> <li>Binding defining and configuring;</li>
 * <li></li> </ul>
 */
@SuppressWarnings("unused")
public class BindingsManager {
  /* [ CONSTANTS AND MEMBERS ] ==================================================================================== */

  /** Associated with POP action constant. */
  private final static int DO_POP = 1;
  /** Associated with PUSH action constant. */
  private final static int DO_PUSH = 0;
  /** Detected View change. */
  private final static int MSG_ON_VIEW_CHANGED = 1;
  /** Detected Model change. */
  private final static int MSG_ON_MODEL_CHANGED = 2;
  /** Request unfreeze operation execution in UI thread. */
  private final static int MSG_UNFREEZE = 3;

  /** Weak references on lifecycle listeners. */
  private final Set<Lifecycle> mListeners = new WeakHashMap<Lifecycle, Void>().keySet();
  /** Facade For all types of the Views. */
  private final Selector<?, View> mFacade;
  /** Collection of all defined binding rules. */
  private final List<Binder> mRules = new LinkedList<Binder>();
  /** Freeze counter. */
  private final AtomicInteger mFreezeCounter = new AtomicInteger(0);
  /** Set of binding exchange transactions. We store order, binder and direction (boolean: true - pop, false - push). */
  private final List<Pair<Binder, Integer>> mPending = new ArrayList<>();
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

    switch (msg.what) {
      case MSG_ON_MODEL_CHANGED:
        pop((Binder) msg.obj);
        return true;

      case MSG_ON_VIEW_CHANGED:
        push((Binder) msg.obj);
        return true;

      case MSG_UNFREEZE:
        if (DO_POP == msg.arg1) {
          ((Binder) msg.obj).pop();
        } else {
          ((Binder) msg.obj).push();
        }
        return true;
    }

    return false;
  }

  /* [ BINDING RULES DEFINING ] =================================================================================== */

  public List<Binder> getBindings() {
    return mRules;
  }

  /** Get list of binder's that interact with specified model model. */
  public List<Binder> getBindingsByModel(@NonNull final Object model) {
    final List<Binder> result = new LinkedList<>();

    for (Binder<?, ?> b : mRules) {
      if (model.equals(b.getRuntimeModel())) {
        result.add(b);
      }
    }

    return result;
  }

  /** Get list of binder's that interact with specified view view. */
  public List<Binder> getBindingsByView(@NonNull final Object view) {
    final List<Binder> result = new LinkedList<>();

    for (Binder<?, ?> b : mRules) {
      if (view.equals(b.getRuntimeView())) {
        result.add(b);
      }
    }

    return result;
  }

  public List<Binder> getSuccessBindings() {
    final List<Binder> result = new LinkedList<>();

    // TODO: do the search

    return result;
  }

  public List<Binder> getFailedBindings() {
    final List<Binder> result = new LinkedList<>();

    // TODO: do the search

    return result;
  }

  /* [ GENERIC BINDERS ] ========================================================================================== */

  public <TLeft, TRight> Binder<TLeft, TRight> bind() {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result.attachToManager(this);
  }

  public <TLeft, TRight> Binder<TLeft, TRight> bind(final Selector<?, TLeft> view,
                                                    final Selector<?, TRight> model) {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result
        .view(view)
        .model(model)
        .attachToManager(this);
  }

  /* [ COMMON BINDERS ] =========================================================================================== */

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
  public BindingsManager register(@Nullable final Lifecycle listener) {
    if (null != listener) {
      mListeners.add(listener);
    }

    return this;
  }

  /** Unregister lifecycle extender listener. */
  public BindingsManager unregister(final Lifecycle listener) {
    if (null != listener) {
      mListeners.remove(listener);
    }

    return this;
  }

	/* [ PUSH AND POP ] ============================================================================================= */

  /**
   * Force model instance update by values from view's.
   *
   * @param instance the instance of model
   */
  public BindingsManager pushByModel(@NonNull final Object instance) {
    for (final Binder bind : getBindingsByModel(instance)) {
      push(bind);
    }

    return this;
  }

  /**
   * Force model instance update by value from view with respect to 'Freeze mode'.
   *
   * @param binder binding rule.
   */
  public BindingsManager push(@NonNull final Binder binder) {
    if (isFrozen()) {
      mPending.add(new Pair<>(binder, DO_PUSH));
    } else {
      binder.push();
    }

    return this;
  }

  /**
   * Force views updates that are bind to the provided model instance.
   *
   * @param instance the instance of model
   */
  public BindingsManager popByModel(@NonNull final Object instance) {
    for (final Binder bind : getBindingsByModel(instance)) {
      pop(bind);
    }

    return this;
  }

  /**
   * Force views updates that are bind to the provided model instance with respect to 'Freeze mode'.
   *
   * @param binder binding rule.
   */
  public BindingsManager pop(@NonNull final Binder binder) {
    if (isFrozen()) {
      mPending.add(new Pair<>(binder, DO_POP));
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

  /** Stop triggering of all data push/pop operations. */
  public BindingsManager freeze() {
    mFreezeCounter.incrementAndGet();
    return this;
  }

  /** Recover triggering of all data push/pop operations. */
  public BindingsManager unfreeze() {
    if (0 >= mFreezeCounter.decrementAndGet()) {
      mFreezeCounter.set(0);

      // execute pending data exchange requests
      if (!mPending.isEmpty()) {
        for (Pair<Binder, Integer> p : mPending) {
          // update is possible only in UI thread
          if (View.class.isAssignableFrom(p.first.getViewType())) {
            mDispatcher.sendMessage(mDispatcher.obtainMessage(MSG_UNFREEZE, p.second, -1, p.first));
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
   * @throws WrongConfigurationException - found mismatch.
   */
  public void associate() throws WrongConfigurationException {
    // TODO: can be executed only from MAIN thread!

    // TODO: force binding manager evaluate binding for each property
  }

  /* package */ void notifyOnViewChanged(@NonNull final Binder binder) {
    mDispatcher.removeMessages(MSG_ON_VIEW_CHANGED);
    mDispatcher.sendMessage(mDispatcher.obtainMessage(MSG_ON_VIEW_CHANGED, binder));
  }

  /* package */ void notifyOnModelChanged(@NonNull final Binder binder) {
    mDispatcher.removeMessages(MSG_ON_MODEL_CHANGED);
    mDispatcher.sendMessage(mDispatcher.obtainMessage(MSG_ON_MODEL_CHANGED, binder));
  }

  /* package */ void notifyOnValidation(@NonNull final Binder binder) {
    // TODO: notify lifecycle on validation success or failure
  }

	/* [ NESTED DECLARATIONS ] ====================================================================================== */

  /**
   * Lifecycle extending callback. Implement it if you want to enhance original lifecycle by new state, during which
   * binding operation is the most suitable.
   */
  public interface Lifecycle {
    /** Raised on moment when is the best to create bindings. */
    void onCreateBinding(final BindingsManager bm);

    /** Raised on moment of validation. */
    void onValidationResult(final BindingsManager bm, final boolean success);
  }
}
