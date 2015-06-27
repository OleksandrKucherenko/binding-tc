package com.artfulbits.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingManager;
import com.artfulbits.ui.binding.toolbox.Binders;
import com.artfulbits.ui.binding.toolbox.Formatter;
import com.artfulbits.ui.binding.toolbox.Listeners;

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
import static org.hamcrest.core.IsNot.not;

/** Login fragment with simplest UI. */
public class PlaceholderFragment extends Fragment implements BindingManager.LifecycleCallback {

  private final BindingManager mBinding = new BindingManager(this).register(this);
  private final User mUser = new User();
  private Button btnProceed;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_main, container, false);

    // create binding to Login
    final Binder bindLogin = Binders.texts(mBinding)
        .view(textView(R.id.et_login))
        .model(pojo(mUser, text("login")));

    // update view by model values
    mBinding.pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    mBinding.pushByModel(mUser);

    btnProceed = (Button) view.findViewById(R.id.bt_proceed);

    return view;
  }

  @Override
  public void onCreateBinding(final BindingManager bm) {
    // #1: worst case scenario: verbose syntax for edit text
    // #2: limit user input by NUMBERS for PIN style password, 4 digits in length
    Binders.numeric(bm)
        .view(editText(R.id.et_password))
        .onView(Listeners.<EditText>none())
        .model(pojo(mUser, integer("Password")))
        .onModel(Listeners.<User>none())
        .format(Formatter.<Integer>asNumber())
        .validate(allOf(greaterThanOrEqualTo(0), lessThan(10000)));

    // edit Text - validation password
    Binders.texts(bm)
        .view(editText(R.id.et_confirm_password))
        .model(pojo(mUser, text("ConfirmPassword")))
        .validate(is(not(emptyString()))); // ???

    // spinner
    Binders.integers(bm)
        .view(spinner(R.id.sp_group))
        .model(pojo(mUser, integer("")));

    // master-details scenario: master checkbox
    Binders.bools(bm)
        .view(checkBox(R.id.cb_login))
        .model(pojo(mUser, bool("StoreLogin")));

    // master-detail scenario: details checkbox
    Binders.bools(bm)
        .view(checkBox(R.id.cb_password))
        .model(pojo(mUser, bool("StorePassword")));

    // radio group
    Binders.integers(bm)
        .view(radioGroup(R.id.rg_options))
        .model(pojo(mUser, integer("Group")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(R.id.rb_chooseGroup))
        .model(pojo(mUser, bool("SelectNewGroup")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(R.id.rb_openLast))
        .model(pojo(mUser, bool("ContinueFromLastPoint")));

    bm.associate();
  }

  @Override
  public void onValidationResult(final BindingManager bm, final boolean success) {

    btnProceed.setEnabled(success);

    if (!success) {
      for (Binder binder : bm.getFailedBindings()) {
        // TODO: show errors
      }
    }
  }
}
