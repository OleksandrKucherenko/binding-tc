package com.truecaller.ui.binding.toolbox;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.truecaller.ui.binding.Matcher;
import com.truecaller.ui.binding.reflection.Property;

/** Methods for simplifying access to different types of View storages. */
public final class Views {
  /**
   * Merge multiple Matcher's into long AND expression.
   *
   * @param <T>     reflection property type
   * @param matches array of matcher's instances
   * @return aggregation instance for calculating logic AND operation.
   */
  public static <T extends Property> Matcher<T> matches(final Matcher<T>... matches) {
    return null;
  }

  public static <T extends Property, P extends Property> Matcher<T> view(final Matcher<T> view,
                                                                         final Matcher<P> property) {
    return null;
  }

  public static <T extends Property> Matcher<T> withId(final int id) {
    return null;
  }

  public static <T extends Property> Matcher<T> onRoot(final Activity activity) {
    return null;
  }

  public static <T extends Property> Matcher<T> onRoot(final Fragment activity) {
    return null;
  }

  public static <T extends Property> Matcher<T> onRoot(final android.app.Fragment activity) {
    return null;
  }

  public static <T extends Property> Matcher<T> root(final View activity) {
    return null;
  }
}
