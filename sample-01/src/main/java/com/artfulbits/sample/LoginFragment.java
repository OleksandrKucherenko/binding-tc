package com.artfulbits.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.artfulbits.sample.data.User;
import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingsManager;
import com.artfulbits.ui.binding.Failure;
import com.artfulbits.ui.binding.Success;
import com.artfulbits.ui.binding.reflection.Property;
import com.artfulbits.ui.binding.toolbox.Binders;
import com.artfulbits.ui.binding.toolbox.ToView;
import com.artfulbits.ui.binding.ui.BindingSupportFragment;

import static com.artfulbits.ui.binding.toolbox.Listeners.anyOf;
import static com.artfulbits.ui.binding.toolbox.Listeners.*;
import static com.artfulbits.ui.binding.toolbox.Models.*;
import static com.artfulbits.ui.binding.toolbox.Molds.fromCharsToInteger;
import static com.artfulbits.ui.binding.toolbox.Molds.onlyPop;
import static com.artfulbits.ui.binding.toolbox.Views.*;
import static org.hamcrest.Matchers.*;

/** Login fragment with simplest UI. */
public class LoginFragment extends BindingSupportFragment {
  /** model instance. */
  private final User mUser = new User();
  /** reference on Proceed button. */
  private Button btnProceed;

  /** {@inheritDoc} */
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saved) {
    final View view = inflater.inflate(R.layout.fragment_main, container, false);

    // create binding to Login
    final Binder<?, ?> bindLogin = Binders.strings(getBindingsManager())
        .view(textView(view, R.id.et_login))
        .model(pojo(mUser, string("Login")));

    // update view by model values
    getBindingsManager().pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    getBindingsManager().pushTo(mUser);

    btnProceed = (Button) view.findViewById(R.id.bt_proceed);

    return view;
  }

  /** {@inheritDoc} */
  @Override
  public void onCreateBinding(@NonNull final BindingsManager bm) {
    if (null == getView()) {
      throw new AssertionError("That should never happens. Lifecycle expects existence of View.");
    }

    // one way binding with timer thread
    Binders.numeric(bm)
        .view(textView(getView(), R.id.tv_login))
        .model(pojo(mUser, integer("getActiveTime", Property.NO_NAME)))
        .onModel(onTimer(1000, 1000)) // update every second
        .format(onlyPop(new ToView<CharSequence, Integer>() {
          @Override
          public CharSequence toView(final Integer value) {
            return getString(R.string.labelLogin).replace(":", " [" + value + " sec]:");
          }
        }));

    // #1: worst case scenario: verbose syntax for edit string
    // #2: limit user input by NUMBERS for PIN style password, 4 digits in length
    Binders.numeric(bm)
        .view(editText(getView(), R.id.et_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mUser, integer("Pin")))
        .onModel(onObservable("Pin"))
        .format(fromCharsToInteger())
        .validate(allOf(greaterThanOrEqualTo(0), lessThan(10000)))
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            ((TextView) getView().findViewById(R.id.tv_password)).setText(R.string.labelPasswordFail);
          }
        })
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            ((TextView) getView().findViewById(R.id.tv_password)).setText(R.string.labelPasswordOk);
          }
        });

    // edit Text - validation password
    Binders.numeric(bm)
        .view(editText(getView(), R.id.et_confirm_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mUser, integer("ConfirmPin")))
        .onModel(anyOf(onObservable("PinsEqual"), onObservable("ConfirmPin")))
        .validate(is(equalTo(mUser.getPin())))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            ((TextView) getView().findViewById(R.id.tv_confirm_password)).setText(R.string.labelPasswordOk);
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            ((TextView) getView().findViewById(R.id.tv_confirm_password)).setText(R.string.labelPasswordFail);
          }
        });

    // spinner
    Binders.integers(bm)
        .view(spinner(getView(), R.id.sp_group))
        .model(pojo(mUser, integer("GroupSpin")));

    // master-details scenario: master checkbox
    Binders.bools(bm)
        .view(checkBox(getView(), R.id.cb_login))
        .model(pojo(mUser, bool("StoreLogin")));

    // master-detail scenario: details checkbox
    Binders.bools(bm)
        .view(checkBox(getView(), R.id.cb_password))
        .model(pojo(mUser, bool("StorePassword")));

    // radio group
    Binders.integers(bm)
        .view(radioGroup(getView(), R.id.rg_options))
        .model(pojo(mUser, integer("Group")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(getView(), R.id.rb_chooseGroup))
        .model(pojo(mUser, bool("SelectNewGroup")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(getView(), R.id.rb_openLast))
        .model(pojo(mUser, bool("ContinueFromLastPoint")));

    bm.associate();
  }

  /** {@inheritDoc} */
  @Override
  public void onValidationResult(final BindingsManager bm, final boolean success) {
    btnProceed.setEnabled(success);

    if (!success) {
      for (Binder<?, ?> binder : bm.getFailedBindings()) {
        Log.i("BINDING", "Result of validation: " + binder.toString());
      }
    }
  }
}
