package com.truecaller.ui.binding;

import java.util.List;
import java.util.Map;

/**
 * Created by alexk on 5/16/2014.
 */
public final class Objects {

    private Objects() {
        throw new AssertionError();
    }

    public static <T> Matcher<T> pojo(final T instance, Matcher<?> matcher) {
        return null;
    }

    public static <T> Matcher<T> map(final Map<String, T> instance, Matcher<?> matcher) {
        return null;
    }

    public static <T> Matcher<T> index(final List<T> instance, Matcher<?> matcher) {
        return null;
    }

    public static <T> Matcher<T> index(final T[] instance, Matcher<?> matcher) {
        return null;
    }
}
