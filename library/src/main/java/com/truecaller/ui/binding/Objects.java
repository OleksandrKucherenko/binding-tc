package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

import java.util.List;
import java.util.Map;

/**
 * Created by alexk on 5/16/2014.
 */
public final class Objects {

	private Objects() {
		throw new AssertionError();
	}

	public static <P extends Property, I> Matcher<P> pojo(final I instance, Matcher<P> matcher) {
		return null;
	}

	public static <P extends Property, I> Matcher<P> map(final Map<String, I> instance, Matcher<P> matcher) {
		return null;
	}

	public static <P extends Property, I> Matcher<P> index(final List<I> instance, Matcher<P> matcher) {
		return null;
	}

	public static <P extends Property, I> Matcher<P> index(final I[] instance, Matcher<P> matcher) {
		return null;
	}

	public static <P extends Property> Matcher<P> property(final String name) {
		return null;
	}

	public static <P extends Property> Matcher<P> name(final String nameIn, final String nameOut) {
		return null;
	}
}
