package com.artfulbits.ui.binding;

/**
 * Interface responsible for converting values in to-, from- directions.<br/> Main rule for inheritors of the
 * interface:<br/> <code> assertEqual( In, toIn(toOut(In)) );<br/> assertEqual( Out, toOut(toIn(Out)) ); </code>
 *
 * @param <TRight> type from right side of binding (Model).
 * @param <TLeft>  type from left side of binding (View).
 */
@SuppressWarnings("unused")
public interface Formatting<TLeft, TRight> {
  /**
   * Convert In- value into Out- value.
   *
   * @param value the value
   * @return the k
   * @throws com.artfulbits.ui.binding.exceptions.OneWayBindingError in case if allowed only one way binding.
   */
  TLeft toOut(final TRight value);

  /**
   * Convert Out- value into In- value.
   *
   * @param value the value
   * @return the t
   * @throws com.artfulbits.ui.binding.exceptions.OneWayBindingError in case if allowed only one way binding.
   */
  TRight toIn(final TLeft value);
}
