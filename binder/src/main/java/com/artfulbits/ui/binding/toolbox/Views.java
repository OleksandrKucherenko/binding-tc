package com.artfulbits.ui.binding.toolbox;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.reflection.Property;

import static com.artfulbits.ui.binding.toolbox.Models.bool;
import static com.artfulbits.ui.binding.toolbox.Models.integer;
import static com.artfulbits.ui.binding.toolbox.Models.text;

/** Methods for simplifying access to different types of View storage's. */
@SuppressWarnings("unused")
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

  public static Selector<TextView, Property<String>> textView(final int id) {
    return textView(Views.<TextView>withId(id));
  }

  public static Selector<EditText, Property<String>> editText(final int id) {
    return textView(Views.<EditText>withId(id));
  }

  public static Selector<CheckBox, Property<Boolean>> checkBox(final int id) {
    return checkedView(Views.<CheckBox>withId(id));
  }

  public static Selector<RadioGroup, Property<Integer>> radioGroup(final int id) {
    return radioGroup(Views.<RadioGroup>withId(id));
  }

  public static Selector<RadioButton, Property<Boolean>> radioButton(final int id) {
    return checkedView(Views.<RadioButton>withId(id));
  }

  public static Selector<Spinner, Property<Integer>> spinner(final int id) {
    return adapterView(Views.<Spinner>withId(id));
  }

  /* [ EXTENDED VERSIONS ] ======================================================================================== */

  public static <T extends AdapterView<?>> Selector<T, Property<Integer>> adapterView(
      final Selector<T, Property<T>> selector) {
    return view(selector, integer("getSelectedItemPosition", "setSelection"));
  }

  /**
   * Text view selector, can be used for: TextView, EditText, AutoComplete, Button or any other inheritor of the
   * TextView.
   *
   * @param <T> Type of View
   * @param selector selector that helps in identifying the view instance
   * @return the selector of "text" property from TextView inheritor
   */
  public static <T extends TextView> Selector<T, Property<String>> textView(
      final Selector<T, Property<T>> selector) {
    return view(selector, text("text"));
  }

  /**
   * Checked property selector, can be used for CheckBox, RadioButton, Switch and ToggleButton.
   *
   * @param <T> the type parameter
   * @param selector the selector
   * @return the selector
   */
  public static <T extends CompoundButton> Selector<T, Property<Boolean>> checkedView(
      final Selector<T, Property<T>> selector) {
    return view(selector, bool("checked"));
  }

  public static <T extends RadioGroup> Selector<T, Property<Integer>> radioGroup(
      final Selector<T, Property<T>> selector) {
    final Property<Integer> prop = Models.integer("checkedRadioButtonId");

    return view(selector, prop);
  }
}
