package com.truecaller.ui.binding;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.WeakHashMap;

/**
 * Manager class responsible for controlling integration of the binding library into corresponding fragment or
 * activity. It controls aspects:<br/>
 * <ul>
 * <li>context instance extracting;</li>
 * <li>Binding defining and configuring;</li>
 * <li></li>
 * </ul>
 */
public class BindingManager {
	/* ==============================[ CONSTANTS AND MEMBERS ]========================== */

	/** Weak references on listeners. */
	private final WeakHashMap<Callback, Callback> mListeners = new WeakHashMap<Callback, Callback>();
	/** Reference on root element of binding. */
	private final Activity mRootActivity;

	private final FragmentFacade mRootFragment;

	private final View mRootView;

	private final BaseAdapter mRootAdapter;

  /* ==============================[ CONSTRUCTORS ]========================== */

	private BindingManager(final Activity activity, final FragmentFacade fragment, final View view, final BaseAdapter adapter) {
		mRootActivity = activity;
		mRootFragment = fragment;
		mRootView = view;
		mRootAdapter = adapter;
	}

	public BindingManager(final Activity parent) {
		this(parent, null, null, null);
	}

	public BindingManager(final Fragment parent) {
		this(null, new FragmentFacade(parent), null, null);
	}

	public BindingManager(final android.support.v4.app.Fragment parent) {
		this(null, new FragmentFacade(parent), null, null);
	}

	public BindingManager(final View parent) {
		this(null, null, parent, null);
	}

	public BindingManager(final BaseAdapter adapter) {
		this(null, null, null, adapter);
	}

  /* ==============================[ IMPLEMENTATION ]========================== */

	public BindingManager register(final Callback listener) {
		mListeners.put(listener, listener);
		return this;
	}

	public BindingManager unregister(final Callback listener) {
		mListeners.remove(listener);
		return this;
	}

	public Binder bind(final Matcher<?> matcher) {
		return null;
	}

	/* ==============================[ HELPERS ]========================== */

	/* ==============================[ NESTED DECLARATIONS ]========================== */

	private static final class FragmentFacade {
		public FragmentFacade(android.support.v4.app.Fragment fragment) {

		}

		public FragmentFacade(Fragment fragment) {

		}
	}

	public interface Callback {

	}
}
