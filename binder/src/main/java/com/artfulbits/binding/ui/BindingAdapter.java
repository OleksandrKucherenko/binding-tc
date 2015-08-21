package com.artfulbits.binding.ui;

import android.database.DataSetObserver;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.artfulbits.binding.BindingsManager;
import com.artfulbits.binding.Selector;
import com.artfulbits.binding.toolbox.Adapters;

import java.lang.ref.WeakReference;

/**
 * Wrapper over instance of Adapter. Provides access to Binding Manager instance and lifecycle.
 * <p/>
 * Notes for inheritors:<br/>
 * 1) mostly all Adapter methods are 'final'. That should force keeping of adapter logic out of the class.
 * this is a wrapper that provides Binding capabilities;<br/>
 * 2) {@link #onCreateBinding(BindingsManager, Selector, Selector)} is a method for overriding;<br/>
 * 3) {@link #onValidationResult(BindingsManager, boolean)} - never called! but you can include it
 * into own lifecycle by direct call;<br/>
 */
@SuppressWarnings("unused")
public class BindingAdapter implements Adapter, BindingsManager.Lifecycle {

  /* [ CONSTANTS ] ================================================================================================= */

  /** Main thread ID. */
  private static final int MAIN_THREAD_ID = 1;

  /* [ MEMBERS ] =================================================================================================== */

  /** Wrapped adapter. */
  private final Adapter mAdapter;
  /** Instance of binding manager. */
  private final BindingsManager mBindingsManager;
  /** Reference on lifecycle callback. */
  private final Lifecycle mLifecycle;
  /** keep adapter context per thread. Thread ID - to - Context instance. */
  private final BindingContext mContext = new BindingContext();

	/* [ CONSTRUCTORS ] ============================================================================================== */

  /** Hidden constructor. Expected static messages use and inheritance only. */
  public BindingAdapter(@NonNull final Adapter adapter) {
    this(adapter, null);
  }

  /** Hidden constructor. Expected static messages use and inheritance only. */
  public BindingAdapter(@NonNull final Adapter adapter,
                        @Nullable final Lifecycle lifecycle) {
    mAdapter = adapter;
    mLifecycle = lifecycle;
    mBindingsManager = BindingsManager.newInstance(this, this);

    // force binding rules creation
    getBindingsManager().doStart(this); // --> onCreateBinding(...)
  }

  /* [ GETTER / SETTER METHODS ] =================================================================================== */

  /**
   * Resolve adapter context with respect to multi-threading. In case if
   * not existing context for current thread method will return reference
   * on MAIN thread context.
   */
  @NonNull
  protected final BindingContext getThreadContext() {
    return mContext;
  }

  /** Get reference on Binding manager instance. */
  public final BindingsManager getBindingsManager() {
    return mBindingsManager;
  }

  /** Get reference on inner wrapped adapter. */
  public final Adapter getInnerAdapter() {
    return mAdapter;
  }

  /** Get position of under the processing item. */
  public final int getBindingPosition() {
    return getThreadContext().mPosition;
  }

  /** Assign position to the processing context. */
  protected final BindingAdapter setBindingPosition(final int position) {
    getThreadContext().mPosition = position;

    return this;
  }

  /** Get reference on under the processing item. */
  @Nullable
  public final Object getBindingItem() {
    if (null == getThreadContext().mBindingItem) return null;

    return getThreadContext().mBindingItem.get();
  }

  /** Assign item to the processing context. */
  protected final BindingAdapter setBindingItem(final Object item) {
    getThreadContext().mBindingItem = new WeakReference<>(item);

    return this;
  }

  /** Get reference on under the processing View created/reused for under the processing item. */
  @Nullable
  public final View getBindingView() {
    if (null == getThreadContext().mBindingView) return null;

    return getThreadContext().mBindingView.get();
  }

  /** Assign view to the processing context. */
  protected final BindingAdapter setBindingView(final View view) {
    getThreadContext().mBindingView = new WeakReference<>(view);

    return this;
  }

	/* [ METHODS ] =================================================================================================== */

  /** {@inheritDoc} */
  @CallSuper
  public void onCreateBinding(@NonNull final BindingsManager bm,
                              @NonNull final Selector<?, View> getView,
                              @NonNull final Selector<?, Object> getModel) {
    // forward call to runtime extender of the lifecycle
    if (null != mLifecycle) {
      mLifecycle.onCreateBinding(bm, getView, getModel);
    }
  }

  /** {@inheritDoc} */
  @Override
  public final void registerDataSetObserver(final DataSetObserver observer) {
    mAdapter.registerDataSetObserver(observer);
  }

  /** {@inheritDoc} */
  @Override
  public final void unregisterDataSetObserver(final DataSetObserver observer) {
    mAdapter.unregisterDataSetObserver(observer);
  }

	/* [ Interface Adapter ] ========================================================================================= */

  /** {@inheritDoc} */
  @Override
  public final int getCount() {
    return mAdapter.getCount();
  }

  /** {@inheritDoc} */
  @Override
  public final Object getItem(final int position) {
    return mAdapter.getItem(position);
  }

  /** {@inheritDoc} */
  @Override
  public final long getItemId(final int position) {
    return mAdapter.getItemId(position);
  }

  /** {@inheritDoc} */
  @Override
  public final boolean hasStableIds() {
    return mAdapter.hasStableIds();
  }

  /** Implemented binding of Item to View. */
  @Override
  public final View getView(final int position, final View convertView, final ViewGroup parent) {
    // request wrapped adapter to create/reuse a view for us
    final View v = mAdapter.getView(position, convertView, parent);

    // assign binding dependencies, define selectors context
    this.setBindingPosition(position)
        .setBindingItem(getItem(position))
        .setBindingView(v);

    // apply data exchange MODEL --> VIEW (POP), time to do the magic
    getBindingsManager().pop();

    // should we run it for all rules???
    // can we limit/filter POP by Model instance or View instance ???

    return v;
  }

  /** {@inheritDoc} */
  @Override
  public final int getItemViewType(final int position) {
    return mAdapter.getItemViewType(position);
  }

  /** {@inheritDoc} */
  @Override
  public final int getViewTypeCount() {
    return mAdapter.getViewTypeCount();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean isEmpty() {
    return mAdapter.isEmpty();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Override is LOCKED. Override {@link #onCreateBinding(BindingsManager, Selector, Selector)} for making
   * customization of the class by inheritance.
   */
  @Override
  public final void onCreateBinding(final BindingsManager bm) {
    final Selector<?, View> getView = Adapters.view(bm);
    final Selector<?, Object> getItem = Adapters.item(bm);

    onCreateBinding(bm, getView, getItem);
  }

  /** {@inheritDoc} */
  @Override
  public void onValidationResult(final BindingsManager bm, final boolean success) {
    // reserved for inheritors
  }

  /** Adjusted lifecycle for Adapters. */
  public interface Lifecycle {
    /**
     * Extended version of data binding for adapters. by parameters provided selectors for
     * getting instances of adapter context: View and Item.
     *
     * @param bm       instance of bindings manager.
     * @param getModel selector for getting instance of Model/Item.
     * @param getView  selector for getting instance of View created for specific Item.
     */
    void onCreateBinding(@NonNull final BindingsManager bm,
                         @NonNull final Selector<?, View> getView,
                         @NonNull final Selector<?, Object> getModel);
  }

  /** Context for adapter. */
  private static class BindingContext {
    /** Position of currently processing item. */
    public int mPosition = -1;
    /** Reference on currently processing item view. */
    public WeakReference<View> mBindingView;
    /** Reference on currently processing item instance. */
    public WeakReference<Object> mBindingItem;
  }
}
