package com.artfulbits.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.binding.Selector;
import com.artfulbits.binding.reflection.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Helpers that simplify common operations with different data storage instances. */
@SuppressWarnings({"unused", "unchecked"})
public final class Models {

  /** hidden constructor. */
  private Models() {
    throw new AssertionError();
  }

  /* [ DATA STRUCTURES ] ========================================================================================== */

  @NonNull
  public static <I, T> Selector<I, T> pojo(@NonNull final I instance, @NonNull final Property<T> property) {
    return new Selector<>(instance, property);
  }

  @NonNull
  public static <I extends Map<String, ?>, T> Selector<I, T> map(@NonNull final I instance, @NonNull final String name) {
    /* DONE: property should know how to extract value from MAP, property name is a key */

    // instance.get(name); instance.put(name, value);
    final Property<T> p = new Property<T>(Models.<T>typeTrick(), "get", "put") {
      @Override
      protected Object[] getterArguments() {
        return new Object[]{name};
      }

      @NonNull
      @Override
      protected Object[] setterArguments(T value) {
        return new Object[]{name, value};
      }
    };

    return new Selector<>(instance, p);
  }

  @NonNull
  public static <I extends List<T>, T> Selector<I, T> index(@NonNull final I instance, final int position) {
    /* DONE: property should know how to extract value from LIST, property name is index/position */

    // instance.get(index); instance.set(index, value);
    final Property<T> p = new Property<T>(Models.<T>typeTrick(), "get", "set") {
      @Override
      protected Object[] getterArguments() {
        return new Object[]{position};
      }

      @NonNull
      @Override
      protected Object[] setterArguments(T value) {
        return new Object[]{position, value};
      }
    };

    return new Selector<>(instance, p);
  }

  @NonNull
  public static <T> Selector<?, T> index(@NonNull final T[] instance, final int position) {
    return index(Arrays.asList(instance), position);
  }

  /* [ GENERICS ] ================================================================================================= */

  /**
   * Extract generic type information in tricky way.
   *
   * @param <T> automatically extracted data type during the call.
   */
  @NonNull
  public static <T> Class<T> typeTrick() {
    final Object t = new Trick() {
      Class<T> typeT;
    };

    try {
      final Class<T> type = (Class<T>) t.getClass().getDeclaredFields()[0].getType();

      return type;
    } catch (final Throwable ignored) {
      // do nothing
    }

    // should never happens
    throw new AssertionError("Type trick does not work. Via reflection is impossible to identify generic type.");
  }

  /**
   * Create property with defined by PATTERN GETTER and SETTER.
   *
   * @param name pattern/prefix of the name.
   * @param <T>  automatically extracted data type during the call.
   */
  @NonNull
  public static <T> Property<T> from(@NonNull final String name) {
    return new Property<>((Class<T>) typeTrick(), name);
  }

  /**
   * Create property with strict GETTER and SETTER.
   *
   * @param getName getter method/field name.
   * @param setName setter method/field name.
   * @param <T>     automatically extracted data type during the call.
   */
  @NonNull
  public static <T> Property<T> from(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>((Class<T>) typeTrick(), getName, setName);
  }

  /** Create a property that reflects method call without parameters. */
  @NonNull
  public static <T> Property<T> call(@NonNull final String method) {
    return new Property<>((Class<T>) typeTrick(), method, Property.NO_NAME);
  }

  /**
   * Create a property that reflects method call with arguments.
   *
   * @param method method name
   * @param args   additional arguments
   */
  @NonNull
  public static <T> Property<T> call(@NonNull final String method, final Object... args) {
    return new Property<T>(Models.<T>typeTrick(), method, Property.NO_NAME) {
      @Override
      protected Object[] getterArguments() {
        return args;
      }
    };
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  @NonNull
  public static Property<Integer> integer(@NonNull final String name) {
    return new Property<>(Integer.class, name);
  }

  @NonNull
  public static Property<Integer> integer(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Integer.class, getName, setName);
  }

  @NonNull
  public static Property<Long> number(@NonNull final String name) {
    return new Property<>(Long.class, name);
  }

  @NonNull
  public static Property<Long> number(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Long.class, getName, setName);
  }

  @NonNull
  public static Property<Float> decimal(@NonNull final String name) {
    return new Property<>(Float.class, name);
  }

  @NonNull
  public static Property<Float> decimal(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Float.class, getName, setName);
  }

  @NonNull
  public static Property<Double> real(@NonNull final String name) {
    return new Property<>(Double.class, name);
  }

  @NonNull
  public static Property<Double> real(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Double.class, getName, setName);
  }

  @NonNull
  public static Property<Boolean> bool(@NonNull final String name) {
    return new Property<>(Boolean.class, name);
  }

  @NonNull
  public static Property<Boolean> bool(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Boolean.class, getName, setName);
  }

  @NonNull
  public static Property<Character> letter(@NonNull final String name) {
    return new Property<>(Character.class, name);
  }

  @NonNull
  public static Property<Character> letter(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Character.class, getName, setName);
  }

  @NonNull
  public static Property<String> text(@NonNull final String name) {
    return new Property<>(String.class, name);
  }

  @NonNull
  public static Property<String> text(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(String.class, getName, setName);
  }

  @NonNull
  public static Property<CharSequence> chars(@NonNull final String name) {
    return new Property<>(CharSequence.class, name);
  }

  @NonNull
  public static Property<CharSequence> chars(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(CharSequence.class, getName, setName);
  }

  @NonNull
  public static Property<Byte> bytes(@NonNull final String name) {
    return new Property<>(Byte.class, name);
  }

  @NonNull
  public static Property<Byte> bytes(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Byte.class, getName, setName);
  }

  @NonNull
  public static Property<Short> shorts(@NonNull final String name) {
    return new Property<>(Short.class, name);
  }

  @NonNull
  public static Property<Short> shorts(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Short.class, getName, setName);
  }

  /** Hack interface for making possible anonymous classes creation. */
  private interface Trick {
  }
}
