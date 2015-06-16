package com.artfulbits.ui.binding;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.BaseAdapter;

import com.artfulbits.ui.binding.reflection.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manager class responsible for controlling integration of the binding library into corresponding fragment or activity.
 * It controls aspects:<br/> <ul> <li>context instance extracting;</li> <li>Binding defining and configuring;</li>
 * <li></li> </ul>
 */
public class BindingManager {
  /* [ CONSTANTS AND MEMBERS ] ==================================================================================== */

  /** Weak references on listeners. */
  private final WeakHashMap<LifecycleCallback, LifecycleCallback> mListeners = new WeakHashMap<LifecycleCallback,
      LifecycleCallback>();
  /** Facade For all types of the Views. */
  private final ViewFacade mFacade;
  /** Collection of all defined binding rules. */
  private final List<Binder> mRules = new LinkedList<Binder>();
  /** Freeze counter. */
  private final AtomicInteger mFreezeCounter = new AtomicInteger(0);

  /* [ CONSTRUCTORS ] ============================================================================================= */

  public BindingManager(final Activity parent) {
    mFacade = new ViewFacade(parent);
  }

  public BindingManager(final Fragment parent) {
    mFacade = new ViewFacade(parent);
  }

  public BindingManager(final android.support.v4.app.Fragment parent) {
    mFacade = new ViewFacade(parent);
  }

  public BindingManager(final View parent) {
    mFacade = new ViewFacade(parent);
  }

  public BindingManager(final BaseAdapter adapter) {
    mFacade = new ViewFacade(adapter);
  }

  /* [ BINDING RULES DEFINING ] =================================================================================== */

  public List<Binder> getBindings() {
    return mRules;
  }

  public List<Binder> getBindingsByInstance(final Object instance) {
    final List<Binder> result = new LinkedList<Binder>();

    // TODO: do the search

    return result;
  }

  public List<Binder> getSuccessBindings() {
    final List<Binder> result = new LinkedList<Binder>();

    // TODO: do the search

    return result;
  }

  public List<Binder> getFailedBindings() {
    final List<Binder> result = new LinkedList<Binder>();

    // TODO: do the search

    return result;
  }

  public <TLeft, TRight> Binder<TLeft, TRight> bind() {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result.attachToManager(this);
  }

  public <TLeft, TRight> Binder<TLeft, TRight> bind(final Selector<?, Property<TLeft>> view,
                                                    final Selector<?, Property<TRight>> model) {
    final Binder<TLeft, TRight> result = new Binder<>();

    return result
        .view(view)
        .model(model)
        .attachToManager(this);
  }

  /* [ LIFECYCLE ] ================================================================================================ */

  public BindingManager register(final LifecycleCallback listener) {
    if (null != listener) {
      mListeners.put(listener, listener);
    }

    return this;
  }

  public BindingManager unregister(final LifecycleCallback listener) {
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
  public BindingManager pushByInstance(final Object instance) {
    for (final Binder bind : getBindingsByInstance(instance)) {
      push(bind);
    }

    return this;
  }

  /**
   * Force model instance update by value from view.
   *
   * @param binder binding rule.
   */
  public BindingManager push(final Binder binder) {
    // TODO: implement me
    return this;
  }

  /**
   * Force views updates that are bind to the provided model instance.
   *
   * @param instance the instance of model
   */
  public BindingManager popByInstance(final Object instance) {
    for (final Binder bind : getBindingsByInstance(instance)) {
      pop(bind);
    }

    return this;
  }

  /**
   * Force views updates that are bind to the provided model instance.
   *
   * @param binder binding rule.
   */
  public BindingManager pop(final Binder binder) {
    // TODO: implement me
    return this;
  }

  /** Stop triggering of all data push/pop operations. */
  public BindingManager freeze() {
    mFreezeCounter.incrementAndGet();
    return this;
  }

  /** Recover triggering of all data push/pop operations. */
  public BindingManager unfreeze() {
    if (0 >= mFreezeCounter.decrementAndGet()) {
      // TODO: unfreeze triggering

      mFreezeCounter.set(0);
    }

    return this;
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

    public ViewFacade(android.support.v4.app.Fragment fragment) {
      mRootActivity = null;
      mSupportFragment = fragment;
      mFragment = null;
      mRootView = null;
      mRootAdapter = null;
    }

    public ViewFacade(Fragment fragment) {
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
   * Lifecycle extending callback. Implement it if you want to enhance original lifecycle by new state, during
   * which binding operation is the most suitable.
   */
  public interface LifecycleCallback {
    void onCreateBinding(final BindingManager bm);

    void onValidationResult(final BindingManager bm, final boolean success);
  }
}
