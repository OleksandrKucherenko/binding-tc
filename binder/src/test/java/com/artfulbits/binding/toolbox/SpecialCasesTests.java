package com.artfulbits.binding.toolbox;

import com.artfulbits.binding.Selector;
import com.artfulbits.binding.reflection.Entry;
import com.artfulbits.binding.reflection.Property;
import com.artfulbits.binding.reflection.ReflectionUtils;
import com.artfulbits.junit.TestHolder;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Ridges} class and issue it resolve. */
public class SpecialCasesTests extends TestHolder {
  @Test
  public void test_00_SharedReferenceProblem() {
    final BoxedFields left = new BoxedFields();
    final BoxedFields inner = new BoxedFields();

    final Property<Long> boxed = Models.number("mLongBoxed");
    final Property<Long> unboxed = Models.number("mLongUnBoxed");
    final Property<Object> destination = Models.from("mDestination");

    final Selector<BoxedFields, Long> boxedPojo = Models.pojo(left, boxed);
    final Selector<BoxedFields, Object> dstPojo = Models.pojo(left, destination);
    final Selector<BoxedFields, Long> unboxedPojo = Models.pojo(left, unboxed);

    // trace binding for verification
    trace(boxed.toString());
    trace(unboxed.toString());
    trace(destination.toString());

    // force resolving
    boxed.resolve(left);
    unboxed.resolve(left);
    destination.resolve(inner);

    // trace binding for verification
    trace(boxed.toString());
    trace(unboxed.toString());
    trace(destination.toString());

    Long lValue = new Long(1L);
    boxedPojo.set(lValue);
    unboxedPojo.set(lValue);
    dstPojo.set(inner);

    // modify our objects
    lValue++;
    inner.mLongBoxed = 5L;

    // check that we have expected behavior
    assertThat(left.mLongBoxed, equalTo(1L));
    assertThat(left.mLongUnBoxed, equalTo(1L));
    assertThat(((BoxedFields) left.mDestination).mLongBoxed, equalTo(5L));

    // expected:
    // 1) primitive types boxed/unboxed assigned "by copy"
    // 2) high level objects assigned "by reference"
  }

  @Test
  public void test_01_ReflectionSort() throws Exception {
    final List<Entry> all = ReflectionUtils.getAll(BoxedFields.class);

    for (Entry e : all) {
      trace(e.getName() + ", " + e.getFullName());
    }

    // check that sorting done correctly
    final String[] lines = getRawLogger().toString().split(NEW_LINE);
    assertThat(lines[2], containsString("mDestination()"));
    assertThat(lines[3], containsString("mDestination(class java.lang.Object)"));
    assertThat(lines[4], containsString("class java.lang.Object mDestination"));
    assertThat(lines[5], containsString("mDestination(class java.lang.Object, boolean)"));
  }

  public static class BoxedFields {
    public Long mLongBoxed;
    public Object mDestination;
    public long mLongUnBoxed;

    /** Method and field has the same name. In this case we prefer methods. */
    public void mDestination() {
    }

    /** Method and field has the same name. In this case we prefer methods. */
    public void mDestination(final Object value) {
      this.mDestination = value;
    }

    /**
     * Method and field has the same name, but this method has more than 1 parameter.
     * In this case we prefer field to method.
     */
    public void mDestination(final Object value, final boolean test) {
      if (test) {
        mDestination = value;
      }
    }
  }
}
