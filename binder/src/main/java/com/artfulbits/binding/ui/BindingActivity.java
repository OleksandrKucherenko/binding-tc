package com.artfulbits.binding.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.artfulbits.binding.BindingsManager;

/** High level activity that implements Binding manager support. */
public abstract class BindingActivity extends AppCompatActivity implements BindingsManager.Lifecycle {
  /** Instance of binding manager. */
  private BindingsManager mBm = BindingsManager.newInstance(this, this);

  /** get instance of the Binding manager. */
  public BindingsManager getBindingsManager() {
    return mBm;
  }

  /** {@inheritDoc} */
  @Override
  protected void onRestart() {
    super.onRestart();

    mBm.doRestart(this); // equal to call of onDestroy()
  }

  /** {@inheritDoc} */
  @Override
  public void onStart() {
    super.onStart();

    // freeze all updates
    mBm.freeze().doStart(this); // call of --> onCreateBinding(...);
  }

  /** {@inheritDoc} */
  @Override
  public void onResume() {
    super.onResume();

    // fragment is ready for updates. UN-freeze can be called several times
    mBm.unfreeze().doResume(this); // call of --> onValidationResult
  }

  /** {@inheritDoc} */
  @Override
  public void onPause() {
    mBm.doPause(this);

    super.onPause();
  }

  /** {@inheritDoc} */
  @Override
  protected void onStop() {
    mBm.doStop(this);

    super.onStop();
  }

  /** {@inheritDoc} */
  @Override
  public void onDestroy() {
    mBm.doDestroy(this);

    // cleanup the binding manager instance
    super.onDestroy();
  }

  /** {@inheritDoc} */
  @Override
  public void onCreateBinding(@NonNull final BindingsManager bm) {
  }

  /** {@inheritDoc} */
  @Override
  public void onValidationResult(@NonNull final BindingsManager bm, boolean success) {
  }
}
