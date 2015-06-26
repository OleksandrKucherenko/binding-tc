package com.artfulbits.sample;

/** Typical POJO class. */
public class User {
  /** User name. */
  private String  mLogin;
  /** Pin Code. */
  private int     mPassword;
  /** Persist password in app settings or not. */
  private boolean mStorePassword;
  /** Persist login information in app settings or not.*/
  private boolean mStoreLogin;
  /** Selected initialization group. Variants are: 'Select New Group' or
   * 'Continue from last point'; */
  private int mGroup;
  /** Is 'Select New Group' option chosen? */
  private boolean mSelectNewGroup;
  /** Is 'Continue from Last Point' chosen? */
  private boolean mContinueFromLastPoint;

  public String getLogin() {
    return mLogin;
  }

  public void setLogin(final String login) {
    mLogin = login;
  }

  public int getPassword() {
    return mPassword;
  }

  public void setPassword(final int password) {
    mPassword = password;
  }

  public boolean isStorePassword() {
    return mStorePassword;
  }

  public void setStorePassword(final boolean storePassword) {
    mStorePassword = storePassword;
  }

  public boolean isStoreLogin() {
    return mStoreLogin;
  }

  public void setStoreLogin(final boolean storeLogin) {
    mStoreLogin = storeLogin;
  }

  public int getGroup() {
    return mGroup;
  }

  public void setGroup(final int group) {
    mGroup = group;
  }

  public boolean isSelectNewGroup() {
    return mSelectNewGroup;
  }

  public void setSelectNewGroup(final boolean selectNewGroup) {
    mSelectNewGroup = selectNewGroup;
  }

  public boolean isContinueFromLastPoint() {
    return mContinueFromLastPoint;
  }

  public void setContinueFromLastPoint(final boolean continueFromLastPoint) {
    mContinueFromLastPoint = continueFromLastPoint;
  }
}
