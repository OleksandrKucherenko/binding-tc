package com.artfulbits.sample;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.artfulbits.sample.data.Pin;
import com.artfulbits.sample.robolectric.RobolectricTestHolder;
import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingsManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.ActivityController;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Test {@link com.artfulbits.sample.PinFragment} instance. */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = "src/main/AndroidManifest.xml",
    libraries = {
        "../../build/intermediates/exploded-aar/com.android.support/appcompat-v7/22.2.1"
    })
@RunWith(RobolectricTestRunner.class)
public class PinRobolectricTest extends RobolectricTestHolder {
  @Test
  public void test_00_FullLifeCycle_WithIntent() {
    final Intent intent = MainActivity.showPin(RuntimeEnvironment.application);
    final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).withIntent(intent);
    final MainActivity activity = controller.get();

    assertThat(activity, notNullValue());

    fullLifecycle(controller, new Runnable() {
      @Override
      public void run() {
        final TextView tv = (TextView) activity.findViewById(R.id.tv_feedback);

        // tv_feedback is available only on PinFragment
        assertThat(tv, notNullValue());

        trace("PinFragment found");
      }
    });
  }

  @Test
  public void test_01_EnterPasswords() {
    final Application context = RuntimeEnvironment.application;
    final Intent intent = MainActivity.showPin(context);
    final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).withIntent(intent);
    final MainActivity activity = controller.get();

    assertThat(activity, notNullValue());

    fullLifecycle(controller, new Runnable() {
      int runsCounter = 0;

      @Override
      public void run() {
        final PinFragment fragment = activity.getCurrentFragment();
        final Pin model = fragment.getModel();
        final BindingsManager bm = fragment.getBindingsManager();
        final List<Binder<?, ?>> tagged = bm.getBindingsByTag(R.id.tag_test);
        final EditText et_password = (EditText) activity.findViewById(R.id.et_password);
        final EditText et_confirm = (EditText) activity.findViewById(R.id.et_confirm_password);
        final TextView tv_feedback = (TextView) activity.findViewById(R.id.tv_feedback);

        trace("--> Runs counter: " + runsCounter);

        assertThat(tagged, hasSize(1));
        assertThat(bm.getBindings(), hasSize(3));

        if (runsCounter == 0) {
          // confirm initial state, binding should be executed at least once onResume()
          assertThat(model.getPassword(), nullValue()); // validation do not allow NULL assignment
          assertThat(model.getConfirmPassword(), blankOrNullString()); // confirm password allows empty string
          assertThat(model.getMessage(), equalTo(R.string.msgPasswordPatternFail));
          assertThat(tv_feedback.getText().toString(), equalTo(context.getString(R.string.msgPasswordPatternFail)));
        }

        // do entering of password
        et_password.setText("1234");
        assertThat(model.getPassword(), equalTo("1234"));

        if (runsCounter == 0) {
          // both confirm and password - should be NOT EMPTY
          assertThat(model.getMessage(), equalTo(R.string.msgPasswordPatternFail));
          assertThat(tv_feedback.getText().toString(), equalTo(context.getString(R.string.msgPasswordPatternFail)));
        } else {
          // last state after the 'first run' should be 'PASSWORD OK'
          assertThat(model.getConfirmPassword(), equalTo("1234"));
          assertThat(model.getMessage(), equalTo(R.string.msgPasswordOK));
          assertThat(tv_feedback.getText().toString(), equalTo(context.getString(R.string.msgPasswordOK)));
        }

        // passwords are not matching each other
        et_confirm.setText("12345");
        ShadowApplication.runBackgroundTasks();
        ShadowLooper.runUiThreadTasks(); // binder should process the Handler MSG

        assertThat(model.getConfirmPassword(), equalTo("12345"));
        assertThat(model.getMessage(), equalTo(R.string.msgPasswordFail));
        assertThat(tv_feedback.getText().toString(), equalTo(context.getString(R.string.msgPasswordFail)));

        // finally both passwords are correct
        et_confirm.setText("1234");
        assertThat(model.getConfirmPassword(), equalTo("1234"));
        assertThat(model.getMessage(), equalTo(R.string.msgPasswordOK));
        assertThat(tv_feedback.getText().toString(), equalTo(context.getString(R.string.msgPasswordOK)));

        // this code executed several times, we emulate SHOW/HIDE and RESTART
        trace("<-- Runs counter: " + runsCounter);
        runsCounter++;
      }
    });
  }
}
