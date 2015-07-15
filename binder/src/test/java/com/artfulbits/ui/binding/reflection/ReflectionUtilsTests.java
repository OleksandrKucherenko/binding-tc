package com.artfulbits.ui.binding.reflection;

import android.util.Log;
import android.util.SparseArray;

import com.artfulbits.junit.TestHolder;

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
    assertThat(getRawLogger().toString(), containsString("method:"));

    final Entry field = ReflectionUtils.find(all, "mInteger");
    assertThat(field, notNullValue());
    assertThat(field.getRawType(), instanceOf(Field.class));

    trace(field.toString());
    assertThat(getRawLogger().toString(), containsString("field:"));
  }

  /* [ NESTED DECLARATIONS ] ======================================================================================= */

  public static class Dummy {
    private String mString;

    public String getString() {
      return mString;
    }

    public void setString(final String string) {
      mString = string;
    }
  }

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
}
