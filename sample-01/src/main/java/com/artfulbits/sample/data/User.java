package com.artfulbits.sample.data;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

/** Typical POJO class. */
@SuppressWarnings("unused")
public class User extends Observable {
  /** User name. */
  private String mLogin;
  /** Pin Code. */
  private int mPin;
  /** Pin Code for confirmation. */
  private int mConfirmPin;
  /** Persist password in app settings or not. */
  private boolean mStorePassword;
  /** Persist login information in app settings or not. */
  private boolean mStoreLogin;
  /** Selected group index. */
  private int mGroupSpin;
  /**
   * Selected initialization group. Variants are: 'Select New Group' or 'Continue from last point';
   */
  private int mGroup;
  /** Is 'Select New Group' option chosen? */
  private boolean mSelectNewGroup;
  /** Is 'Continue from Last Point' chosen? */
  private boolean mContinueFromLastPoint;
  /** Active time in seconds. */
  private long mStartTime = System.currentTimeMillis();

  public int getGroupSpin() {
    return mGroupSpin;
  }

  public void setGroupSpin(final int groupSpin) {
    mGroupSpin = groupSpin;
  }

  public String getLogin() {
    return mLogin;
  }

  public void setLogin(final String login) {
    mLogin = login;
  }

  public int getPin() {
    return mPin;
  }

  public void setPin(final int pin) {
    mPin = pin;

    setChanged();
    notifyObservers("Pin");
  }

  public int getConfirmPin() {
    return mConfirmPin;
  }

  public void setConfirmPin(final int confirmPin) {
    mConfirmPin = confirmPin;

    setChanged();
    notifyObservers("ConfirmPin");

    if (mConfirmPin == mPin && mPin != 0)
      notifyObservers("PinsEqual");
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

  public int getActiveTime() {
    return (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - mStartTime);
  }
}
