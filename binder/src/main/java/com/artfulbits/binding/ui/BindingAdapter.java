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

/** Wrapper over instance of Adapter. Provides access to Binding Manager instance and lifecycle. */
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
  private final ThreadContext mContext = new ThreadContext();

	/* [ CONSTRUCTORS ] ============================================================================================== */

  public BindingAdapter(@NonNull final Adapter adapter) {
    this(adapter, null);
  }

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
  private ThreadContext getThreadContext() {
    return mContext;
  }

  public BindingsManager getBindingsManager() {
    return mBindingsManager;
  }

  /** Get reference on inner wrapped adapter. */
  public Adapter getInnerAdapter() {
    return mAdapter;
  }

  public int getBindingPosition() {
    return getThreadContext().mPosition;
  }

  private BindingAdapter setBindingPosition(final int position) {
    getThreadContext().mPosition = position;

    return this;
  }

  @Nullable
  public Object getBindingItem() {
    if (null == getThreadContext().mBindingItem) return null;

    return getThreadContext().mBindingItem.get();
  }

  private BindingAdapter setBindingItem(final Object item) {
    getThreadContext().mBindingItem = new WeakReference<>(item);

    return this;
  }

  @Nullable
  public View getBindingView() {
    if (null == getThreadContext().mBindingView) return null;

    return getThreadContext().mBindingView.get();
  }

  private BindingAdapter setBindingView(final View view) {
    getThreadContext().mBindingView = new WeakReference<>(view);

    return this;
  }

	/* [ METHODS ] =================================================================================================== */

  /** {@inheritDoc} */
  @CallSuper
  public void onCreateBinding(@NonNull final BindingsManager bm,
                              @NonNull final Selector<?, View> getView,
                              @NonNull final Selector<?, Object> getModel) {
    if (null != mLifecycle) {
      mLifecycle.onCreateBinding(bm, getView, getModel);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void registerDataSetObserver(final DataSetObserver observer) {
    mAdapter.registerDataSetObserver(observer);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterDataSetObserver(final DataSetObserver observer) {
    mAdapter.unregisterDataSetObserver(observer);
  }

	/* [ Interface Adapter ] ========================================================================================= */

  /** {@inheritDoc} */
  @Override
  public int getCount() {
    return mAdapter.getCount();
  }

  /** {@inheritDoc} */
  @Override
  public Object getItem(final int position) {
    return mAdapter.getItem(position);
  }

  /** {@inheritDoc} */
  @Override
  public long getItemId(final int position) {
    return mAdapter.getItemId(position);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasStableIds() {
    return mAdapter.hasStableIds();
  }

  /** Implemented binding of Item to View. */
  @Override
  public final View getView(final int position, final View convertView, final ViewGroup parent) {
    // request wrapped adapter to create a view for us
    final View v = mAdapter.getView(position, convertView, parent);

    // assign binding dependencies, define selectors context
    this.setBindingPosition(position)
        .setBindingItem(getItem(position))
        .setBindingView(v);

    // TODO: apply data exchange MODEL --> VIEW (POP), time to do the magic
    getBindingsManager().pop();

    // should we run it for all rules???
    // can we limit/filter POP by Model instance or View instance ???

    return v;
  }

  /** {@inheritDoc} */
  @Override
  public int getItemViewType(final int position) {
    return mAdapter.getItemViewType(position);
  }

  /** {@inheritDoc} */
  @Override
  public int getViewTypeCount() {
    return mAdapter.getViewTypeCount();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmpty() {
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

  /** Context of adapter per thread. */
  private static class ThreadContext {
    /** creator of the context. */
    public final int ThreadId = (int) Thread.currentThread().getId();
    /** Position of currently processing item. */
    public int mPosition = -1;
    /** Reference on currently processing item view. */
    public WeakReference<View> mBindingView;
    /** Reference on currently processing item instance. */
    public WeakReference<Object> mBindingItem;
  }
}
