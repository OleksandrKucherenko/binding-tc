package com.artfulbits.ui.binding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.artfulbits.junit.TestHolder;
import com.artfulbits.ui.binding.exceptions.ConfigurationError;
import com.artfulbits.ui.binding.toolbox.Formatter;
import com.artfulbits.ui.binding.toolbox.Listeners;

import org.junit.Test;

import java.util.Observable;

import static com.artfulbits.ui.binding.toolbox.Models.integer;
import static com.artfulbits.ui.binding.toolbox.Models.number;
import static com.artfulbits.ui.binding.toolbox.Models.pojo;
import static com.artfulbits.ui.binding.toolbox.Models.real;
import static com.artfulbits.ui.binding.toolbox.Models.text;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    bss.pop(); // from ONE --> TWO

    assertThat(view.getLogin(), equalTo("name-set"));
    assertThat(bss.isPushOk(), equalTo(true));

    view.setLogin("login-set");
    bss.push(); // from TWO --> ONE

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
    bss.pop(); // from MODEL --> VIEW

    assertThat(viewInstance.getPassword(), equalTo("1234"));
    assertThat(bss.isPushOk(), equalTo(true));

    viewInstance.setPassword("4321");
    bss.push(); // from VIEW --> MODEL

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
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    // Step #1: validation ok - POP
    modelInstance.setLogin("login-set");
    bss.pop(); // from MODEL --> VIEW
    assertThat(modelInstance.getLogin(), equalTo("login-set"));
    assertThat(bss.isPushOk(), equalTo(true));
    assertThat(getRawLogger().toString(), containsString("success validation"));

    // Step #1: validation ok - PUSH
    viewInstance.setName("name-set");
    bss.push(); // from VIEW --> MODEL
    assertThat(viewInstance.getName(), equalTo("name-set"));
    assertThat(bss.isPopOk(), equalTo(true));

    // Step #2: validation failed - POP
    final boolean wasPushOk = bss.isPushOk();
    modelInstance.setLogin("dummy");
    bss.pop();
    assertThat(bss.isPushOk(), equalTo(false));
    assertThat(bss.isPushOk(), not(equalTo(wasPushOk)));
    assertThat(bss.isPopOk(), equalTo(true)); // state from prev call
    assertThat(viewInstance.getName(), not(equalTo("dummy")));
    assertThat(getRawLogger().toString(), containsString("failure validation"));

    // Step #2: validation failed - PUSH
    final boolean wasPopOk = bss.isPopOk();
    viewInstance.setName("dummy2");
    bss.push();
    assertThat(bss.isPopOk(), equalTo(false));
    assertThat(bss.isPopOk(), not(equalTo(wasPopOk)));
    assertThat(modelInstance.getLogin(), not(equalTo("dummy2")));

    // trace expression for validation
    trace(bss.toString());
  }

  @Test
  public void test_03_Resolve_Common() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Name")))
        .model(pojo(modelInstance, text("Login")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    // correct configuration, no Exceptions
    bss.resolve();
  }

  @Test(expected = ConfigurationError.class)
  public void test_04_Resolve_NoView() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
//        .view(pojo(viewInstance, text("Name")))
        .model(pojo(modelInstance, text("Login")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    bss.resolve();

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_05_Resolve_NoModel() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Name")))
//        .model(pojo(modelInstance, text("Login")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    bss.resolve();

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_06_Resolve_View_BrokenBinding() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Something")))
        .model(pojo(modelInstance, text("Login")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    // expected exception, "Something" field/method does not exists in View
    bss.resolve();

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_07_Resolve_Model_BrokenBinding() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Name")))
        .model(pojo(modelInstance, text("Something")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    // expected exception, "Something" field/method does not exists in Model
    bss.resolve();

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_08_Resolve_ModelAndView_BrokenBinding() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<String, String> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Something")))
        .model(pojo(modelInstance, text("Something")))
        .validate(allOf(notNullValue(), containsString("-set")))
        .onSuccess(new Success() {
          @Override
          public void onValidationSuccess(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("success validation");
          }
        })
        .onFailure(new Failure() {
          @Override
          public void onValidationFailure(@NonNull final BindingsManager bm, @NonNull final Binder<?, ?> b) {
            trace("failure validation");
          }
        });

    // expected exception, "Something" field/method does not exists in Model and View
    bss.resolve();

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_09_Resolve_GetSet_BrokenBinding() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<Double, Long> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, real("Amount")))
        .model(pojo(modelInstance, number("Timestamp")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    // expected exception, "Something" field/method does not exists in Model and View
    bss.resolve();

    fail("exception expected!");
  }

  @Test
  public void test_10_Listeners() {
    final PojoStringObserve viewInstance = new PojoStringObserve();
    final PojoObserve modelInstance = new PojoObserve();
    final Binder<String, Long> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, text("Time")))
        .model(pojo(modelInstance, number("Timestamp")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    bss.resolve();

    final BindingsManager bmMocked = mock(BindingsManager.class);

    // listeners works only when binder attached to manager
    bss.attachToManager(bmMocked);

    // manager should receive one call - onModel
    modelInstance.setTimestamp(System.currentTimeMillis());
    verify(bmMocked, times(1)).notifyOnModelChanged(bss);

    // manager should receive one call - onView
    viewInstance.setTime("2015-08-06");
    verify(bmMocked, times(1)).notifyOnViewChanged(bss);
  }

  @Test(expected = ConfigurationError.class)
  public void test_11_BadConfiguration_NoView_Push() {
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<Double, Long> bss = new Binder<>();

    bss
        .model(pojo(modelInstance, number("Timestamp")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    bss.push(); // raise exception! No View

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_12_BadConfiguration_NoModel_Push() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final Binder<Double, Long> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, real("Amount")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    bss.push(); // raise exception! No Model

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_13_BadConfiguration_NoView_Pop() {
    final PojoLoginPassword modelInstance = new PojoLoginPassword();
    final Binder<Double, Long> bss = new Binder<>();

    bss
        .model(pojo(modelInstance, number("Timestamp")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    bss.pop(); // raise exception! No View

    fail("exception expected!");
  }

  @Test(expected = ConfigurationError.class)
  public void test_14_BadConfiguration_NoModel_Pop() {
    final PojoNamePin viewInstance = new PojoNamePin();
    final Binder<Double, Long> bss = new Binder<>();

    bss
        .view(pojo(viewInstance, real("Amount")))
        .onView(Listeners.onObservable())
        .onModel(Listeners.onObservable());

    bss.pop(); // raise exception! No Model

    fail("exception expected!");
  }

  /* [ NESTED DECLARATIONS ] ======================================================================================= */

  public static class PojoNamePin {
    private String mName;
    private int mPin;
    private double mAmounts;

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

    public double getAmount() {
      return mAmounts;
    }
  }

  public static class PojoLoginPassword {
    private String mLogin;

    private String mPassword;

    private long mTimestamp;

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

    public void setTimestamp(final long time) {
      mTimestamp = time;
    }
  }

  public static class PojoObserve extends Observable {
    private long mTimestampL;

    public long getTimestamp() {
      return mTimestampL;
    }

    public void setTimestamp(final long value) {
      mTimestampL = value;

      setChanged();
      notifyObservers("timestamp");
    }
  }

  public static class PojoStringObserve extends Observable {
    private String mTime;

    public String getTime() {
      return mTime;
    }

    public void setTime(final String value) {
      mTime = value;

      setChanged();
      notifyObservers("pojotime");
    }
  }
}
