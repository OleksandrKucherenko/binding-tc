package com.artfulbits.sample;

import android.os.Build;

import com.artfulbits.sample.robolectric.LoggerActivity;
import com.artfulbits.sample.robolectric.RobolectricTestHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** First robolectric test. */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = "src/main/AndroidManifest.xml",
    libraries = {
        "../../build/intermediates/exploded-aar/com.android.support/appcompat-v7/22.2.1"
    })
@RunWith(RobolectricTestRunner.class)
public class LoginFillRobolectricTest extends RobolectricTestHolder {

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
  public void test_01_MainActivity_FullLifecycle() {
    final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
    final MainActivity activity = controller.get();

    assertThat(activity, notNullValue());

    fullLifecycle(controller, new Runnable() {
      @Override
      public void run() {
        // binding manager instance should be available
        assertThat(activity.getBindingsManager(), notNullValue());

        // main activity does not have any bindings
        assertThat(activity.getBindingsManager().getBindings(), hasSize(0));
      }
    });
  }
}
