package com.artfulbits.ui.binding.reflection;

import com.artfulbits.benchmark.Meter;
import com.artfulbits.junit.Sampling;
import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.exceptions.ConfigurationError;
import com.artfulbits.ui.binding.toolbox.Models;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Property} class. */
public class PropertySimpleTypesTests extends TestHolder {

  @Test
  public void test_00_Reflection_vs_Access() {
    final Meter m = getMeter();

    m.start("Reflection performance");

    final Property<String> fProperty = Models.strings("fieldStr");
    final Property<String> sProperty = Models.strings("String");
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
  public void test_01_Properties_Binding_Short() {
    final Property<Short> property = Models.from("fieldSht");

    final DummyClass instance = new DummyClass();
    instance.fieldSht = 10;

    assertThat(property.get(instance), equalTo(instance.fieldSht));
  }

  @Test
  public void test_02_Properties_Binding_Integer() {
    final Property<Integer> property = Models.integer("fieldInt");

    final DummyClass instance = new DummyClass();
    instance.fieldInt = 10;

    assertThat(property.get(instance), equalTo(instance.fieldInt));
  }

  @Test
  public void test_03_Properties_Binding_Long() {
    final Property<Long> property = Models.number("fieldLng");

    final DummyClass instance = new DummyClass();
    instance.fieldLng = 10L;

    assertThat(property.get(instance), equalTo(instance.fieldLng));
  }

  @Test
  public void test_04_Properties_Binding_Boolean() {
    final Property<Boolean> property = Models.bool("fieldBool");

    final DummyClass instance = new DummyClass();
    instance.fieldBool = true;

    assertThat(property.get(instance), equalTo(instance.fieldBool));
  }

  @Test
  public void test_05_Properties_Binding_Float() {
    final Property<Float> property = Models.decimal("fieldFlt");

    final DummyClass instance = new DummyClass();
    instance.fieldFlt = 3.1415f;

    assertThat(property.get(instance), equalTo(instance.fieldFlt));
  }

  @Test
  public void test_06_Properties_Binding_Double() {
    final Property<Double> property = Models.real("fieldDbl");

    final DummyClass instance = new DummyClass();
    instance.fieldDbl = 3.1415;

    assertThat(property.get(instance), equalTo(instance.fieldDbl));
  }

  @Test
  public void test_07_Properties_Binding_Char() {
    final Property<Character> property = Models.letter("fieldChr");

    final DummyClass instance = new DummyClass();
    instance.fieldChr = 'A';

    assertThat(property.get(instance), equalTo(instance.fieldChr));
  }

  @Test
  public void test_08_Properties_Binding_String() {
    final Property<String> property = Models.strings("fieldStr");

    final DummyClass instance = new DummyClass();
    instance.fieldStr = "test";

    assertThat(property.get(instance), equalTo(instance.fieldStr));
  }

  @Test
  public void test_09_Properties_Binding_Enum() {
    final Property<DummyEnum> property = Models.from("fieldEnum");

    final DummyClass instance = new DummyClass();
    instance.fieldEnum = DummyEnum.Something;

    assertThat(property.get(instance), equalTo(instance.fieldEnum));
  }

  @Test
  public void test_10_Properties_Binding_Object() {
    final Property<Object> property = Models.from("fieldObj");

    final DummyClass instance = new DummyClass();
    instance.fieldObj = this;

    assertThat(property.get(instance), equalTo((Object) this));
  }

  @Test
  public void test_11_Properties_Defaults() {
    // create properties with names that cannot be resolved
    final Property<Boolean> propertyBoolean = Models.bool("getget", "setset");
    final Property<Byte> propertyByte = Models.bytes("getget", "setset");
    final Property<Short> propertyShort = Models.shorts("getget", "setset");
    final Property<Integer> propertyInteger = Models.integer("getget", "setset");
    final Property<Long> propertyLong = Models.number("getget", "setset");
    final Property<Float> propertyFloat = Models.decimal("getget", "setset");
    final Property<Double> propertyDouble = Models.real("getget", "setset");
    final Property<Character> propertyChar = Models.letter("getget", "setset");
    final Property<String> propertyString = Models.strings("getget", "setset");
    final Property<DummyEnum> propertyEnum = Models.from("getget", "setset");
    final Property<Object> propertyObject = Models.from("getget", "setset");

    final DummyClass instance = new DummyClass();
    assertThat(propertyBoolean.get(instance), equalTo(false));
    assertThat(propertyByte.get(instance), equalTo((byte) 0));
    assertThat(propertyShort.get(instance), equalTo((short) 0));
    assertThat(propertyInteger.get(instance), equalTo(0));
    assertThat(propertyLong.get(instance), equalTo(0L));
    assertThat(propertyFloat.get(instance), equalTo(0.0f));
    assertThat(propertyDouble.get(instance), equalTo(0.0));
    assertThat(propertyChar.get(instance), equalTo('\0'));
    assertThat(propertyString.get(instance), nullValue());
    assertThat(propertyEnum.get(instance), nullValue());
    assertThat(propertyObject.get(instance), nullValue());
  }

  @Test(expected = ConfigurationError.class)
  public void test_12_Properties_NoGet() {
    final DummyClass instance = new DummyClass();
    final Property<Boolean> propertyBoolean = Models.bool("getget", "fieldBool");

    assertThat(propertyBoolean.get(instance), equalTo(false));

    // expected exception!
    propertyBoolean.resolve(instance);
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
