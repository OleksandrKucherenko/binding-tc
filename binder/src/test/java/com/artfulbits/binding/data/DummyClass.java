package com.artfulbits.binding.data;

@SuppressWarnings("unused")
public class DummyClass {
  /* package */ boolean fieldBool;
  /* package */ byte fieldByte;
  /* package */ short fieldSht;
  /* package */ int fieldInt;
  /* package */ long fieldLng;
  /* package */ float fieldFlt;
  /* package */ double fieldDbl;
  /* package */ char fieldChr;
  /* package */ String fieldStr;
  /** enums. */
  /* package */ DummyEnum fieldEnum;
  /** object reference. */
  /* package */ Object fieldObj;
  /** unboxed version of array. */
  /* package */ int[] fieldIntArray;
  /** boxed version of items in array. */
  /* package */ Long[] fieldLongArray;

  private boolean mBoolean;
  private byte mByte;
  private int mInteger;
  private long mLong;
  private float mFloat;
  private double mDouble;
  private char mChar;
  private String mString;
  private DummyEnum mEnum;
  private Object mObject;
  private int[] mInts;
  private Long[] mLongs;

  public DummyClass() {
  }

  public DummyClass(final String msg) {
    this.fieldStr = msg;
  }

  @Override
  public String toString() {
    return this.fieldStr;
  }
}
