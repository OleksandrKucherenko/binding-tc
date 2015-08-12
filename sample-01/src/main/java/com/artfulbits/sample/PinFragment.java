package com.artfulbits.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artfulbits.sample.data.Pin;
import com.artfulbits.ui.binding.BindingsManager;
import com.artfulbits.ui.binding.toolbox.Binders;
import com.artfulbits.ui.binding.toolbox.BindingFragment;
import com.artfulbits.ui.binding.toolbox.Listeners;
import com.artfulbits.ui.binding.toolbox.ToView;

import org.hamcrest.Matchers;

import static com.artfulbits.ui.binding.toolbox.Listeners.anyOf;
import static com.artfulbits.ui.binding.toolbox.Listeners.*;
import static com.artfulbits.ui.binding.toolbox.Models.*;
import static com.artfulbits.ui.binding.toolbox.Molds.fromCharsToString;
import static com.artfulbits.ui.binding.toolbox.Molds.onlyPop;
import static com.artfulbits.ui.binding.toolbox.Molds.onlyPush;
import static com.artfulbits.ui.binding.toolbox.Views.*;
import static org.hamcrest.Matchers.*;

/** Fragment for entering PIN, demo of connected to each other properties. */
public class PinFragment extends BindingFragment {
  /** model instance. */
  private final Pin mPin = new Pin();

  /** Get access to model instance. */
  public Pin getModel() {
    return mPin;
  }

  /** {@inheritDoc} */
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saved) {
    return inflater.inflate(R.layout.fragment_pin, container, false);
  }

  /** {@inheritDoc} */
  @Override
  public void onCreateBinding(@NonNull final BindingsManager bm) {
    if (null == getView()) return;

    /**
     * EXPLANATION:
     * - we listen to user input in et_password and et_confirm_password
     * - during update of the model instance by entered text we expect Observable callbacks
     * - observable callback force Binding update of UI label - tv_feedback
     *
     * USE CASE:
     * - on wrong password display Error message
     * - on correct password display OK message,
     * - Correct pattern: pin contains at least 4 digits and not more than 6
     */

    // do binding only from UI side, only PUSH
    Binders.strings(bm) // <CharSequence - to - String>
        .view(editText(getView(), R.id.et_password))
        .onView(Listeners.anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mPin, strings("Password")))
        .format(onlyPush(fromCharsToString()))
        .validate(Matchers.anyOf(blankString(), matchesPattern("[0-9]{4,6}")));

    Binders.strings(bm) // <CharSequence - to - String>
        .view(editText(getView(), R.id.et_confirm_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mPin, strings("ConfirmPassword")))
//        .validate(matchesPattern("[0-9]{4,6}")) // important!!!
        .setTag(R.id.tag_test, "test");

    // POP message from model to View only, do that on model state change
    Binders.numeric(bm) // <CharSequence - to - Integer>
        .view(textView(getView(), R.id.tv_feedback))
        .model(pojo(mPin, integer("Message")))
        .onModel(onObservable())
        .format(onlyPop(new ToView<CharSequence, Integer>() {
          @Override
          public CharSequence toView(final Integer value) {
            return getString(value);
          }
        }));
  }
}
