package com.artfulbits.binding.toolbox;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.artfulbits.binding.BindingsManager;
import com.artfulbits.binding.BuildConfig;
import com.artfulbits.binding.Selector;
import com.artfulbits.binding.data.DummyClass;
import com.artfulbits.binding.ui.BindingAdapter;
import com.artfulbits.junit.TestHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.artfulbits.binding.toolbox.Models.pojo;
import static com.artfulbits.binding.toolbox.Models.text;
import static com.artfulbits.binding.toolbox.Views.textView;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for {@link Adapters}. */
@Config(sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = "src/main/AndroidManifest.xml",
    libraries = {
        BuildConfig.ROBOLECTRIC_APPCOMPAT
    })
@RunWith(RobolectricTestRunner.class)
public class AdaptersTests extends TestHolder {
  // create a array of dummy instances
  final DummyClass[] array = new DummyClass[]{
      //region Instances
      new DummyClass("Item #01"),
      new DummyClass("Item #02"),
      new DummyClass("Item #03"),
      new DummyClass("Item #04"),
      new DummyClass("Item #05"),
      new DummyClass("Item #06"),
      new DummyClass("Item #07"),
      new DummyClass("Item #08"),
      new DummyClass("Item #09"),
      new DummyClass("Item #10"),
      new DummyClass("Item #11"),
      new DummyClass("Item #12"),
      new DummyClass("Item #13"),
      new DummyClass("Item #14"),
      new DummyClass("Item #15"),
      new DummyClass("Item #16"),
      new DummyClass("Item #17"),
      new DummyClass("Item #18"),
      new DummyClass("Item #19"),
      new DummyClass("Item #20")
      //endregion
  };

  @Test
  public void test_00_BaseAdapter_InlinedBinding() {
    final Context context = RuntimeEnvironment.application;
    final int resourceId = android.R.layout.simple_list_item_1;
    final Adapter adapter = new ArrayAdapter<>(context, resourceId, array);

    final BindingAdapter bindable = Adapters.wrap(adapter, new BindingAdapter.Lifecycle() {
      int counter = 0;

      @Override
      public void onCreateBinding(@NonNull final BindingsManager bm,
                                  @NonNull final Selector<?, View> getView,
                                  @NonNull final Selector<?, Object> getModel) {
        Binders.strings(bm)
            .view(textView(getView, android.R.id.text1))
            .model(pojo(getModel, text("fieldStr")));

        trace("Binding executed [" + counter + "]");
        counter++;
      }
    });

    assertThat(bindable.getCount(), equalTo(array.length));
    assertThat(bindable.getItem(0), instanceOf(DummyClass.class));
  }

  @Test
  public void test_01_InheritedAdapter() {
    final Context context = RuntimeEnvironment.application;
    final int resourceId = android.R.layout.simple_list_item_1;
    final Adapter adapter = new ArrayAdapter<>(context, resourceId, array);
    final DummyAdapter bindable = new DummyAdapter(adapter);

    assertThat(bindable.getCount(), equalTo(array.length));
    assertThat(bindable.getItem(0), instanceOf(DummyClass.class));

    for (int i = 0; i < array.length; i++) {
      final DummyClass item = (DummyClass) bindable.getItem(i);
      final View v = bindable.getView(i, null, null);

      assertThat(v, notNullValue());
      assertThat(item, notNullValue());

      final TextView textView = (TextView) v.findViewById(android.R.id.text1);
      assertThat(textView.getText().toString(), equalTo(item.toString()));
    }

    // expected only one time call of Binding defining method
    assertThat(bindable.getCounter(), equalTo(1));
  }

  private final static class DummyAdapter extends BindingAdapter {
    private int mCounter;

    public DummyAdapter(@NonNull final Adapter adapter) {
      super(adapter);
    }

    public DummyAdapter(@NonNull final Adapter adapter, @Nullable final Lifecycle lifecycle) {
      super(adapter, lifecycle);
    }

    public int getCounter() {
      return mCounter;
    }

    @Override
    public void onCreateBinding(@NonNull final BindingsManager bm, @NonNull final Selector<?, View> getView, @NonNull final Selector<?, Object> getModel) {
      super.onCreateBinding(bm, getView, getModel);

      mCounter++;
    }

    @Override
    public void onValidationResult(final BindingsManager bm, final boolean success) {
      super.onValidationResult(bm, success);
    }
  }
}
