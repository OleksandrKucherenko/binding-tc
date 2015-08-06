package com.artfulbits.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingsManager;
import com.artfulbits.ui.binding.toolbox.BindingFragment;
import com.artfulbits.ui.binding.toolbox.ToView;

import static com.artfulbits.ui.binding.toolbox.Formatter.onlyPop;
import static com.artfulbits.ui.binding.toolbox.Formatter.toInteger;
import static com.artfulbits.ui.binding.toolbox.Listeners.anyOf;
import static com.artfulbits.ui.binding.toolbox.Listeners.onFocusLost;
import static com.artfulbits.ui.binding.toolbox.Listeners.onObservable;
import static com.artfulbits.ui.binding.toolbox.Listeners.onTextChanged;
import static com.artfulbits.ui.binding.toolbox.Listeners.onTimer;
import static com.artfulbits.ui.binding.toolbox.Models.bool;
import static com.artfulbits.ui.binding.toolbox.Models.integer;
import static com.artfulbits.ui.binding.toolbox.Models.pojo;
import static com.artfulbits.ui.binding.toolbox.Models.text;
import static com.artfulbits.ui.binding.toolbox.Views.checkBox;
import static com.artfulbits.ui.binding.toolbox.Views.editText;
import static com.artfulbits.ui.binding.toolbox.Views.radioButton;
import static com.artfulbits.ui.binding.toolbox.Views.radioGroup;
import static com.artfulbits.ui.binding.toolbox.Views.spinner;
import static com.artfulbits.ui.binding.toolbox.Views.textView;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.AllOf.allOf;

/** Login fragment with simplest UI. */
public class PlaceholderFragment extends BindingFragment implements BindingsManager.Lifecycle {
  /** model instance. */
  private final User mUser = new User();
  /** reference on Proceed button. */
  private Button btnProceed;

  /** {@inheritDoc} */
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saved) {
    final View view = inflater.inflate(R.layout.fragment_main, container, false);

    // create binding to Login
    final Binder bindLogin = getBindingsManager().texts()
        .view(textView(view, R.id.et_login))
        .model(pojo(mUser, text("Login")));

    // update view by model values
    getBindingsManager().pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    getBindingsManager().pushTo(mUser);

    btnProceed = (Button) view.findViewById(R.id.bt_proceed);

    return view;
  }

  /** {@inheritDoc} */
  @Override
  public void onCreateBinding(final BindingsManager bm) {
    if (null == getView()) {
      throw new AssertionError("That should never happens. Lifecycle expects existence of View.");
    }

    // #1: worst case scenario: verbose syntax for edit text
    // #2: limit user input by NUMBERS for PIN style password, 4 digits in length
    bm.numeric()
        .view(editText(getView(), R.id.et_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mUser, integer("Pin")))
        .onModel(onObservable("Pin"))
        .format(toInteger())
        .validate(allOf(greaterThanOrEqualTo(0), lessThan(10000)));

    // one way binding with timer thread
    bm.numeric()
        .view(textView(getView(), R.id.tv_login))
        .model(pojo(mUser, integer("getActiveTime")))
        .onModel(onTimer(1000, 1000)) // update every second
        .format(onlyPop(new ToView<String, Integer>() {
          @Override
          public String toView(final Integer value) {
            return getString(R.string.labelLogin).replace(":", " [" + value + " sec]:");
          }
        }));

    // edit Text - validation password
    bm.numeric()
        .view(editText(getView(), R.id.et_confirm_password))
        .model(pojo(mUser, integer("ConfirmPin")))
        .validate(is(equalTo(mUser.getPin()))); // ???

    // spinner
    bm.integers()
        .view(spinner(getView(), R.id.sp_group))
        .model(pojo(mUser, integer("GroupSpin")));

    // master-details scenario: master checkbox
    bm.bools()
        .view(checkBox(getView(), R.id.cb_login))
        .model(pojo(mUser, bool("StoreLogin")));

    // master-detail scenario: details checkbox
    bm.bools()
        .view(checkBox(getView(), R.id.cb_password))
        .model(pojo(mUser, bool("StorePassword")));

    // radio group
    bm.integers()
        .view(radioGroup(getView(), R.id.rg_options))
        .model(pojo(mUser, integer("Group")));

    // radio button
    bm.bools()
        .view(radioButton(getView(), R.id.rb_chooseGroup))
        .model(pojo(mUser, bool("SelectNewGroup")));

    // radio button
    bm.bools()
        .view(radioButton(getView(), R.id.rb_openLast))
        .model(pojo(mUser, bool("ContinueFromLastPoint")));

    bm.associate();
  }

  /** {@inheritDoc} */
  @Override
  public void onValidationResult(final BindingsManager bm, final boolean success) {
    btnProceed.setEnabled(success);

    if (!success) {
      for (Binder binder : bm.getFailedBindings()) {
        Log.i("BINDING", "Result of validation: " + binder.toString());
      }
    }
  }
}
