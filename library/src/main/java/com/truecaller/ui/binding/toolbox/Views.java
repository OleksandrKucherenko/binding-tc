package com.truecaller.ui.binding.toolbox;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.truecaller.ui.binding.Selector;
import com.truecaller.ui.binding.reflection.Property;

import static com.truecaller.ui.binding.toolbox.Models.bool;
import static com.truecaller.ui.binding.toolbox.Models.integer;
import static com.truecaller.ui.binding.toolbox.Models.property;
import static com.truecaller.ui.binding.toolbox.Models.string;

/** Methods for simplifying access to different types of View storages. */
public final class Views {
  /* [ VIEW SELECTOR ] ============================================================================================ */

  public static <V extends View, T> Selector<V, Property<T>> view(
          final Selector<V, Property<V>> instance,
          final Property<T> property) {
    return null;
  }

  /* [ GENERIC SELECTORS ] ======================================================================================== */

  public static <V extends View> Selector<V, Property<V>> matches(final Selector<V, Property<V>>... selectors) {
    return null;
  }

  public static <V extends View> Selector<V, Property<V>> withId(final int id) {
    return null;
  }

  public static <V extends View> Selector<V, Property<V>> root(final Activity activity) {
    return null;
  }

  public static <V extends View> Selector<V, Property<V>> root(final Fragment activity) {
    return null;
  }

  public static <V extends View> Selector<V, Property<V>> root(final android.app.Fragment activity) {
    return null;
  }

  public static <V extends View> Selector<V, Property<V>> root(final View activity) {
    return null;
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  public static <T extends AdapterView<?>> Selector<T, Property<Integer>> adapterView(
          final Selector<T, Property<T>> selector) {
    return view(selector, integer("getSelectedItemPosition", "setSelection"));
  }

  /**
   * Text view selector, can be used for: TextView, EditText, AutoComplete, Button
   * or any other inheritor of the TextView.
   *
   * @param <T>      Type of View
   * @param selector selector that helps in identifying the view instance
   * @return the selector of "text" property from TextView inheritor
   */
  public static <T extends TextView> Selector<T, Property<String>> textView(
          final Selector<T, Property<T>> selector) {
    return view(selector, string("text"));
  }

  /**
   * Checked property selector, can be used for CheckBox, RadioButton, Switch and ToggleButton.
   *
   * @param <T>      the type parameter
   * @param selector the selector
   * @return the selector
   */
  public static <T extends CompoundButton> Selector<T, Property<Boolean>> checkedView(
          final Selector<T, Property<T>> selector) {
    return view(selector, bool("checked"));
  }

  /* [ EXTENDED VERSIONS ] ======================================================================================== */

  public static <T extends RadioGroup> Selector<T, Property<Integer>> radioGroup(
          final Selector<T, Property<T>> selector) {
    final Property<Integer> prop = property("checkedRadioButtonId");

    return view(selector, prop);
  }
}
