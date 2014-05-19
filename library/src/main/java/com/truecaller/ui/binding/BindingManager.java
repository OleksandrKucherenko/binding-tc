package com.truecaller.ui.binding;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

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
    private final RootView mRoot;

    /* ==============================[ CONSTRUCTORS ]========================== */

    public BindingManager(final Activity parent) {
        mRoot = RootView.fromActivity(parent);
    }

    public BindingManager(final Fragment parent) {
        mRoot = RootView.fromFragment(parent);
    }

    public BindingManager(final android.support.v4.app.Fragment parent) {
        mRoot = RootView.fromFragment(parent);
    }

    public BindingManager(final View parent) {
        mRoot = RootView.fromView(parent);
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

    public Binder onRoot(final View view, Matcher<?> matches) {
        return new Binder(RootView.fromView(view).find(matches));
    }

    public Binder view(final Matcher<?> matches) {
        return new Binder(mRoot.find(matches));
    }

    /* ==============================[ HELPERS ]========================== */

    /* ==============================[ NESTED DECLARATIONS ]========================== */

    public static class RootView {

        private final View mView;

        private RootView(final View view) {
            mView = view;
        }

        public static RootView fromActivity(final Activity parent) {
            return new RootView(parent.getWindow().getDecorView().getRootView());
        }

        public static RootView fromFragment(final android.support.v4.app.Fragment fragment) {
            return new RootView(fragment.getView());
        }

        public static RootView fromFragment(final Fragment fragment) {
            return new RootView(fragment.getView());
        }

        public static RootView fromView(final View view) {
            return new RootView(view);
        }

        public View root() {
            return mView;
        }

        public View find(final Matcher<?> matchers) {
            return null;
        }
    }

    public interface Callback {

    }
}
