package com.truecaller.ui.binding;

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

    private final WeakHashMap<Callback, Callback> mListeners = new WeakHashMap<Callback, Callback>();

    public BindingManager(final Callback listener) {
        mListeners.put(listener, listener);
    }

    public Binder onRoot(final View view, Matcher<?> matches) {
        return new Binder( view, matches );
    }

    public interface Callback {

    }
}
