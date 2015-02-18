package com.truecaller.ui.binding;

import com.truecaller.ui.binding.reflection.Property;

/**
 * The type Selector of the property value from instance.
 *
 * @param <I> the instance data type
 * @param <P> the property extracted data type
 */
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

  public Class<?> getInstanceType() {
    return mInstance.getClass();
  }

  public P getProperty() {
    return mProperty;
  }

  /* [ INITIALIZATION HELPERS ] =================================================================================== */

  public Selector<I, P> listenTo(final Listener<?> listener) {
    // TODO: store for late binding when actually Property instances will be calculated

    //listener.attach(mProperty, mInstance);

    return this;
  }
}
