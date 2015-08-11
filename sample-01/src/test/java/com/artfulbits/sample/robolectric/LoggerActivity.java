package com.artfulbits.sample.robolectric;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.logging.Level;

/** Special Activity used for tracking lifecycle changes. */
public class LoggerActivity extends AppCompatActivity {
  /* [ MEMBERS ] =================================================================================================== */

  /** Nesting control marker. Increase nesting. */
  private final static String IN = "--> ";
  /** Nesting control marker. Reduce nesting. */
  private final static String OU = "<-- ";

  /** get class simple name. */
  private final String SELF = this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
  /** Standard Output Logger. Helps to save some useful results of tests as a part of execution. */
  private final StringBuilder mLog = new StringBuilder(64 * 1024);
  /** Level of method calls nesting. */
  private int mNestedCalls;

	/* [ LIFECYCLE ] ================================================================================================= */

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    log(Level.INFO, SELF, OU + "onCreate(Bundle)");
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

  /** Log message into activity state logger. */
  private void log(final Level level, final String tag, final String msg) {
    if (msg.startsWith(OU)) mNestedCalls--;

    mLog.append(level.toString().charAt(0)).append(" : ")
        .append(tag).append(" : ")
        .append(new String(new char[mNestedCalls * 2]).replace('\0', ' '))
        .append(msg).append("\r\n");

    if (msg.startsWith(IN)) mNestedCalls++;
  }

  /** Get internal logs storage. */
  @NonNull
  public StringBuilder getRawLogger() {
    return mLog;
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
