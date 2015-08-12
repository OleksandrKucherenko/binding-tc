package com.artfulbits.sample.data;

import android.text.TextUtils;

import com.artfulbits.sample.R;

import java.util.Observable;

/** Pin validation. */
@SuppressWarnings("unused")
public class Pin extends Observable {
  private String mPassword;
  private String mConfirmPassword;

  /** Calculated message based on state of instance. */
  public int getMessage() {
    if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mConfirmPassword))
      return R.string.msgPasswordPatternFail;

    if (mPassword.length() < 4 || mPassword.length() > 6)
      return R.string.msgPasswordPatternFail;

    if (mConfirmPassword.length() < 4 || mConfirmPassword.length() > 6)
      return R.string.msgPasswordPatternFail;

    return mPassword.equals(mConfirmPassword) ?
        R.string.msgPasswordOK :
        R.string.msgPasswordFail;
  }

  public String getConfirmPassword() {
    return mConfirmPassword;
  }

  public void setConfirmPassword(final String confirmPassword) {
    mConfirmPassword = confirmPassword;

    setChanged();
    notifyObservers("ConfirmPassword");
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(final String password) {
    mPassword = password;

    setChanged();
    notifyObservers("Password");
  }
}
