package com.artfulbits.ui.binding;

import com.artfulbits.ui.binding.reflection.Property;

/**
 * The type Selector of the property value from instance.
 * Responsibility: 'Late Binding' association. Allows to associate property 'late binding information'
 * with instance of class.
 *
 * @param <I> the instance data type
 * @param <P> the property extracted data type
 */
@SuppressWarnings("unused")
public class Selector<I, P extends Property> {
  /* ============================================================================================================== */

  /** Instance of the class which we use as source/destination of data. */
  private final I mInstance;
  /** Property descriptor. */
  private final P mProperty;

  /* ============================================================================================================== */

  public Selector(final I instance, final P property) {
    mInstance = instance;
    mProperty = property;
  }

  /* ============================================================================================================== */

  /** Get instance reflection type. */
  public Class<?> getInstanceType() {
    return mInstance.getClass();
  }

  /** Get property definition that should be used for data exchange. */
  public P getProperty() {
    return mProperty;
  }

  /** Return reference on associated instance. */
  public I getRuntimeInstance() {
    return mInstance;
  }

  /* [ INITIALIZATION HELPERS ] =================================================================================== */

  public Selector<I, P> listenTo(final Listener<?> listener) {
    // TODO: store for late binding when actually Property instances will be calculated

    //listener.attach(mProperty, mInstance);

    return this;
  }
}
