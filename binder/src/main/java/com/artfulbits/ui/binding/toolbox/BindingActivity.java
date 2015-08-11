package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.artfulbits.ui.binding.BindingsManager;

/** High level activity that implements Binding manager support. */
public class BindingActivity extends AppCompatActivity implements BindingsManager.Lifecycle {
  /** Instance of binding manager. */
  private BindingsManager mBm = BindingsManager.newInstance(this, this);

  /** get instance of the Binding manager. */
  public BindingsManager getBindingsManager() {
    return mBm;
  }

  @Override
  protected void onRestart() {
    super.onRestart();

    mBm.doRestart(); // equal to call of onDestroy()
  }

  @Override
  public void onStart() {
    super.onStart();

    // freeze all updates
    mBm.freeze().doStart(); // call of --> onCreateBinding(...);
  }

  @Override
  public void onResume() {
    super.onResume();

    // fragment is ready for updates. UN-freeze can be called several times
    mBm.unfreeze().doResume(); // call of --> onValidationResult
  }

  @Override
  public void onPause() {
    super.onPause();

    mBm.doPause();
  }

  @Override
  public void onDestroy() {
    mBm.doDestroy();

    // cleanup the binding manager instance
    super.onDestroy();
  }

  @Override
  public void onCreateBinding(@NonNull final BindingsManager bm) {
  }

  @Override
  public void onValidationResult(@NonNull final BindingsManager bm, boolean success) {
  }
}
