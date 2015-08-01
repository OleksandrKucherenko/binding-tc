package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.ui.binding.BindingsManager;

/**
 * Created by alexk on 8/1/2015.
 */
public class BindingFragment extends android.support.v4.app.Fragment implements BindingsManager.Lifecycle {
  /** Instance of binding manager. */
  private BindingsManager mBm = BindingsManager.newInstance(this, this);

  /** get instance of the Binding manager. */
  public BindingsManager getBindingsManager() {
    return mBm;
  }

  @Override
  public void onStart() {
    super.onStart();

    // freeze all updates
    mBm.freeze()
        .doStart(); // call of --> onCreateBinding(...);
  }

  @Override
  public void onResume() {
    super.onResume();

    // fragment is ready for updates
    mBm.unfreeze()
        .doResume(); // call of --> onValidationResult
  }

  @Override
  public void onPause() {
    super.onPause();

    mBm.doPause();
  }

  @Override
  public void onDestroy() {
    mBm.doDestroy();

    super.onDestroy();
  }

  @Override
  public void onCreateBinding(final BindingsManager bm) {

  }

  @Override
  public void onValidationResult(final BindingsManager bm, boolean success) {

  }
}
