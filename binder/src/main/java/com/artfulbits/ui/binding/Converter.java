package com.artfulbits.ui.binding;

/**
 * Interface responsible for converting values in to-, from- directions.<br/> Main rule for inheritors of the
 * interface:<br/> <code> assertEqual( In, toIn(toOut(In)) );<br/> assertEqual( Out, toOut(toIn(Out)) ); </code>
 *
 * @param <TRight> type from right side of binding.
 * @param <TLeft>  type from left side of binding.
 */
@SuppressWarnings("unused")
public interface Converter<TLeft, TRight> {
  /**
   * Convert In- value into Out- value.
   *
   * @param value the value
   * @return the k
   */
  TLeft toOut(final TRight value);

  /**
   * Convert Out- value into In- value.
   *
   * @param value the value
   * @return the t
   */
  TRight toIn(final TLeft value);
}
