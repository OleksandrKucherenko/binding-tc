package com.truecaller.ui.binding;

/**
 * Interface responsible for converting values in to-, from- directions.<br/> Main rule for inheritors of the
 * interface:<br/> <code> assertEqual( In, toIn(toOut(In)) );<br/> assertEqual( Out, toOut(toIn(Out)) ); </code>
 *
 * @param <Right> type from right side of binding.
 * @param <Left>  type from left side of binding.
 */
public interface Converter<Left, Right> {
  /**
   * Convert In- value into Out- value.
   *
   * @param value the value
   * @return the k
   */
  Left toOut(final Right value);

  /**
   * Convert Out- value into In- value.
   *
   * @param value the value
   * @return the t
   */
  Right toIn(final Left value);
}
