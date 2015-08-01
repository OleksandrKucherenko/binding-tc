package com.artfulbits.ui.binding;

import com.artfulbits.ui.binding.toolbox.ToModel;
import com.artfulbits.ui.binding.toolbox.ToView;

/**
 * Interface responsible for converting values in to-, from- directions.<br/> Main rule for inheritors of the
 * interface:<br/> <code> assertEqual( In, toIn(toOut(In)) );<br/> assertEqual( Out, toOut(toIn(Out)) ); </code>
 *
 * @param <TRight> type from right side of binding (Model).
 * @param <TLeft>  type from left side of binding (View).
 */
@SuppressWarnings("unused")
public interface Formatting<TLeft, TRight>
    extends ToView<TLeft, TRight>, ToModel<TRight, TLeft> {
}

