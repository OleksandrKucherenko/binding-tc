package com.truecaller.ui.binding.toolbox;

import com.truecaller.ui.binding.Matcher;
import com.truecaller.ui.binding.reflection.Property;

import java.util.List;
import java.util.Map;

/** Helpers that simplify common operations with different data storage instances. */
public final class Models {

  private Models() {
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
