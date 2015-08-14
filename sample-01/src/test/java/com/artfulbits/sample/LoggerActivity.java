package com.artfulbits.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import java.util.logging.Level;

/**
 * Special Activity used for tracking lifecycle changes.
 *
 * @see <a href="http://ideaventure.blogspot.se/2014/01/android-activityfragment-life-cycle.html">Life Cycle</a>
 */
public class LoggerActivity extends AppCompatActivity implements ILogger {
  /* [ MEMBERS ] =================================================================================================== */

  /** Nesting control marker. Increase nesting. */
  private final static String IN = "--> ";
  /** Nesting control marker. Reduce nesting. */
  private final static String OU = "<-- ";

  /** get class simple name. */
  private final String SELF = this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
  /** Standard Output Logger. Helps to save some useful results of tests as a part of execution. */
  private StringBuilder mLog;
  /** Level of method calls nesting. */
  private int mNestedCalls;
  /** Borrowed logger. */
  private ILogger mLogger;

	/* [ LIFECYCLE ] ================================================================================================= */

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onCreate(Bundle)");
    super.onCreate(savedInstanceState);

    // create a new view for holding
    final LinearLayout view = new LinearLayout(this);
    view.setId(1);
    setContentView(view);

    // logger fragment
    final LoggerFragment fragment = new LoggerFragment().assignLogger(this);
    getSupportFragmentManager().beginTransaction().add(1, fragment, null).commit();

    log(Level.INFO, SELF, OU + "onCreate(Bundle)");
  }

  @Nullable
  @Override
  public View onCreateView(final String name, @NonNull final Context context, @NonNull final AttributeSet attrs) {
    log(Level.INFO, SELF, IN + "onCreateView(String, Context, AttributeSet)");
    final View v = super.onCreateView(name, context, attrs);
    log(Level.INFO, SELF, OU + "onCreateView(String, Context, AttributeSet)");
    return v;
  }

  @Override
  public void onAttachFragment(final Fragment fragment) {
    log(Level.INFO, SELF, IN + "onAttachFragment(Fragment)");
    super.onAttachFragment(fragment);
    log(Level.INFO, SELF, OU + "onAttachFragment(Fragment)");
  }

  @Override
  protected void onStart() {
    log(Level.INFO, SELF, IN + "onStart()");
    super.onStart();
    log(Level.INFO, SELF, OU + "onStart()");
  }

  @Override
  protected void onRestart() {
    log(Level.INFO, SELF, IN + "onRestart()");
    super.onRestart();
    log(Level.INFO, SELF, OU + "onRestart()");
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    log(Level.INFO, SELF, IN + "onActivityResult(int, int, Intent)");
    super.onActivityResult(requestCode, resultCode, data);
    log(Level.INFO, SELF, OU + "onActivityResult(int, int, Intent)");
  }

  @Override
  protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onRestoreInstanceState(Bundle)");
    super.onRestoreInstanceState(savedInstanceState);
    log(Level.INFO, SELF, OU + "onRestoreInstanceState(Bundle)");
  }

  @Override
  protected void onPostCreate(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onPostCreate(Bundle)");
    super.onPostCreate(savedInstanceState);
    log(Level.INFO, SELF, OU + "onPostCreate(Bundle)");
  }

  @Override
  protected void onResume() {
    log(Level.INFO, SELF, IN + "onResume()");
    super.onResume();
    log(Level.INFO, SELF, OU + "onResume()");
  }

  @Override
  protected void onPostResume() {
    log(Level.INFO, SELF, IN + "onPostResume()");
    super.onPostResume();
    log(Level.INFO, SELF, OU + "onPostResume()");
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    log(Level.INFO, SELF, IN + "onCreateOptionsMenu(Menu)");
    final boolean result = super.onCreateOptionsMenu(menu);
    log(Level.INFO, SELF, OU + "onCreateOptionsMenu(Menu)");
    return result;
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    log(Level.INFO, SELF, IN + "onPrepareOptionsMenu(Menu)");
    final boolean result = super.onPrepareOptionsMenu(menu);
    log(Level.INFO, SELF, OU + "onPrepareOptionsMenu(Menu)");
    return result;
  }

  @Override
  public void onUserInteraction() {
    log(Level.INFO, SELF, IN + "onUserInteraction()");
    super.onUserInteraction();
    log(Level.INFO, SELF, OU + "onUserInteraction()");
  }

  @Override
  protected void onUserLeaveHint() {
    log(Level.INFO, SELF, IN + "onUserLeaveHint()");
    super.onUserLeaveHint();
    log(Level.INFO, SELF, OU + "onUserLeaveHint()");
  }

  @Override
  protected void onPause() {
    log(Level.INFO, SELF, IN + "onPause()");
    super.onPause();
    log(Level.INFO, SELF, OU + "onPause()");
  }

  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    log(Level.INFO, SELF, IN + "onSaveInstanceState(Bundle)");
    super.onSaveInstanceState(outState);
    log(Level.INFO, SELF, OU + "onSaveInstanceState(Bundle)");
  }

  @Override
  protected void onStop() {
    log(Level.INFO, SELF, IN + "onStop()");
    super.onStop();
    log(Level.INFO, SELF, OU + "onStop()");
  }

  @Override
  protected void onDestroy() {
    log(Level.INFO, SELF, IN + "onDestroy()");
    super.onDestroy();
    log(Level.INFO, SELF, OU + "onDestroy()");
  }

	/* [ METHODS ] =================================================================================================== */

  /** Get internal logs storage. */
  @NonNull
  public StringBuilder getRawLogger() {
    if (null != mLogger)
      return mLogger.getRawLogger();

    if (null == mLog)
      mLog = new StringBuilder(64 * 1024);

    return mLog;
  }

  /** Log message into activity state logger. */
  public void log(final Level level, final String tag, final String msg) {
    if (null != mLogger) {
      mLogger.log(level, tag, msg);
      return;
    }

    if (msg.startsWith(OU)) mNestedCalls--;

    getRawLogger()
        .append(level.toString().charAt(0)).append(" : ")
        .append(tag).append(" : ")
        .append(new String(new char[mNestedCalls * 2]).replace('\0', ' '))
        .append(msg).append("\r\n");

    if (msg.startsWith(IN)) mNestedCalls++;
  }

  public void setRawLogger(@Nullable final StringBuilder log) {
    mLog = log;
  }

  @NonNull
  public LoggerActivity assignLogger(@Nullable final ILogger logger) {
    mLogger = logger;

    return this;
  }

	/* [ Interface Callback ] ======================================================================================== */

  @Override
  public void onContentChanged() {
    log(Level.INFO, SELF, IN + "onContentChanged()");
    super.onContentChanged();
    log(Level.INFO, SELF, OU + "onContentChanged()");
  }

  @Override
  public void onAttachedToWindow() {
    log(Level.INFO, SELF, IN + "onAttachedToWindow()");
    super.onAttachedToWindow();
    log(Level.INFO, SELF, OU + "onAttachedToWindow()");
  }

  @Override
  public void onDetachedFromWindow() {
    log(Level.INFO, SELF, IN + "onDetachedFromWindow()");
    super.onDetachedFromWindow();
    log(Level.INFO, SELF, OU + "onDetachedFromWindow()");
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  @Override
  public void onBackPressed() {
    log(Level.INFO, SELF, IN + "onBackPressed()");
    super.onBackPressed();
    log(Level.INFO, SELF, OU + "onBackPressed()");
  }

  @Override
  protected void onResumeFragments() {
    log(Level.INFO, SELF, IN + "onResumeFragments()");
    super.onResumeFragments();
    log(Level.INFO, SELF, OU + "onResumeFragments()");
  }
}
