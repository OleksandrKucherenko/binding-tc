package com.artfulbits.ui.binding.ui;

import android.database.DataSetObserver;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.artfulbits.ui.binding.BindingsManager;
import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.toolbox.Adapters;

import java.lang.ref.WeakReference;

/** Wrapper over instance of Adapter. Provides access to Binding Manager instance and lifecycle. */
@SuppressWarnings("unused")
public class BindingAdapter implements Adapter, BindingsManager.Lifecycle {
  /* [ MEMBERS ] =================================================================================================== */

  /** Wrapped adapter. */
  private final Adapter mAdapter;
  /** Instance of binding manager. */
  private final BindingsManager mBindingsManager;
  /** Reference on lifecycle callback. */
  private final Lifecycle mLifecycle;
  /** Position of currently processing item. */
  private int mPosition = -1;
  /** Reference on currently processing item view. */
  private WeakReference<View> mBindingView;
  /** Reference on currently processing item instance. */
  private WeakReference<Object> mBindingItem;

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

  public int getBindingPosition() {
    return mPosition;
  }

  public BindingAdapter setBindingPosition(final int position) {
    mPosition = position;

    return this;
  }

  public BindingsManager getBindingsManager() {
    return mBindingsManager;
  }

  @Nullable
  public Object getBindingItem() {
    if (null == mBindingItem) return null;

    return mBindingItem.get();
  }

  private BindingAdapter setBindingItem(final Object item) {
    mBindingItem = new WeakReference<>(item);

    return this;
  }

  @Nullable
  public View getBindingView() {
    if (null == mBindingView) return null;

    return mBindingView.get();
  }

  private BindingAdapter setBindingView(final View view) {
    mBindingView = new WeakReference<>(view);

    return this;
  }

	/* [ METHODS ] =================================================================================================== */

  @CallSuper
  public void onCreateBinding(@NonNull final BindingsManager bm,
                              @NonNull final Selector<?, View> getView,
                              @NonNull final Selector<?, Object> getModel) {
    if (null != mLifecycle) {
      mLifecycle.onCreateBinding(bm, getView, getModel);
    }
  }

  @Override
  public void registerDataSetObserver(final DataSetObserver observer) {
    mAdapter.registerDataSetObserver(observer);
  }

  @Override
  public void unregisterDataSetObserver(final DataSetObserver observer) {
    mAdapter.unregisterDataSetObserver(observer);
  }

	/* [ Interface Adapter ] ========================================================================================= */

  @Override
  public int getCount() {
    return mAdapter.getCount();
  }

  @Override
  public Object getItem(final int position) {
    return mAdapter.getItem(position);
  }

  @Override
  public long getItemId(final int position) {
    return mAdapter.getItemId(position);
  }

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

  @Override
  public int getItemViewType(final int position) {
    return mAdapter.getItemViewType(position);
  }

  @Override
  public int getViewTypeCount() {
    return mAdapter.getViewTypeCount();
  }

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

  @Override
  public void onValidationResult(final BindingsManager bm, final boolean success) {
    // reserved for inheritors
  }

  /** Adjusted lifecycle for Adapters. */
  public interface Lifecycle {
    void onCreateBinding(@NonNull final BindingsManager bm,
                         @NonNull final Selector<?, View> getView,
                         @NonNull final Selector<?, Object> getModel);
  }
}
