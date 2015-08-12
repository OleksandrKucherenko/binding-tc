package com.artfulbits.sample.robolectric;

import android.os.Build;
import android.support.v4.app.Fragment;

import com.artfulbits.sample.LoggerActivity;
import com.artfulbits.sample.LoggerFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for confirming android lifecycle for activities and fragments. */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = "src/main/AndroidManifest.xml",
    libraries = {
        "../../build/intermediates/exploded-aar/com.android.support/appcompat-v7/22.2.1"
    })
@RunWith(RobolectricTestRunner.class)
public class LifecycleRobolectricTest extends RobolectricTestHolder {
  /**
   * This is full life-cycle of activity based on diagram:
   * https://raw.githubusercontent.com/xxv/android-lifecycle/master/complete_android_fragment_lifecycle.png
   */
  @Test
  public void test_00_LoggerActivity_FullLifecycle() {
    final ActivityController<LoggerActivity> controller = Robolectric.buildActivity(LoggerActivity.class);
    final LoggerActivity activity = controller.get();

    assertThat(activity, notNullValue());

    try {
      fullLifecycle(controller, null);
    } finally {
      // dump states switches
      getRawLogger().append(activity.getRawLogger().toString());
    }
  }

  @Test
  public void test_01_LoggerFragment_FullLifecycle() {
    Fragment fragment = new LoggerFragment();
//    final FragmentManager fm = FragmentTestUtil.buildFragmentManager(LoggerActivity.class);
//    FragmentTestUtil.startFragment(fragment);

  }
}
