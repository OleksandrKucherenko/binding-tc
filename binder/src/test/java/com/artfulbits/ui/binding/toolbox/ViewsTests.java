package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.Selector;
import com.artfulbits.ui.binding.reflection.Property;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Tests for {@link Views} class. */
public class ViewsTests extends TestHolder {
  public static final int MOCK_ID = 1;

  @Test
  public void test_00_Binding_To_findByViewId() throws Exception {
    final ActivityMock activity = new ActivityMock();

    // call with specific arguments
    final Property<IView> find = new Property<IView>(Models.<IView>typeTrick(), "findViewById", Property.NO_NAME) {
      @Override
      protected Object[] getterArguments() {
        return new Object[]{MOCK_ID};
      }
    };
    final Selector<ActivityMock, IView> selector = new Selector<>(activity, find);

    trace(selector.toString());

    assertThat(activity, is(notNullValue()));
    assertThat(selector, is(notNullValue()));
    assertThat(selector.get(), is(notNullValue()));
  }

  public static class ActivityMock {
    public IView findViewById(int id) {
      if (MOCK_ID == id) {
        return new IView() {
        };
      }

      return null;
    }
  }

  /***/
  public interface IView {
  }
}
