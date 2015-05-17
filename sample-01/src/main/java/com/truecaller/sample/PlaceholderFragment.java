package com.truecaller.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.truecaller.ui.binding.Binder;
import com.truecaller.ui.binding.BindingManager;
import com.truecaller.ui.binding.toolbox.Binders;
import com.truecaller.ui.binding.toolbox.Converters;
import com.truecaller.ui.binding.toolbox.Listeners;
import com.truecaller.ui.binding.toolbox.Views;

import static com.truecaller.ui.binding.toolbox.Models.bool;
import static com.truecaller.ui.binding.toolbox.Models.integer;
import static com.truecaller.ui.binding.toolbox.Models.pojo;
import static com.truecaller.ui.binding.toolbox.Models.string;
import static com.truecaller.ui.binding.toolbox.Views.checkBox;
import static com.truecaller.ui.binding.toolbox.Views.editText;
import static com.truecaller.ui.binding.toolbox.Views.radioButton;
import static com.truecaller.ui.binding.toolbox.Views.radioGroup;
import static com.truecaller.ui.binding.toolbox.Views.spinner;
import static com.truecaller.ui.binding.toolbox.Views.textView;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
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
        .view(textView(Views.<EditText>withId(R.id.et_login)))
        .model(pojo(mUser, string("login")));

    // update view by model values
    mBinding.pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    mBinding.pushByInstance(mUser);

    btnProceed = (Button) view.findViewById(R.id.bt_proceed);

    return view;
  }

  @Override
  public void onCreateBinding(final BindingManager bm) {
    // worst case scenario: verbose syntax for edit text
    // limit user input by NUMBERS for PIN style password, 4 digits in length
    Binders.numeric(bm)
        .view(editText(R.id.et_password))
        .onView(Listeners.<EditText>none())
        .model(pojo(mUser, integer("password")))
        .onModel(Listeners.<User>none())
        .format(Converters.<Integer>asNumber())
        .validate(allOf(greaterThanOrEqualTo(0), lessThan(10000)));

    // edit Text - validation password
    Binders.texts(bm)
        .view(editText(R.id.et_confirm_password))
        .model(pojo(mUser, string("confirmPassword")))
        .validate(is(not(emptyString()))); // ???

    // spinner
    Binders.numbers(bm)
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
    Binders.numbers(bm)
        .view(radioGroup(R.id.rg_options))
        .model(pojo(mUser, integer("")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(R.id.rb_chooseGroup))
        .model(pojo(mUser, bool("")));

    // radio button
    Binders.bools(bm)
        .view(radioButton(R.id.rb_openLast))
        .model(pojo(mUser, bool("")));
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
