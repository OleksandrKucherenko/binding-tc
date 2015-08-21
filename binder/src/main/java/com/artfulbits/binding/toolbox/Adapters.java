package com.artfulbits.binding.toolbox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;

import com.artfulbits.binding.BindingsManager;
import com.artfulbits.binding.Selector;
import com.artfulbits.binding.ui.BindingAdapter;

/** Utility class. simplify binding for data adapters. */
public final class Adapters {
  /** hidden constructor. */
  private Adapters() {
    throw new AssertionError("Not for inheritance or call.");
  }


  /**
   * Create Facade selector that allows to access the "root" of the binding. In this
   * specific case we are accessing inner adapter instance that we wrap by 'binding'.
   *
   * @param adapter instance of wrapped adapter
   */
  @NonNull
  public static <V> Selector<?, V> root(@NonNull final BindingAdapter adapter) {
    //final Adapter inner = adapter.getInnerAdapter();
    return new Selector<>(adapter, Models.<V>call("getInnerAdapter"));
  }

  /** Get selector of current view for processing. */
  @NonNull
  public static <V extends View> Selector<?, V> view(@NonNull final BindingsManager bm) {
    // facade should allow resolving of index/position to View and Model instances
    final Selector<?, ?> facade = bm.getFacade();
    final BindingAdapter adapter = (BindingAdapter) facade.getRuntimeInstance();

    //adapter.getBindingView()
    return new Selector<>(adapter, Models.<V>call("getBindingView"));
  }

  /** Get selector of current item for processing. */
  @NonNull
  public static <V> Selector<?, V> item(@NonNull final BindingsManager bm) {
    final Selector<?, Integer> facade = bm.getFacade();
    final BindingAdapter adapter = (BindingAdapter) facade.getRuntimeInstance();

    // adapter.getBindingItem()
    return new Selector<>(adapter, Models.<V>call("getBindingItem"));
  }

  /**
   * Create instance of bindings ready adapter with attached lifecycle.
   *
   * @param adapter instance of adapter
   */
  @NonNull
  public static BindingAdapter wrap(@NonNull final Adapter adapter) {
    return new BindingAdapter(adapter);
  }

  /**
   * Create instance of bindings ready adapter with attached lifecycle.
   *
   * @param adapter   instance of adapter
   * @param lifecycle instance of the lifecycle listener, can be NULL.
   */
  @NonNull
  public static BindingAdapter wrap(@NonNull final Adapter adapter,
                                    @Nullable final BindingAdapter.Lifecycle lifecycle) {
    return new BindingAdapter(adapter, lifecycle);
  }
}
