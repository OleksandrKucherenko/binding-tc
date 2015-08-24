package com.artfulbits.binding.reflection;

import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.artfulbits.junit.TestHolder;

import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests of the {@link ReflectionUtils} class. */
public class ReflectionUtilsTests extends TestHolder {
  /* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  @Test
  public void test_00_ExtractFields() {
    // generic class
    final List<Field> fields = ReflectionUtils.getAllFields(SparseArray.class);
    assertThat(fields, hasSize(greaterThan(0)));

    // static class
    final List<Field> fields1 = ReflectionUtils.getAllFields(Log.class);
    assertThat(fields1, hasSize(6)); // constants quantity

    // inheritance
    final List<Field> fields2 = ReflectionUtils.getAllFields(First.class);
    assertThat(fields2, hasSize(2));

    // find by name
    final Field field = ReflectionUtils.findField(fields2, "mString");
    assertThat(field, notNullValue());
    assertThat(field.getName(), equalTo("mString"));
  }

  @Test
  public void test_01_ExtractMethods() {
    final List<Method> methods = ReflectionUtils.getAllMethods(Dummy.class);
    assertThat(methods, hasSize(2));

    final List<Method> methods1 = ReflectionUtils.getAllMethods(First.class);
    assertThat(methods1, hasSize(5));

    final Method method = ReflectionUtils.findMethod(methods1, "setString");
    assertThat(method, notNullValue());
    assertThat(method.getName(), equalTo("setString"));
  }

  @Test
  public void test_02_ExtractEntries() {
    final List<Entry> all = ReflectionUtils.getAll(First.class);
    assertThat(all, hasSize(greaterThanOrEqualTo(7)));

    final Entry method = ReflectionUtils.find(all, "setString");
    assertThat(method, notNullValue());
    assertThat(method.getRawType(), instanceOf(Method.class));

    trace(method.toString());
    assertThat(getRawLogger().toString(), containsString("setString(...)"));

    final Entry field = ReflectionUtils.find(all, "mInteger");
    assertThat(field, notNullValue());
    assertThat(field.getRawType(), instanceOf(Field.class));

    trace(field.toString());
    assertThat(getRawLogger().toString(), containsString("mInteger"));
  }

  @Test
  public void test_03_FindOverloadedMethod() {
    final List<Entry> all = ReflectionUtils.getAll(Overloads.class);
    assertThat(all, hasSize(7));

    final Entry method = ReflectionUtils.find(all, "setText");
    assertThat(method, notNullValue());
    assertThat(method.getRawType(), instanceOf(Method.class));

    final int index = all.indexOf(method);
    assertThat(index, greaterThanOrEqualTo(0));

    trace("Found index: " + index);

    for (int i = 0; i < all.size(); i++) {
      final Entry e = all.get(i);
      trace(e.getFullName());
    }

    final Method m = (Method) method.getRawType();
    assertThat(m.getParameterTypes(), arrayWithSize(0));

    final Entry exactString = ReflectionUtils.match(all, method, String.class);

    assertThat(exactString, notNullValue());
    assertThat(exactString.getRawType(), instanceOf(Method.class));

    final Class<?>[] parameterTypes = ((Method) exactString.getRawType()).getParameterTypes();
    assertThat(parameterTypes, arrayWithSize(1));
    assertTrue(parameterTypes[0].equals(CharSequence.class));

    // first item in ALL list, method is already pointing on it
    final Entry exactFirst = ReflectionUtils.match(all, method);
    assertThat(exactFirst, notNullValue());

    final Entry exactInt = ReflectionUtils.match(all, method, int.class);
    assertThat(exactInt, notNullValue());

    final Entry exactArr = ReflectionUtils.match(all, method, char[].class, int.class, int.class);
    assertThat(exactArr, notNullValue());

    // last method in a sequence
    final Entry exactLast = ReflectionUtils.match(all, method, CharSequence.class,
        TextView.BufferType.class, boolean.class, int.class);
    assertThat(exactLast, notNullValue());

    trace("confirm findings...");
    trace("Found: " + exactFirst.getFullName());
    trace("Found: " + exactString.getFullName());
    trace("Found: " + exactInt.getFullName());
    trace("Found: " + exactArr.getFullName());
    trace("Found: " + exactLast.getFullName());
  }

  @Test
  @Ignore
  public void test_04_ReverseSearch() {
    // TODO: implement search of method/field in string array
    // SearchByFieldNameComparator, SearchByExecutableNameComparator, SearchByMethodNameComparator
  }

  @Test
  public void test_05_BoxingTypes() {
    assertTrue(ReflectionUtils.boxing(boolean.class).equals(Boolean.class));
    assertTrue(ReflectionUtils.boxing(char.class).equals(Character.class));
    assertTrue(ReflectionUtils.boxing(byte.class).equals(Byte.class));
    assertTrue(ReflectionUtils.boxing(short.class).equals(Short.class));
    assertTrue(ReflectionUtils.boxing(int.class).equals(Integer.class));
    assertTrue(ReflectionUtils.boxing(long.class).equals(Long.class));
    assertTrue(ReflectionUtils.boxing(float.class).equals(Float.class));
    assertTrue(ReflectionUtils.boxing(double.class).equals(Double.class));
  }

  @Test
  public void test_06_ToTypes() {
    final Class<?>[] types = ReflectionUtils.toTypes("Something", 1, 1L,
        Long.valueOf(10L), new Double(5.0f), (String) null);

    for (Class<?> c : types) {
      trace((null == c) ? "<null>" : c.toString());
    }

    assertThat(types, arrayWithSize(6));

    // during sending the parameters they are automatically BOXED, that is why
    // should expect only expect non-primitive types
    assertTrue(types[0].equals(String.class));
    assertTrue(types[1].equals(Integer.class));
    assertTrue(types[2].equals(Long.class));
    assertTrue(types[3].equals(Long.class));
    assertTrue(types[4].equals(Double.class));
    assertThat(types[5], nullValue());
  }

  /* [ NESTED DECLARATIONS ] ======================================================================================= */

  @SuppressWarnings("unused")
  public static class Dummy {
    private String mString;

    public String getString() {
      return mString;
    }

    public void setString(final String string) {
      mString = string;
    }
  }

  @SuppressWarnings("unused")
  public static class First extends Dummy {
    private int mInteger;

    public int getInteger() {
      return mInteger;
    }

    public void setInteger(final int integer) {
      mInteger = integer;
    }

    public First setString(final int integer) {
      setString(String.valueOf(integer));
      return this;
    }
  }

  @SuppressWarnings("unused")
  public static class DummyOverloads {

    /** NO parameters. */
    public final void setText() {
    }

    public final void setText(int resid) {
    }
  }

  @SuppressWarnings("unused")
  public static class Overloads extends DummyOverloads {
    public void setText(CharSequence text, TextView.BufferType type) {
    }

    private void setText(CharSequence text, TextView.BufferType type, boolean notifyBefore, int oldlen) {
    }

    public final void setText(CharSequence text) {
    }

    public final void setText(char[] text, int start, int len) {
    }

    public final void setText(int resid, TextView.BufferType type) {
    }
  }
}
