package com.truecaller.ui.binding;

/**
 * Interface responsible for converting values in to-, from- directions.<br/> Main rule for inheritors of the
 * interface:<br/> <code> assertEqual( In, toIn(toOut(In)) );<br/> assertEqual( Out, toOut(toIn(Out)) ); </code>
 *
 * @param <In>  type from right side of binding.
 * @param <Out> type from left side of binding.
 */
public interface Converter<Out, In> {
  /**
   * Convert In- value into Out- value.
   *
   * @param value the value
   * @return the k
   */
  Out toOut(final In value);

  /**
   * Convert Out- value into In- value.
   *
   * @param value the value
   * @return the t
   */
  In toIn(final Out value);
}
