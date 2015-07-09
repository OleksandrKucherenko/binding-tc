package com.artfulbits.ui.binding.toolbox;

import android.app.Activity;
import android.support.annotation.NonNull;
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

  /** Create chained view property extractor. */
  public static <V extends View, T> Selector<?, T> view(
      final Selector<?, V> instance,
      final Property<T> property) {
    return new Selector<>(instance, property);
  }

  /* [ GENERIC SELECTORS ] ======================================================================================== */

  /** Create selector that evaluated to view instance with specified id. */
  public static <V extends View> Selector<?, V> withId(@NonNull final View v, final int id) {
    // View.findViewById(id)
    final Property<V> p = new Property<V>(Models.<V>typeTrick(), "findViewById", Property.NO_NAME) {
      @Override
      protected Object[] getterArguments() {
        return new Object[]{id};
      }
    };

    return new Selector<>(v, p);
  }

  /** Extract View from other selector. */
  public static <V extends View> Selector<?, V> withId(@NonNull final Selector<?, ?> s, final int id) {
    // {root}.get(id)
    final Property<V> p = new Property<V>(Models.<V>typeTrick(), "get", Property.NO_NAME) {
      @Override
      protected Object[] getterArguments() {
        return new Object[]{id};
      }
    };

    return new Selector<>(s, p);
  }

  /** create selector bind to 'findViewById' method. */
  public static <V extends View> Selector<?, V> root(final Activity activity) {
    // activity.findViewById(...);
    final Property<V> find = Models.from("findViewById", Property.NO_NAME);
    return new Selector<>(activity, find);
  }

  /** create selector bind to 'findViewById' method. */
  public static <V extends View> Selector<?, V> root(final Fragment fragment) {
    // fragment.getView().findViewById(...);

    // double binding in use
    final Selector<Fragment, View> view = new Selector<>(
        fragment, Models.<View>from("getView", Property.NO_NAME));

    return new Selector<>(view, Models.<V>from("findViewById", Property.NO_NAME));
  }

  /** create selector bind to 'findViewById' method. */
  public static <V extends View> Selector<?, V> root(final android.app.Fragment fragment) {
    // fragment.getView().findViewById(...);

    // double binding in use
    final Selector<android.app.Fragment, View> view = new Selector<>(
        fragment, Models.<View>from("getView", Property.NO_NAME));

    return new Selector<>(view, Models.<V>from("findViewById", Property.NO_NAME));
  }

  /** create selector bind to 'findViewById' method. */
  public static <V extends View> Selector<?, V> root(final View view) {
    // view.findViewById(...);
    return new Selector<>(view, Models.<V>from("findViewById", Property.NO_NAME));
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  public static Selector<TextView, String> textView(@NonNull final View v, final int id) {
    return textView(Views.<TextView>withId(v, id));
  }

  public static <V extends View> Selector<TextView, String> textView(@NonNull final Selector<?, V> s, final int id) {
    return textView(Views.<TextView>withId(s, id));
  }

  public static Selector<EditText, String> editText(@NonNull final View v, final int id) {
    return textView(Views.<EditText>withId(v, id));
  }

  public static <V extends View> Selector<EditText, String> editText(@NonNull final Selector<?, V> s, final int id) {
    return textView(Views.<EditText>withId(s, id));
  }

  public static Selector<CheckBox, Boolean> checkBox(@NonNull final View v, final int id) {
    return checkedView(Views.<CheckBox>withId(v, id));
  }

  public static Selector<RadioGroup, Integer> radioGroup(@NonNull final View v, final int id) {
    return radioGroup(Views.<RadioGroup>withId(v, id));
  }

  public static Selector<RadioButton, Boolean> radioButton(@NonNull final View v, final int id) {
    return checkedView(Views.<RadioButton>withId(v, id));
  }

  public static Selector<Spinner, Integer> spinner(@NonNull final View v, final int id) {
    return adapterView(Views.<Spinner>withId(v, id));
  }

  /* [ EXTENDED VERSIONS ] ======================================================================================== */

  /**
   * Text view selector, can be used for: TextView, EditText, AutoComplete, Button or any other inheritor of the
   * TextView.
   *
   * @param <T>      Type of View
   * @param selector selector that helps in identifying the view instance
   * @return the selector of "text" property from TextView inheritor
   */
  public static <T extends TextView> Selector<T, String> textView(@NonNull final Selector<?, T> selector) {
    final Property<String> property = text("text");
    return (Selector<T, String>) view(selector, property);
  }

  /**
   * Checked property selector, can be used for CheckBox, RadioButton, Switch and ToggleButton.
   *
   * @param <T>      the type parameter
   * @param selector the selector
   * @return the selector
   */
  public static <T extends CompoundButton> Selector<T, Boolean> checkedView(@NonNull final Selector<?, T> selector) {
    final Property<Boolean> property = bool("checked");
    return (Selector<T, Boolean>) view(selector, property);
  }

  /** 'Checked Radio Button Id' property binding. */
  public static <T extends RadioGroup> Selector<T, Integer> radioGroup(@NonNull final Selector<?, T> selector) {
    final Property<Integer> property = integer("checkedRadioButtonId");

    return (Selector<T, Integer>) view(selector, property);
  }

  public static <T extends AdapterView<?>> Selector<T, Integer> adapterView(@NonNull final Selector<?, T> selector) {
    final Property<Integer> property = integer("getSelectedItemPosition", "setSelection");

    return (Selector<T, Integer>) view(selector, property);
  }
}
