package com.truecaller.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truecaller.ui.binding.BindingManager;

import static com.truecaller.ui.binding.Objects.pojo;
import static com.truecaller.ui.binding.Storages.property;
import static com.truecaller.ui.binding.Views.withId;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements BindingManager.Callback {

    private final BindingManager mBm = new BindingManager(this).register(this);
    private final User           mUser = new User();

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mBm.onRoot(view, withId(R.id.et_login))
                .bind(property("text"))
                .storage(pojo(mUser, property("login")));

        mBm.view(withId(R.id.et_password))
                .bind(property("text"))
                .storage(pojo(mUser, property("password")));


        return view;
    }
}
