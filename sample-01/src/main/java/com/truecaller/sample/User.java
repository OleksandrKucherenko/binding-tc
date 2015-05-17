package com.truecaller.sample;

/**
 * Created by alexk on 5/16/2014.
 */
public class User {
  private String  mLogin;
  private int     mPassword;
  private boolean mStorePassword;
  private boolean mStoreLogin;

  public String getLogin() {
    return mLogin;
  }

  public int getPassword() {
    return mPassword;
  }

  public boolean getStoreLogin() {
    return mStoreLogin;
  }

  public boolean getStorePassword() {
    return mStorePassword;
  }
}
