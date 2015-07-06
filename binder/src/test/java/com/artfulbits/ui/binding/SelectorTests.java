package com.artfulbits.ui.binding;

import com.artfulbits.ui.binding.toolbox.Models;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests of the {@link com.artfulbits.ui.binding.Selector} class. */
public class SelectorTests {
  @Test
  public void test_00_CommonUsage() throws Exception {
    final DummyInner di = new DummyInner();
    di.setOwner("owner");
    di.getSubInner().setName("test");

    final Selector<DummyInner, String> sOwner = new Selector<>(di, Models.text("Owner"));

    assertThat(sOwner.get(), equalTo("owner"));
  }

  @Test
  public void test_01_ChainCalls() throws Exception {
    final DummyInner di = new DummyInner();
    di.setOwner("owner");
    di.getSubInner().setName("test");

    // chain of calls di.getSubInner().getName()
    final Selector<DummyInner, DummySubInner> sSub = new Selector<>(di, Models.<DummySubInner>from("SubInner"));
    final Selector<?, String> sName = new Selector<>(sSub, Models.text("Name"));

    assertThat(sName.get(), equalTo("test"));
  }

  
  /* [ NESTED DECLARATIONS ] ====================================================================================== */

  private static class DummyInner {
    private DummySubInner mSubInner = new DummySubInner();

    private String mOwner;

    public DummySubInner getSubInner() {
      return mSubInner;
    }

    public void setSubInner(final DummySubInner subInner) {
      mSubInner = subInner;
    }

    public String getOwner() {
      return mOwner;
    }

    public void setOwner(final String owner) {
      mOwner = owner;
    }
  }

  private static class DummySubInner {
    private String mName;

    private String mPassword;

    public String getName() {
      return mName;
    }

    public void setName(final String name) {
      mName = name;
    }

    public String getPassword() {
      return mPassword;
    }

    public void setPassword(final String password) {
      mPassword = password;
    }
  }
}
