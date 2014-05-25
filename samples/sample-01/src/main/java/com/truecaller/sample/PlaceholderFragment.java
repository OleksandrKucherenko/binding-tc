package com.truecaller.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truecaller.ui.binding.Binder;
import com.truecaller.ui.binding.BindingManager;

import static com.truecaller.ui.binding.toolbox.Converters.direct;
import static com.truecaller.ui.binding.toolbox.Listeners.none;
import static com.truecaller.ui.binding.toolbox.Models.pojo;
import static com.truecaller.ui.binding.toolbox.Models.property;
import static com.truecaller.ui.binding.toolbox.Views.matches;
import static com.truecaller.ui.binding.toolbox.Views.root;
import static com.truecaller.ui.binding.toolbox.Views.view;
import static com.truecaller.ui.binding.toolbox.Views.withId;
import static org.hamcrest.core.IsAnything.anything;

/** Login fragment with simplest UI. */
public class PlaceholderFragment extends Fragment implements BindingManager.LifecycleCallback {

  private final BindingManager mBm   = new BindingManager(this).register(this);
  private final User           mUser = new User();

  public PlaceholderFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_main, container, false);

    Binder bindLogin = mBm.bind(view(matches(root(view), withId(R.id.et_login)), property("text")))
            .model(pojo(mUser, property("login")));

    // update view by model values
    mBm.pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    mBm.pushByInstance(mUser);

    return view;
  }

  @Override
  public void onCreateBinding(final BindingManager bm) {

    final Binder bindPassword = bm
            .bind(view(withId(R.id.et_password), property("text")))
            .model(pojo(mUser, property("password")))
            .formatter(direct())
            .validator(anything())
            .listenOnModel(none())
            .listenOnView(none());
  }
}
