package com.artfulbits.ui.binding.reflection;

import com.artfulbits.benchmark.Meter;
import com.artfulbits.junit.Sampling;
import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.toolbox.Models;

import org.junit.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Property} class. */
public class PropertySimpleTypesTests extends TestHolder {

  @Test
  public void test_Reflection_vs_Access() throws Exception {
    final Meter m = getMeter();

    m.start("Reflection performance");

    final Property<String> fProperty = Models.text("fieldStr");
    final Property<String> sProperty = Models.text("String");
    final DummyClass instance = new DummyClass();
    instance.fieldStr = "#1";
    instance.setString("#2");

    m.skip("warmup - " + fProperty.get(instance) + ", " + sProperty.get(instance));

    int checks = 0; // dummy code, for dropping optimizations

    for (int j = 0; j < Sampling.ITERATIONS_S; j++) {
      m.loop(Sampling.ITERATIONS_XXL, "Property GET - method");
      for (int i = 0; i < Sampling.ITERATIONS_XXL; i++) {
        final String value = sProperty.get(instance);

        if (null != value && 0 < value.length()) {
          checks++;
        }

        m.recap();
      }
      m.unloop("method");

      m.loop(Sampling.ITERATIONS_XXL, "Property GET - field");
      for (int i = 0; i < Sampling.ITERATIONS_XXL; i++) {
        final String value = fProperty.get(instance);

        if (null != value && 0 < value.length()) {
          checks++;
        }

        m.recap();
      }
      m.unloop("field");

      m.loop(Sampling.ITERATIONS_XXL, "Property GET - direct");
      for (int i = 0; i < Sampling.ITERATIONS_XXL; i++) {
        final String value = instance.getString();

        if (null != value && 0 < value.length()) {
          checks++;
        }

        m.recap();
      }
      m.unloop("direct");
    }

    m.finish("All done! " + checks);
  }

  @Test
  public void test_Properties_Binding_Short() throws Exception {
    final Property<Short> property = Models.from("fieldSht");

    final DummyClass instance = new DummyClass();
    instance.fieldSht = 10;

    assertThat(property.get(instance), equalTo(instance.fieldSht));
  }

  @Test
  public void test_Properties_Binding_Integer() throws Exception {
    final Property<Integer> property = Models.integer("fieldInt");

    final DummyClass instance = new DummyClass();
    instance.fieldInt = 10;

    assertThat(property.get(instance), equalTo(instance.fieldInt));
  }

  @Test
  public void test_Properties_Binding_Long() throws Exception {
    final Property<Long> property = Models.number("fieldLng");

    final DummyClass instance = new DummyClass();
    instance.fieldLng = 10L;

    assertThat(property.get(instance), equalTo(instance.fieldLng));
  }

  @Test
  public void test_Properties_Binding_Boolean() throws Exception {
    final Property<Boolean> property = Models.bool("fieldBool");

    final DummyClass instance = new DummyClass();
    instance.fieldBool = true;

    assertThat(property.get(instance), equalTo(instance.fieldBool));
  }

  @Test
  public void test_Properties_Binding_Float() throws Exception {
    final Property<Float> property = Models.decimal("fieldFlt");

    final DummyClass instance = new DummyClass();
    instance.fieldFlt = 3.1415f;

    assertThat(property.get(instance), equalTo(instance.fieldFlt));
  }

  @Test
  public void test_Properties_Binding_Double() throws Exception {
    final Property<Double> property = Models.real("fieldDbl");

    final DummyClass instance = new DummyClass();
    instance.fieldDbl = 3.1415;

    assertThat(property.get(instance), equalTo(instance.fieldDbl));
  }

  @Test
  public void test_Properties_Binding_Char() throws Exception {
    final Property<Character> property = Models.letter("fieldChr");

    final DummyClass instance = new DummyClass();
    instance.fieldChr = 'A';

    assertThat(property.get(instance), equalTo(instance.fieldChr));
  }

  @Test
  public void test_Properties_Binding_String() throws Exception {
    final Property<String> property = Models.text("fieldStr");

    final DummyClass instance = new DummyClass();
    instance.fieldStr = "test";

    assertThat(property.get(instance), equalTo(instance.fieldStr));
  }

  @Test
  public void test_Properties_Binding_Enum() throws Exception {
    final Property<DummyEnum> property = Models.from("fieldEnum");

    final DummyClass instance = new DummyClass();
    instance.fieldEnum = DummyEnum.Something;

    assertThat(property.get(instance), equalTo(instance.fieldEnum));
  }

  @Test
  public void test_Properties_Binding_Object() throws Exception {
    final Property<Object> property = Models.from("fieldObj");

    final DummyClass instance = new DummyClass();
    instance.fieldObj = this;

    assertThat(property.get(instance), equalTo((Object) this));
  }

  /* [ NESTED DECLARATIONS ] ======================================================================================== */

  public enum DummyEnum {
    Nothing,
    Something;
  }

  public static class DummyClass {
    /* package */ boolean fieldBool;
    /* package */ short fieldSht;
    /* package */ int fieldInt;
    /* package */ long fieldLng;
    /* package */ float fieldFlt;
    /* package */ double fieldDbl;
    /* package */ char fieldChr;
    /* package */ String fieldStr;
    /* package */ DummyEnum fieldEnum;
    /* package */ Object fieldObj;

    private boolean mBoolean;
    private int mInteger;
    private long mLong;
    private float mFloat;
    private double mDouble;
    private char mChar;
    private String mString;
    private DummyEnum mEnum;
    private Object mObject;

    public int getInteger() {
      return mInteger;
    }

    public DummyClass setInteger(final int value) {
      mInteger = value;
      return this;
    }

    public boolean hasTime() {
      return 0 < mLong;
    }

    public boolean exceedsTime() {
      return mLong < System.currentTimeMillis();
    }

    public boolean isBoolean() {
      return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
      mBoolean = aBoolean;
    }

    public double getDouble() {
      return mDouble;
    }

    public void setDouble(double aDouble) {
      mDouble = aDouble;
    }

    public String getString() {
      return mString;
    }

    public void setString(String string) {
      mString = string;
    }

    public long getLong() {
      return mLong;
    }

    public void setLong(long aLong) {
      mLong = aLong;
    }

    public char getChar() {
      return mChar;
    }

    public void setChar(char aChar) {
      mChar = aChar;
    }

    public float getFloat() {
      return mFloat;
    }

    public void setFloat(final float aFloat) {
      mFloat = aFloat;
    }

    public DummyEnum getEnum() {
      return mEnum;
    }

    public void setEnum(final DummyEnum anEnum) {
      mEnum = anEnum;
    }

    public Object getObject() {
      return mObject;
    }

    public void setObject(final Object object) {
      mObject = object;
    }
  }
}
