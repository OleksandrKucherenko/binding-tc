package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.reflection.Property;

import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Models} class. */
@SuppressWarnings("unused")
public class ModelsTests extends TestHolder {
  @Test
  public void test_00_Properties_TwoParams_Get() {
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

  @Test
  public void test_01_Properties_OneParameter_Get() {
    final Property<Boolean> propertyBoolean1 = Models.bool("getget");
    final Property<Byte> propertyByte1 = Models.bytes("getget");
    final Property<Short> propertyShort1 = Models.shorts("getget");
    final Property<Integer> propertyInteger1 = Models.integer("getget");
    final Property<Long> propertyLong1 = Models.number("getget");
    final Property<Float> propertyFloat1 = Models.decimal("getget");
    final Property<Double> propertyDouble1 = Models.real("getget");
    final Property<Character> propertyChar1 = Models.letter("getget");
    final Property<String> propertyString1 = Models.strings("getget");
    final Property<DummyEnum> propertyEnum1 = Models.from("getget");
    final Property<Object> propertyObject1 = Models.from("getget");

    final DummyClass instance = new DummyClass();

    assertThat(propertyBoolean1.get(instance), equalTo(false));
    assertThat(propertyByte1.get(instance), equalTo((byte) 0));
    assertThat(propertyShort1.get(instance), equalTo((short) 0));
    assertThat(propertyInteger1.get(instance), equalTo(0));
    assertThat(propertyLong1.get(instance), equalTo(0L));
    assertThat(propertyFloat1.get(instance), equalTo(0.0f));
    assertThat(propertyDouble1.get(instance), equalTo(0.0));
    assertThat(propertyChar1.get(instance), equalTo('\0'));
    assertThat(propertyString1.get(instance), nullValue());
    assertThat(propertyEnum1.get(instance), nullValue());
    assertThat(propertyObject1.get(instance), nullValue());
  }

  @Test
  public void test_02_Properties_TwoParams_Set() {
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

    // expected NO EXCEPTIONS
    propertyBoolean.set(instance, true);
    propertyByte.set(instance, (byte) 1);
    propertyShort.set(instance, (short) 1);
    propertyInteger.set(instance, 1);
    propertyLong.set(instance, 1L);
    propertyFloat.set(instance, 1.1f);
    propertyDouble.set(instance, 1.1);
    propertyChar.set(instance, '\1');
    propertyString.set(instance, "test");
    propertyEnum.set(instance, DummyEnum.Something);
    propertyObject.set(instance, new Object());
  }

  @Test
  public void test_03_Properties_OneParameter_Set() {
    final Property<Boolean> propertyBoolean = Models.bool("getget");
    final Property<Byte> propertyByte = Models.bytes("getget");
    final Property<Short> propertyShort = Models.shorts("getget");
    final Property<Integer> propertyInteger = Models.integer("getget");
    final Property<Long> propertyLong = Models.number("getget");
    final Property<Float> propertyFloat = Models.decimal("getget");
    final Property<Double> propertyDouble = Models.real("getget");
    final Property<Character> propertyChar = Models.letter("getget");
    final Property<String> propertyString = Models.strings("getget");
    final Property<DummyEnum> propertyEnum = Models.from("getget");
    final Property<Object> propertyObject = Models.from("getget");

    final DummyClass instance = new DummyClass();

    // expected NO EXCEPTIONS
    propertyBoolean.set(instance, true);
    propertyByte.set(instance, (byte) 1);
    propertyShort.set(instance, (short) 1);
    propertyInteger.set(instance, 1);
    propertyLong.set(instance, 1L);
    propertyFloat.set(instance, 1.1f);
    propertyDouble.set(instance, 1.1);
    propertyChar.set(instance, '\1');
    propertyString.set(instance, "test");
    propertyEnum.set(instance, DummyEnum.Something);
    propertyObject.set(instance, new Object());
  }

  @Test
  public void test_04_Mapping() throws Exception {
    final Map<String, Object> json = new HashMap<>();
    json.put("something", true);

    final Selector<?, Boolean> selector = Models.map(json, "something");

    assertThat(selector.get(), equalTo(true));
  }

  @Test
  public void test_05_ArrayList() throws Exception {
    final String[] values = new String[]{"something"};

    final Selector<?, String> selector = Models.index(values, 0);

    assertThat(selector.get(), equalTo("something"));
  }

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
  }
}
