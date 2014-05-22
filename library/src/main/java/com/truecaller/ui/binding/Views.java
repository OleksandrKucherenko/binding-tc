package com.truecaller.ui.binding;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.truecaller.ui.binding.reflection.Property;

/** static methods for access different types of Views. */
public final class Views {
	public static <T extends View> Matcher<T> matches(final Matcher<T>... matches) {
		return null;
	}

	public static <T extends View, P extends Property> Matcher<T> view(final Matcher<T> view, final Matcher<P> property) {
		return null;
	}

	public static <T extends View> Matcher<T> withId(final int id) {
		return null;
	}

	public static <T extends View> Matcher<T> onRoot(final Activity activity) {
		return null;
	}

	public static <T extends View> Matcher<T> onRoot(final Fragment activity) {
		return null;
	}

	public static <T extends View> Matcher<T> onRoot(final android.app.Fragment activity) {
		return null;
	}

	public static <T extends View> Matcher<T> root(final View activity) {
		return null;
	}
}
