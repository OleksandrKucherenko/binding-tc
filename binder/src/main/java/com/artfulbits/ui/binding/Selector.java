package com.artfulbits.ui.binding;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.exceptions.WrongConfigurationError;
import com.artfulbits.ui.binding.reflection.Property;

import java.util.Locale;

/**
 * Selector of the property value from instance.<br/> Responsibility:<br/> - 'Late Binding' declaration.<br/> - Allows
 * to associate property 'binding information' with instance of class.<br/> - Allows to resolve chain of
 * selectors.<br/>
 *
 * @param <I> the instance data type
 * @param <V> the property extracted data type
 */
@SuppressWarnings("unused")
public class Selector<I, V> implements Notifications {
  /* ============================================================================================================== */

  /** Instance of the class which we use as source/destination of data. */
  private final I mInstance;
  /** Property descriptor. */
  private final Property<V> mProperty;

  /* ============================================================================================================== */

  /** Create a new instance of the selector. */
  public Selector(@NonNull final I instance, @NonNull final Property<V> property) {
    mInstance = instance;
    mProperty = property;
  }

  /**
   * Return human readable resolved to string selector. If runtime binding not happens yet, resolving to string may
   * provide 'incomplete' selector without correct properties names.
   * <p/>
   * Example 1: {View}.getText()/{View}.setText() | setText()<br/> Example 2: ({Activity}.findViewById(...)).getText() |
   * setText()<br/> Example 3: (({Data}.getSubItem()).getView()).getText() | setText()
   * <p/>
   * Syntax:<br/> {} - dynamic type, resolved in runtime<br/> &lt;&gt; - constant<br/> ... - arguments expected;
   */
  @Override
  public String toString() {
    final String subSelector = (mInstance instanceof Selector) ?
        "(" + ((Selector) mInstance).toGetterString() + ")" :
        "{" + getInstanceType().getSimpleName() + "}";

    // selector is designed mostly for GET only approach
    return String.format(Locale.US, "%s.%s", subSelector, mProperty.toString());
  }

  @NonNull
  public String toGetterString() {
    final String subSelector = (mInstance instanceof Selector) ?
        "(" + ((Selector) mInstance).toGetterString() + ")" :
        "{" + getInstanceType().getSimpleName() + "}";

    // selector is designed mostly for GET only approach
    return String.format(Locale.US, "%s.%s", subSelector, mProperty.toGetterString());
  }

  @NonNull
  public String toSetterString() {
    final String subSelector = (mInstance instanceof Selector) ?
        "(" + ((Selector) mInstance).toGetterString() + ")" :
        "{" + getInstanceType().getSimpleName() + "}";

    // selector is designed mostly for GET only approach
    return String.format(Locale.US, "%s.%s", subSelector, mProperty.toSetterString());
  }

  /* ============================================================================================================== */

  /** Get instance reflection type. */
  public Class<?> getInstanceType() {
    return mInstance.getClass();
  }

  /** Get property definition that should be used for data exchange. */
  public Property<V> getProperty() {
    return mProperty;
  }

  /** Return reference on associated instance. */
  @SuppressWarnings({"unchecked"})
  public I getRuntimeInstance() {
    // late binding trick, allows to build a chain of calls
    if (mInstance instanceof Selector) {
      return (I) ((Selector) mInstance).get();
    }

    return mInstance;
  }

  /** Extract value from property. */
  public V get() {
    return getProperty().get(getRuntimeInstance());
  }

  /** Set value of the property. */
  public void set(final V value) {
    getProperty().set(getRuntimeInstance(), value);
  }

  /** Force property to resolve bindings now. Allows to validate configuration of instance. */
  public void resolve() throws WrongConfigurationError {
    getProperty().resolve(getRuntimeInstance());
  }

  @Override
  public final void onChanged() {
    // leave for inheritors
  }

  /* [ INITIALIZATION HELPERS ] =================================================================================== */

}
