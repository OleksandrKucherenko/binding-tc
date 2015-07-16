package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.reflection.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Helpers that simplify common operations with different data storage instances. */
@SuppressWarnings({"unused", "unchecked"})
public final class Models {

  private Models() {
    throw new AssertionError();
  }

  /* [ DATA STRUCTURES ] ========================================================================================== */

  @NonNull
  public static <I, T> Selector<I, T> pojo(@NonNull final I instance,
                                           @NonNull Property<T> property) {
    return new Selector<>(instance, property);
  }

  @NonNull
  public static <I, T> Selector<I, T> map(@NonNull final Map<String, I> instance,
                                          @NonNull Property<T> property) {
    // TODO: property should know how to extract value from MAP, property name is a key

    return null;
  }

  @NonNull
  public static <I, T> Selector<I, T> index(@NonNull final List<I> instance,
                                            @NonNull Property<T> property) {
    // TODO: property should know how to extract value from LIST, property name is index/position

    return null;
  }

  @NonNull
  public static <I, T> Selector<I, T> index(@NonNull final I[] instance,
                                            @NonNull Property<T> property) {
    return index(Arrays.asList(instance), property);
  }

  /* [ GENERICS ] ================================================================================================= */

  /** Extract generic type information in tricky way. */
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

    return null;
  }

  @NonNull
  public static <T> Property<T> from(@NonNull final String name) {
    return new Property<>((Class<T>) typeTrick(), name);
  }

  @NonNull
  public static <T> Property<T> from(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>((Class<T>) typeTrick(), getName, setName);
  }

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
  public static Property<Character> letter(@NonNull final String name) {
    return new Property<>(Character.class, name);
  }

  @NonNull
  public static Property<Character> letter(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(Character.class, getName, setName);
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
  public static Property<String> text(@NonNull final String name) {
    return new Property<>(String.class, name);
  }

  @NonNull
  public static Property<String> text(@NonNull final String getName, @NonNull final String setName) {
    return new Property<>(String.class, getName, setName);
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
