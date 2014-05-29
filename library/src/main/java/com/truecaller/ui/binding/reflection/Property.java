package com.truecaller.ui.binding.reflection;

/** class is responsible for accessing a specific abstract 'field' by using reflection. */
public class Property<T> {

  private Class<T> mType;

  protected Property(final Class<T> type) {
    mType = type;
  }

  public final Class<T> getDataType() {
    return mType;
  }

  @SuppressWarnings("unchecked")
  public T get(final Object instance) {
    // TODO: extract the value from instance
    return null;
  }

  public boolean set(final Object instance, final T value) {
    // TODO: store the value in instance
    return false;
  }

  protected Object primitiveDefault(final Class<T> clazz) {
    if (int.class == clazz || short.class == clazz || long.class == clazz || byte.class == clazz) {
      return (byte) 0;
    }

    if (float.class == clazz || double.class == clazz) {
      return 0.0f;
    }

    if (char.class == clazz) {
      return '\0';
    }

    return false;
  }
}
