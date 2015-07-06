package com.artfulbits.ui.binding;

import com.artfulbits.ui.binding.reflection.Property;

/**
 * Selector of the property value from instance.<br/> Responsibility:<br/> - 'Late Binding' declaration.<br/> - Allows
 * to associate property 'binding information' with instance of class.<br/> - Allows to resolve chain of
 * selectors.<br/>
 *
 * @param <I> the instance data type
 * @param <V> the property extracted data type
 */
@SuppressWarnings("unused")
public class Selector<I, V> {
  /* ============================================================================================================== */

  /** Instance of the class which we use as source/destination of data. */
  private final I mInstance;
  /** Property descriptor. */
  private final Property<V> mProperty;

  /* ============================================================================================================== */

  public Selector(final I instance, final Property<V> property) {
    mInstance = instance;
    mProperty = property;
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
  public I getRuntimeInstance() {
    // late binding trick, allows to build a chain of calls
    if (mInstance instanceof Selector) {
      final Selector inner = (Selector) mInstance;
      final Object result = inner.get();
      return (I) result;
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

  /* [ INITIALIZATION HELPERS ] =================================================================================== */

  public Selector<I, V> listenTo(final Listener<?> listener) {
    // TODO: store for late binding when actually Property instances will be calculated

    //listener.attach(mProperty, mInstance);

    return this;
  }
}
