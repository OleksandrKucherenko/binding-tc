package com.artfulbits.binding.toolbox;

import com.artfulbits.binding.Formatting;
import com.artfulbits.binding.exceptions.OneWayBindingError;
import com.artfulbits.junit.TestHolder;

import org.junit.Test;

import static com.artfulbits.binding.toolbox.Molds.chain;
import static com.artfulbits.binding.toolbox.Molds.fromCharsToInteger;
import static com.artfulbits.binding.toolbox.Molds.fromIntegerToBoolean;
import static com.artfulbits.binding.toolbox.Molds.fromStringToChars;
import static com.artfulbits.binding.toolbox.Molds.fromStringToNumber;
import static com.artfulbits.binding.toolbox.Molds.onlyPop;
import static com.artfulbits.binding.toolbox.Molds.onlyPush;
import static com.artfulbits.binding.toolbox.Molds.reverse;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Molds} class. */
public class MoldsTests extends TestHolder {

  @Test
  public void test_00_direct() {
    final Formatting<String, String> direct = Molds.direct();
    final Formatting<Integer, Number> digits = Molds.direct();

    assertThat(direct.toModel("test"), equalTo("test"));
    assertThat(direct.toView("test2"), equalTo("test2"));

    assertThat(digits.toModel(10), equalTo((Number) Integer.valueOf(10)));
    assertThat(digits.toView(Integer.valueOf(11)), equalTo(11));
  }

  @Test
  public void test_01_reverse() {
    final Formatting<String, Integer> convert = chain(fromStringToChars(), fromCharsToInteger());
    final Formatting<Integer, String> reverse = reverse(convert);

    assertThat(reverse.toModel(10), equalTo("10"));
    assertThat(reverse.toView("10"), equalTo(10));

    assertThat(convert.toModel("10"), equalTo(10));
    assertThat(convert.toView(10), equalTo("10"));

    assertThat(reverse.toView(convert.toView(10)), equalTo(10));
    assertThat(reverse.toModel(convert.toModel("10")), equalTo("10"));
  }

  @Test(expected = OneWayBindingError.class)
  public void test_02_onlyPop() {
    final Formatting<String, Integer> convert = chain(fromStringToChars(), fromCharsToInteger());
    final Formatting<String, Integer> onlyPop = onlyPop(convert);

    // should PASS!
    assertThat(onlyPop.toView(10), equalTo("10"));

    trace("toView() PASSED!");

    // will work only from model to View, line should raise exception
    assertThat(onlyPop.toModel("10"), equalTo(11));

    fail("exception expected!");
  }

  @Test(expected = OneWayBindingError.class)
  public void test_03_onlyPop_Partial() {
    final Formatting<String, Integer> convert = chain(fromStringToChars(), fromCharsToInteger());
    final Formatting<String, Integer> onlyPop = onlyPop(new ToView<String, Integer>() {
      @Override
      public String toView(final Integer value) {
        return convert.toView(value);
      }
    });

    // should PASS!
    assertThat(onlyPop.toView(10), equalTo("10"));

    trace("toView() PASSED!");

    // will work only from model to View, line should raise exception
    assertThat(onlyPop.toModel("10"), equalTo(11));

    fail("exception expected!");
  }

  @Test(expected = OneWayBindingError.class)
  public void test_04_onlyPush() {
    final Formatting<String, Integer> convert = chain(fromStringToChars(), fromCharsToInteger());
    final Formatting<String, Integer> onlyPush = onlyPush(convert);

    // should Pass
    assertThat(onlyPush.toModel("10"), equalTo(10));

    // will work only from model to View, line should raise exception
    assertThat(onlyPush.toView(10), equalTo("10"));

    fail("exception expected!");
  }

  @Test(expected = OneWayBindingError.class)
  public void test_05_onlyPush_Partial() {
    final Formatting<String, Integer> convert = chain(fromStringToChars(), fromCharsToInteger());
    final Formatting<String, Integer> onlyPush = onlyPush(new ToModel<Integer, String>() {
      @Override
      public Integer toModel(final String value) {
        return convert.toModel(value);
      }
    });

    // should Pass
    assertThat(onlyPush.toModel("10"), equalTo(10));

    // will work only from model to View, line should raise exception
    assertThat(onlyPush.toView(10), equalTo("10"));

    fail("exception expected!");
  }

  @Test
  public void test_06_toNumbers() {
    final Formatting<String, Byte> bytes = fromStringToNumber(Byte.class);
    final Formatting<String, Short> shorts = fromStringToNumber(Short.class);
    final Formatting<String, Integer> integers = fromStringToNumber(Integer.class);
    final Formatting<String, Long> longs = fromStringToNumber(Long.class);
    final Formatting<String, Float> floats = fromStringToNumber(Float.class);
    final Formatting<String, Double> doubles = fromStringToNumber(Double.class);

    assertThat(bytes.toModel("1"), equalTo((byte) 1));
    assertThat(bytes.toView((byte) 1), equalTo("1"));

    assertThat(shorts.toModel("1"), equalTo((short) 1));
    assertThat(shorts.toView((short) 1), equalTo("1"));

    assertThat(integers.toModel("1"), equalTo(1));
    assertThat(integers.toView(1), equalTo("1"));

    assertThat(longs.toModel("1"), equalTo(1L));
    assertThat(longs.toView(1L), equalTo("1"));

    assertThat(floats.toModel("1.1"), equalTo(1.1f));
    assertThat(floats.toView(1.1f), equalTo("1.1"));

    assertThat(doubles.toModel("1.1"), equalTo(1.1));
    assertThat(doubles.toView(1.1), equalTo("1.1"));
  }

  @Test
  public void test_07_Boolean() throws Exception {
    final Formatting<Integer, Boolean> converter = fromIntegerToBoolean();

    assertThat(converter.toModel(2), equalTo(true));
    assertThat(converter.toView(true), equalTo(1));

    assertThat(converter.toModel(0), equalTo(false));
    assertThat(converter.toView(false), equalTo(0));
  }
}
