package com.artfulbits.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.artfulbits.ui.binding.ui.BindingActivity;

/** Main activity, host fragments. */
public class MainActivity extends BindingActivity {
  /* [ CONSTANTS ] ================================================================================================= */

  public static final String EXTRA_FRAGMENT = "fragment_to_show";

  /* [ MEMBERS ] =================================================================================================== */

  private Fragment mCurrentFragment;

	/* [ STATIC METHODS ] ============================================================================================ */

  @NonNull
  public static Intent showLogin(@NonNull final Context context) {
    return new Intent(context, MainActivity.class)
        .putExtra(EXTRA_FRAGMENT, LoginFragment.class);
  }

  @NonNull
  public static Intent showPin(@NonNull final Context context) {
    return new Intent(context, MainActivity.class)
        .putExtra(EXTRA_FRAGMENT, PinFragment.class);
  }

	/* [ LIFECYCLE ] ================================================================================================= */

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Class<?> extra = LoginFragment.class;

    // get Intent that starts us, it may contain info about fragment
    final Intent intent = getIntent();
    if (null != intent && intent.hasExtra(EXTRA_FRAGMENT)) {
      extra = (Class<?>) intent.getSerializableExtra(EXTRA_FRAGMENT);
    }

    if (savedInstanceState == null) {
      // create instance of the fragment from it Class<?> info
      try {
        mCurrentFragment = (Fragment) extra.newInstance();
      } catch (Throwable e) {
        mCurrentFragment = new LoginFragment();
      }

      // replace container.
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, mCurrentFragment)
          .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  @SuppressWarnings("unchecked")
  public <T extends Fragment> T getCurrentFragment() {
    return (T) mCurrentFragment;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean result = false;

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      // TODO: place reaction on settings click

      result = true;
    } else {
      result = super.onOptionsItemSelected(item);
    }

    return result;
  }
}
