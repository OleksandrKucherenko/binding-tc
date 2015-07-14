package com.artfulbits.ui.binding;

import android.support.annotation.NonNull;

import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.toolbox.Formatter;

import org.junit.Test;

import static com.artfulbits.ui.binding.toolbox.Models.integer;
import static com.artfulbits.ui.binding.toolbox.Models.pojo;
import static com.artfulbits.ui.binding.toolbox.Models.text;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/** Unit tests for class {@link Binder}. */
public class BinderTests extends TestHolder {
  /* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  @Test
  public void test_00_PojoToPojo_Simplest() {
    final PojoNamePin model = new PojoNamePin();
    final PojoLoginPassword view = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(view, text("Login"))) // PojoLoginPassword.getLogin()
        .model(pojo(model, text("Name"))); // PojoNamePin.getName()

    model.setName("name-set");
    bss.push(); // from ONE --> TWO

    assertThat(view.getLogin(), equalTo("name-set"));
    assertThat(bss.isPushOk(), equalTo(true));

    view.setLogin("login-set");
    bss.pop(); // from TWO --> ONE

    assertThat(model.getName(), equalTo("login-set"));
    assertThat(bss.isPopOk(), equalTo(true));

    // check that POP status is still the same, sticky...
    assertThat(bss.isPushOk(), equalTo(true));

    // validate binder object state
    assertThat(bss.getRuntimeModel(), notNullValue());
    assertThat(bss.getRuntimeView(), notNullValue());
    assertThat(bss.getModelType().equals(PojoNamePin.class), equalTo(true));
    assertThat(bss.getViewType().equals(PojoLoginPassword.class), equalTo(true));
    assertThat(bss.resolveFormatting(), notNullValue());
    assertThat(bss.resolveValidation(), notNullValue());
    assertThat(bss.resolveModel(), notNullValue());
    assertThat(bss.resolveModel().getGetterName(), equalTo("getName"));
    assertThat(bss.resolveModel().getSetterName(), equalTo("setName"));
    assertThat(bss.resolveView(), notNullValue());
    assertThat(bss.resolveView().getGetterName(), equalTo("getLogin"));
    assertThat(bss.resolveView().getSetterName(), equalTo("setLogin"));
  }

  @Test
  public void test_01_PojoToPojo_WithFormatting() {
    final PojoNamePin modelInstance = new PojoNamePin();
    final PojoLoginPassword viewInstance = new PojoLoginPassword();
    final Binder<String, Integer> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Password"))) // PojoLoginPassword.getPassword()
        .model(pojo(modelInstance, integer("Pin"))) // PojoNamePin.getPin()
        .format(Formatter.toInteger());

    modelInstance.setPin(1234);
    bss.push(); // from MODEL --> VIEW

    assertThat(viewInstance.getPassword(), equalTo("1234"));
    assertThat(bss.isPushOk(), equalTo(true));

    viewInstance.setPassword("4321");
    bss.pop(); // from VIEW --> MODEL

    assertThat(modelInstance.getPin(), equalTo(4321));
    assertThat(bss.isPopOk(), equalTo(true));
  }

  @Test
  public void test_02_PojoToPojo_WithValidation() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Name")))
        .model(pojo(modelInstance, text("Login")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder b) {
            trace("failure validation");
          }
        });

    // Step #1: validation ok - POP
    modelInstance.setLogin("login-set");
    bss.push(); // from MODEL --> VIEW
    assertThat(modelInstance.getLogin(), equalTo("login-set"));
    assertThat(bss.isPushOk(), equalTo(true));
    assertThat(getRawLogger().toString(), containsString("success validation"));

    // Step #1: validation ok - PUSH
    viewInstance.setName("name-set");
    bss.pop(); // from VIEW --> MODEL
    assertThat(viewInstance.getName(), equalTo("name-set"));
    assertThat(bss.isPopOk(), equalTo(true));

    // Step #2: validation failed - POP
    final boolean wasPushOk = bss.isPushOk();
    modelInstance.setLogin("dummy");
    bss.push();
    assertThat(bss.isPushOk(), equalTo(false));
    assertThat(bss.isPushOk(), not(equalTo(wasPushOk)));
    assertThat(bss.isPopOk(), equalTo(true)); // state from prev call
    assertThat(viewInstance.getName(), not(equalTo("dummy")));
    assertThat(getRawLogger().toString(), containsString("failure validation"));

    // Step #2: validation failed - PUSH
    final boolean wasPopOk = bss.isPopOk();
    viewInstance.setName("dummy2");
    bss.pop();
    assertThat(bss.isPopOk(), equalTo(false));
    assertThat(bss.isPopOk(), not(equalTo(wasPopOk)));
    assertThat(modelInstance.getLogin(), not(equalTo("dummy2")));

    // trace expression for validation
    trace(bss.toString());
  }

	/* [ NESTED DECLARATIONS ] ======================================================================================= */

  public static class PojoNamePin {
    private String mName;
    private int mPin;

    public String getName() {
      return mName;
    }

    public void setName(final String name) {
      mName = name;
    }

    public int getPin() {
      return mPin;
    }

    public void setPin(final int pin) {
      mPin = pin;
    }
  }

  public static class PojoLoginPassword {
    private String mLogin;

    private String mPassword;

    public String getLogin() {
      return mLogin;
    }

    public void setLogin(final String login) {
      mLogin = login;
    }

    public String getPassword() {
      return mPassword;
    }

    public void setPassword(final String password) {
      mPassword = password;
    }
  }
}
