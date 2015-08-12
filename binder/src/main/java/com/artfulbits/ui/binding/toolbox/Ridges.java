package com.artfulbits.ui.binding.toolbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.reflection.Property;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;

/**
 * Class contain implementation of typical ridge strategies for Binding between model and view.
 * <p/>
 * Why we needs this? View and Model may operate the same high level reference, that makes logic corrupted and buggy.
 * For fixing that we should guaranty that view and model has own instances. That is why exists this
 * interface and implementation.
 * <p/>
 * Real life example: android TextView internally store text in {@link android.text.SpannedString} instance. When we
 * request text we got a reference on this high-level class and will assign it to Model.
 */
@SuppressWarnings("unchecked")
public final class Ridges {

  /** Simplest strategy. */
  @NonNull
  public static <T> Binder.Ridge<T> simplest() {
    return new Binder.Ridge<T>() {
      private T mValue;

      @Override
      public boolean isChanged(final T value) {
        if (null != mValue)
          return !mValue.equals(value);

        return null == value || !value.equals(mValue);

      }

      @Override
      public T clone(final T value) {
        // custom logic for CharSequence/Spanned
        if (value instanceof CharSequence) {
          return mValue = (T) Ridges.clone((CharSequence) value);
        } else if (value instanceof Cloneable) {
          final Selector<T, ?> caller = new Selector<>(value, Models.from("clone", Property.NO_NAME));

          // call clone() method
          return mValue = (T) caller.get();
        } else if (value instanceof Serializable) {
          return mValue = (T) deepCopy((Serializable) value);
        }

        return mValue = value;
      }

      @Override
      public String toString() {
        return String.format(Locale.US, "simplest@%s = %s", Integer.toHexString(hashCode()), mValue);
      }
    };
  }

  /** Clone CharSequence instance. */
  public static CharSequence clone(@NonNull final CharSequence value) {
    // custom logic for CharSequence/Spanned
    if (value instanceof SpannedString) {
      return new SpannedString(value);
    } else if (value instanceof SpannableString) {
      return new SpannableString(value);
    } else if (value instanceof SpannableStringBuilder) {
      return new SpannableStringBuilder(value);
    }

    return value;
  }

  public static <T extends Serializable> T bundleCopy(final T value) {
    Bundle b = new Bundle();
    b.putSerializable("obj", value);
    return (T) b.getSerializable("obj");
  }

  public static <T extends Serializable> T deepCopy(final T value) {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      oos.writeObject(value);
      oos.flush();

      final ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
      ois = new ObjectInputStream(bin);
      return (T) ois.readObject();

    } catch (final Throwable ignored) {

    } finally {
      if (null != oos) {
        try {
          oos.close();
        } catch (final Throwable ignored) {
        }
      }

      if (null != ois) {
        try {
          ois.close();
        } catch (final Throwable ignored) {
        }
      }
    }

    return value;
  }
}
