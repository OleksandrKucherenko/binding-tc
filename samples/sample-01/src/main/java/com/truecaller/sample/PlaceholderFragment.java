package com.truecaller.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truecaller.ui.binding.BindingManager;

import static com.truecaller.ui.binding.Objects.pojo;
import static com.truecaller.ui.binding.Objects.property;
import static com.truecaller.ui.binding.Views.matches;
import static com.truecaller.ui.binding.Views.root;
import static com.truecaller.ui.binding.Views.view;
import static com.truecaller.ui.binding.Views.withId;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements BindingManager.Callback {

	private final BindingManager mBm = new BindingManager(this).register(this);
	private final User mUser = new User();

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_main, container, false);

		mBm.bind(view(matches(root(view), withId(R.id.et_login)), property("text")))
						.storage(pojo(mUser, property("login")));

		mBm.bind(view(withId(R.id.et_password), property("text")))
						.storage(pojo(mUser, property("password")));

		return view;
	}
}
