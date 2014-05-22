package com.truecaller.ui.binding;

import android.view.View;

import com.truecaller.ui.binding.reflection.Property;

/**
 * Created by alexk on 5/16/2014.
 */
public class Binder {

	public Binder() {
	}

	public <T extends View> Binder view(final Matcher<T> view) {
		return this;
	}

	public <T extends Property> Binder storage(final Matcher<T> storage) {
		return this;
	}

	public Binder listeners(final Matcher<?> listeners) {
		return this;
	}
}
