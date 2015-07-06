package com.artfulbits.ui.binding;

import android.app.Activity;
import android.app.Fragment;
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
  private final static boolean DO_POP = true;
  /** Associated with PUSH action constant. */
  private final static boolean DO_PUSH = false;

  /** Weak references on listeners. */
  private final Set<Lifecycle> mListeners = new WeakHashMap<Lifecycle, Lifecycle>().keySet();
  /** Facade For all types of the Views. */
  private final Selector<?, View> mFacade;
  /** Collection of all defined binding rules. */
  private final List<Binder> mRules = new LinkedList<Binder>();
  /** Freeze counter. */
  private final AtomicInteger mFreezeCounter = new AtomicInteger(0);
  /** Set of binding exchange transactions. We store order, binder and direction (boolean: true - pop, false - push). */
  private final List<Pair<Binder, Boolean>> mPending = new ArrayList<>();

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
        for (Pair<Binder, Boolean> p : mPending) {
          if (/* DO_POP == */ p.second) {
            pop(p.first);
          } else {
            push(p.first);
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

	/* [ NESTED DECLARATIONS ] ====================================================================================== */

  /** Consolidate API for all types of Views. */
  private static final class ViewFacade {
    /** Reference on root element of binding. */
    private final Activity mRootActivity;

    private final View mRootView;

    private final BaseAdapter mRootAdapter;

    private final android.support.v4.app.Fragment mSupportFragment;

    private final Fragment mFragment;

    public ViewFacade(final android.support.v4.app.Fragment fragment) {
      mRootActivity = null;
      mSupportFragment = fragment;
      mFragment = null;
      mRootView = null;
      mRootAdapter = null;
    }

    public ViewFacade(final Fragment fragment) {
      mRootActivity = null;
      mSupportFragment = null;
      mFragment = fragment;
      mRootView = null;
      mRootAdapter = null;
    }

    public ViewFacade(final View parent) {
      mRootActivity = null;
      mSupportFragment = null;
      mFragment = null;
      mRootView = parent;
      mRootAdapter = null;
    }

    public ViewFacade(final Activity parent) {
      mRootActivity = parent;
      mSupportFragment = null;
      mFragment = null;
      mRootView = null;
      mRootAdapter = null;
    }

    public ViewFacade(final BaseAdapter adapter) {
      mRootActivity = null;
      mSupportFragment = null;
      mFragment = null;
      mRootView = null;
      mRootAdapter = adapter;
    }
  }

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
