package com.artfulbits.binding.toolbox;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.artfulbits.binding.Selector;
import com.artfulbits.binding.reflection.Property;

import static com.artfulbits.binding.toolbox.Models.bool;
import static com.artfulbits.binding.toolbox.Models.chars;
import static com.artfulbits.binding.toolbox.Models.integer;

/** Methods for simplifying access to different types of UI controls/View's. */
@SuppressWarnings({"unused", "unchecked"})
public final class Views {
  /* [ VIEW SELECTOR ] ============================================================================================ */

  /** Create chained view property extractor. */
  @NonNull
  public static <V extends View, T> Selector<?, T> view(@NonNull final Selector<?, V> instance, @NonNull final Property<T> property) {
    return new Selector<>(instance, property);
  }

  /* [ GENERIC SELECTORS ] ======================================================================================== */

  /** Create selector that evaluated to view instance with specified id. */
  @NonNull
  public static <V extends View> Selector<?, V> withId(@NonNull final View v, final int id) {
    // View.findViewById(id)
    return new Selector<>(v, Models.<V>call("findViewById", id));
  }

  /** Extract View from other selector. */
  @NonNull
  public static <V extends View> Selector<?, V> withId(@NonNull final Selector<?, ?> s, final int id) {
    // {root}.get(id)
    return new Selector<>(s, Models.<V>call("get", id));
  }

  /** create selector bind to 'findViewById' method. */
  @NonNull
  public static <V extends View> Selector<?, V> root(@NonNull final Activity activity) {
    // activity.getWindow().getDecorView().findViewById(android.R.id.content);

    final Selector<?, ?> getWindow = new Selector<>(activity, Models.<Window>call("getWindow"));
    final Selector<?, ?> getDecorView = new Selector<>(getWindow, Models.<View>call("getDecorView"));
    return new Selector<>(getDecorView, Models.<V>call("findViewById", android.R.id.content));
  }

  /** create selector bind to 'findViewById' method. */
  @NonNull
  public static <V extends View> Selector<?, V> root(@NonNull final Fragment fragment) {
    // fragment.getView().findViewById(...);

    // double binding in use
    final Selector<?, ?> getView = new Selector<>(fragment, Models.<View>call("getView"));
    return new Selector<>(getView, Models.<V>call("findViewById"));
  }

  /** create selector bind to 'findViewById' method. */
  @NonNull
  public static <V extends View> Selector<?, V> root(@NonNull final android.app.Fragment fragment) {
    // fragment.getView().findViewById(...);

    // double binding in use
    final Selector<?, ?> getView = new Selector<>(fragment, Models.<View>call("getView"));

    return new Selector<>(getView, Models.<V>call("findViewById"));
  }

  /** create selector bind to 'findViewById' method. */
  @NonNull
  public static <V extends View> Selector<?, V> root(@NonNull final View view) {
    // view.findViewById(...);
    return new Selector<>(view, Models.<V>call("findViewById"));
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  @NonNull
  public static Selector<TextView, CharSequence> textView(@NonNull final View v, final int id) {
    return textView(Views.<TextView>withId(v, id));
  }

  @NonNull
  public static <V extends View> Selector<TextView, CharSequence> textView(@NonNull final Selector<?, V> s, final int id) {
    return textView(Views.<TextView>withId(s, id));
  }

  @NonNull
  public static Selector<EditText, CharSequence> editText(@NonNull final View v, final int id) {
    return textView(Views.<EditText>withId(v, id));
  }

  @NonNull
  public static <V extends View> Selector<EditText, CharSequence> editText(@NonNull final Selector<?, V> s, final int id) {
    return textView(Views.<EditText>withId(s, id));
  }

  @NonNull
  public static Selector<CheckBox, Boolean> checkBox(@NonNull final View v, final int id) {
    return checkedView(Views.<CheckBox>withId(v, id));
  }

  @NonNull
  public static Selector<RadioGroup, Integer> radioGroup(@NonNull final View v, final int id) {
    return radioGroup(Views.<RadioGroup>withId(v, id));
  }

  @NonNull
  public static Selector<RadioButton, Boolean> radioButton(@NonNull final View v, final int id) {
    return checkedView(Views.<RadioButton>withId(v, id));
  }

  @NonNull
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
  @NonNull
  public static <T extends TextView> Selector<T, CharSequence> textView(@NonNull final Selector<?, T> selector) {
    // final T v; v.getText(); v.setText();

    final Property<CharSequence> property = chars("Text");
    return (Selector<T, CharSequence>) view(selector, property);
  }

  /**
   * Checked property selector, can be used for CheckBox, RadioButton, Switch and ToggleButton.
   *
   * @param <T>      the type parameter
   * @param selector the selector
   * @return the selector
   */
  @NonNull
  public static <T extends CompoundButton> Selector<T, Boolean> checkedView(@NonNull final Selector<?, T> selector) {
    // final T v; v.isChecked(); v.setChecked();

    final Property<Boolean> property = bool("Checked");
    return (Selector<T, Boolean>) view(selector, property);
  }

  /** 'Checked Radio Button Id' property binding. */
  @NonNull
  public static <T extends RadioGroup> Selector<T, Integer> radioGroup(@NonNull final Selector<?, T> selector) {
    // final T v; v.getCheckedRadioButtonId();

    final Property<Integer> property = integer("getCheckedRadioButtonId", Property.NO_NAME);
    return (Selector<T, Integer>) view(selector, property);
  }

  @NonNull
  public static <T extends AdapterView<?>> Selector<T, Integer> adapterView(@NonNull final Selector<?, T> selector) {
    // final T a; a.getSelectedItemPosition(); a.setSelection();

    final Property<Integer> property = integer("getSelectedItemPosition", "setSelection");
    return (Selector<T, Integer>) view(selector, property);
  }
}
