package com.artfulbits.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Level;

/** Special Fragment for logging fragment states. */
public class LoggerFragment extends Fragment {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Nesting control marker. Increase nesting. */
  private final static String IN = "--> ";
  /** Nesting control marker. Reduce nesting. */
  private final static String OU = "<-- ";

	/* [ MEMBERS ] =================================================================================================== */

  /** get class simple name. */
  private final String SELF = this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
  /** Standard Output Logger. Helps to save some useful results of tests as a part of execution. */
  private final StringBuilder mLog = new StringBuilder(64 * 1024);
  /** Level of method calls nesting. */
  private int mNestedCalls;

	/* [ GETTER / SETTER METHODS ] =================================================================================== */

  /** Get internal logs storage. */
  @NonNull
  public StringBuilder getRawLogger() {
    return mLog;
  }

	/* [ LIFECYCLE ] ================================================================================================= */

  @Override
  public void onAttach(final Activity activity) {
    log(Level.INFO, SELF, IN + "onAttach(Activity)");
    super.onAttach(activity);
    log(Level.INFO, SELF, OU + "onAttach(Activity)");
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    log(Level.INFO, SELF, OU + "onCreate(Bundle)");
  }

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onCreateView(...)");
    final View v = super.onCreateView(inflater, container, savedInstanceState);
    log(Level.INFO, SELF, OU + "onCreateView(...)");
    return v;
  }

  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onViewCreated(View, Bundle)");
    super.onViewCreated(view, savedInstanceState);
    log(Level.INFO, SELF, OU + "onViewCreated(View, Bundle)");
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);
    log(Level.INFO, SELF, OU + "onActivityCreated(Bundle)");
  }

  @Override
  public void onViewStateRestored(final Bundle savedInstanceState) {
    log(Level.INFO, SELF, IN + "onViewStateRestored(Bundle)");
    super.onViewStateRestored(savedInstanceState);
    log(Level.INFO, SELF, OU + "onViewStateRestored(Bundle)");
  }

  @Override
  public void onStart() {
    log(Level.INFO, SELF, IN + "onStart()");
    super.onStart();
    log(Level.INFO, SELF, OU + "onStart()");
  }

  @Override
  public void onResume() {
    log(Level.INFO, SELF, IN + "onResume()");
    super.onResume();
    log(Level.INFO, SELF, OU + "onResume()");
  }

  @Override
  public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
    log(Level.INFO, SELF, IN + "onCreateOptionsMenu(Menu, MenuInflater)");
    super.onCreateOptionsMenu(menu, inflater);
    log(Level.INFO, SELF, OU + "onCreateOptionsMenu(Menu, MenuInflater)");
  }

  @Override
  public void onPrepareOptionsMenu(final Menu menu) {
    log(Level.INFO, SELF, IN + "onPrepareOptionsMenu(Menu)");
    super.onPrepareOptionsMenu(menu);
    log(Level.INFO, SELF, OU + "onPrepareOptionsMenu(Menu)");
  }

  @Override
  public void onPause() {
    log(Level.INFO, SELF, IN + "onPause()");
    super.onPause();
    log(Level.INFO, SELF, OU + "onPause()");
  }

  @Override
  public void onSaveInstanceState(final Bundle outState) {
    log(Level.INFO, SELF, IN + "onSaveInstanceState(Bundle)");
    super.onSaveInstanceState(outState);
    log(Level.INFO, SELF, OU + "onSaveInstanceState(Bundle)");
  }

  @Override
  public void onStop() {
    log(Level.INFO, SELF, IN + "onStop()");
    super.onStop();
    log(Level.INFO, SELF, OU + "onStop()");
  }

  @Override
  public void onDestroyView() {
    log(Level.INFO, SELF, IN + "onDestroyView()");
    super.onDestroyView();
    log(Level.INFO, SELF, OU + "onDestroyView()");
  }

  @Override
  public void onDestroy() {
    log(Level.INFO, SELF, IN + "onDestroy()");
    super.onDestroy();
    log(Level.INFO, SELF, OU + "onDestroy()");
  }

  @Override
  public void onDetach() {
    log(Level.INFO, SELF, IN + "onDetach()");
    super.onDetach();
    log(Level.INFO, SELF, OU + "onDetach()");
  }

	/* [ METHODS ] =================================================================================================== */

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  /** Log message into activity state logger. */
  private void log(final Level level, final String tag, final String msg) {
    if (msg.startsWith(OU)) mNestedCalls--;

    mLog.append(level.toString().charAt(0)).append(" : ")
        .append(tag).append(" : ")
        .append(new String(new char[mNestedCalls * 2]).replace('\0', ' '))
        .append(msg).append("\r\n");

    if (msg.startsWith(IN)) mNestedCalls++;
  }
}
