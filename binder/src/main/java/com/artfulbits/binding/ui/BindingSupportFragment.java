package com.artfulbits.binding.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.artfulbits.binding.BindingsManager;

/** Basic implementation of the Fragment with binding library enabled support. */
public abstract class BindingSupportFragment
    extends android.support.v4.app.Fragment
    implements BindingsManager.Lifecycle {
  /** Instance of binding manager. */
  private BindingsManager mBm = BindingsManager.newInstance(this, this);

  /** get instance of the Binding manager. */
  public BindingsManager getBindingsManager() {
    return mBm;
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
  public void onStop() {
    mBm.doStop(this);

    super.onStop();
  }

  /** {@inheritDoc} */
  @Override
  public void onDestroy() {
    mBm.doDestroy(this);

    super.onDestroy();
  }

  /** {@inheritDoc} */
  @Override
  public void onCreateBinding(@NonNull final BindingsManager bm) {
    // reserved for inheritors
  }

  /** {@inheritDoc} */
  @Override
  public void onValidationResult(@Nullable final BindingsManager bm, boolean success) {
    // reserved for inheritors
  }
}
