package com.artfulbits.sample.robolectric;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.artfulbits.sample.ILogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.logging.Level;

/** Generic class for performing robolectric tests. */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public abstract class RobolectricTestHolder implements ILogger {

  //region Members
  /** Executed method name. */
  @Rule
  public TestName mTestName = new TestName();
  /** Class simple name */
  private final String mClassName = this.getClass().getSimpleName();
  /** Standard Output Logger. Helps to save some useful results of tests as a part of execution. */
  private final StringBuilder mLog = new StringBuilder(64 * 1024).append("\r\n");
  //endregion

  //region --> Standard Output

  /** Get access to the logs memory storage directly. */
  @NonNull
  public StringBuilder getRawLogger() {
    return mLog;
  }

  @Override
  public void setRawLogger(@NonNull final StringBuilder logger) {
    throw new AssertionError("Not allowed!");
  }

  /** {@inheritDoc} */
  public void log(final Level level, final String tag, final String msg) {
    mLog.append(level.toString().charAt(0)).append(" : ")
        .append(tag).append(" : ")
        .append(msg).append("\r\n");
  }

  /** Put message into system output. */
  public void trace(final String msg) {
    log(Level.INFO, "--", msg);
  }
  //endregion

  //region --> Setup/TearDown Log method name
  @Before
  public final void setUp() throws Exception {
    log(Level.INFO, "->", mClassName + "." + mTestName.getMethodName());

    onSetUp();
  }

  @After
  public final void tearDown() throws Exception {
    onTearDown();

    log(Level.INFO, "<-", mClassName + "." + mTestName.getMethodName());

    // all collected output lines dump into Standard Output
    System.out.append(mLog.toString());
  }
  //endregion

  //region --> Setup/TearDown Overrides

  public void onSetUp() {
    // do nothing, leave for inheritors
  }

  public void onTearDown() {
    // do nothing, leave for inheritors
  }

  //endregion

  //region --> Activity Lifecycle emulation

  /**
   * Perform full lifecycle emulation for activity. When Activity is in visible state
   * is possible to execute some additional actions.
   */
  public <T extends Activity> void fullLifecycle(@NonNull final ActivityController<T> controller,
                                                 @Nullable final Runnable onVisible) {
    fullLifecycle(controller, null, null, onVisible);
  }


  /**
   * Perform full lifecycle emulation for activity. When Activity is in visible state
   * is possible to execute some additional actions.
   */
  public <T extends Activity> void fullLifecycle(@NonNull final ActivityController<T> controller,
                                                 @Nullable final Runnable onRestart,
                                                 @Nullable final Runnable onResume,
                                                 @Nullable final Runnable onVisible) {
    final Bundle savedInstanceState = new Bundle();

    trace("state - onCreate");
    controller.create();

    // CYCLE #1: emulate activity restart
    int lifeLoops = 1;
    do {
      trace("state - onStart : " + lifeLoops);
      controller.start();

      controller.restoreInstanceState(savedInstanceState);

      controller.postCreate(savedInstanceState);

      // CYCLE #1.1: emulate show/hide
      int loops = 1;
      do {
        trace("state - onResume : " + loops);
        if (null != onResume) onResume.run();

        controller.resume(); // --> onPostResume()

        controller.visible(); // --> onUserInteraction()

        if (null != onVisible) onVisible.run();

        controller.userLeaving();

        controller.pause();

        loops--;
      } while (loops >= 0);

      controller.saveInstanceState(savedInstanceState);

      controller.stop();

      // go-to onRestart() state
      if (lifeLoops > 0) {
        if (null != onRestart) onRestart.run();

        trace("state - onRestart");
        controller.restart();
      }

      lifeLoops--;
    } while (lifeLoops >= 0);

    trace("state - onDestroy");
    controller.destroy();
  }
  //endregion
}
