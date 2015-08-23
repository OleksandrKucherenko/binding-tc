package com.artfulbits.sample;

import android.os.Build;

import com.artfulbits.binding.BuildConfig;
import com.artfulbits.sample.robolectric.RobolectricTestHolder;

import org.junit.*;
import org.junit.runner.*;
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
        BuildConfig.ROBOLECTRIC_APPCOMPAT
    })
@RunWith(RobolectricTestRunner.class)
public class LoginFillRobolectricTest extends RobolectricTestHolder {

  @Test
  public void test_00_MainActivity_FullLifecycle() {
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
