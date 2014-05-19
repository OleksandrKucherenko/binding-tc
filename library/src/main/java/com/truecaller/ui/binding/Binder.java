package com.truecaller.ui.binding;

import android.view.View;

/**
 * Created by alexk on 5/16/2014.
 */
public class Binder {

    private final View mView;

    public Binder(final View view) {
        mView = view;
    }

    public Binder bind(final Matcher<?> matches) {
        return this;
    }

    public Binder storage(final Matcher<?> item) {
        return this;
    }

    public Binder listeners(final Matcher<?> listeners) {
        return this;
    }
}
