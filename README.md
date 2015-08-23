# binding-tc

*First Name:* ArtfulBits Binding
*Second Name:* Android Easy Binding (AEB)

Android View properties binding to the Business Objects (POJO).

_Why *TC* suffix? TC - Take Care!_

# State [![Build Status][3]][4] [![Coverage Status][5]][6] [![Gitter][7]][8]

Active development, started at: 2014-05-15

Changes: [Changes Log](_documentation/changes.md)

Roadmap (v1.0.0 - v2.0.0): [Road Map](_documentation/roadmap.md)

Compare to Android Binding: [Comparison](_documentation/comparison.md)

# Goals
* High performance, 
* easy syntax, 
* minimalistic approach, 
* highly customizable,
* hamcrest validation syntax,
* serializable binding configuration
* 100% covered by unit tests, TDD

#Binding In Details

![Data Flow inside the Binding Library](_documentation/images/binding-architecture-flow.png)

# Example of Usage

Typical Business Object declared in POJO way (with Observable pattern):

```java
public class Pin extends Observable {
  private String mPassword;
  private String mConfirmPassword;

  /** Calculated message based on state of instance. */
  public int getMessage() {
    if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mConfirmPassword))
      return R.string.msgPasswordPatternFail;

    if (mPassword.length() < 4 || mPassword.length() > 6)
      return R.string.msgPasswordPatternFail;

    if (mConfirmPassword.length() < 4 || mConfirmPassword.length() > 6)
      return R.string.msgPasswordPatternFail;

    return mPassword.equals(mConfirmPassword) ?
        R.string.msgPasswordOK :
        R.string.msgPasswordFail;
  }

  public String getConfirmPassword() {
    return mConfirmPassword;
  }

  public void setConfirmPassword(final String confirmPassword) {
    mConfirmPassword = confirmPassword;

    setChanged();
    notifyObservers("ConfirmPassword");
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(final String password) {
    mPassword = password;

    setChanged();
    notifyObservers("Password");
  }
}
```

Fragment binding:

```java
import com.artfulbits.binding.*;

import static com.artfulbits.binding.toolbox.Binders.*;
import static com.artfulbits.binding.toolbox.Listeners.*;
import static com.artfulbits.binding.toolbox.Models.*;
import static com.artfulbits.binding.toolbox.Molds.*;
import static com.artfulbits.binding.toolbox.Views.*;
import static org.hamcrest.core.IsAnything.anything;

/** Login fragment with simplest UI. */
public class PinFragment extends BindingFragment {

  private final Pin mPin = new Pin();

  public PlaceholderFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_pin, container, false);
  }

  @Override
  public void onCreateBinding(final BindingManager bm) {
    /**
     * EXPLANATION:
     * - we listen to user input in et_password and et_confirm_password
     * - during update of the model instance by entered text we expect Observable callbacks
     * - observable callback force Binding update of UI label - tv_feedback
     *
     * USE CASE:
     * - on wrong password display Error message
     * - on correct password display OK message,
     * - Correct pattern: pin contains at least 4 digits and not more than 6
     */

    // do binding only from UI side, only PUSH
    Binders.strings(bm) // <CharSequence - to - String>
        .view(editText(getView(), R.id.et_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mPin, text("Password")))
        .format(onlyPush(fromCharsToString()))
        .validate(Matchers.anyOf(blankString(), matchesPattern("[0-9]{4,6}")));

    Binders.strings(bm) // <CharSequence - to - String>
        .view(editText(getView(), R.id.et_confirm_password))
        .onView(anyOf(onTextChanged(), onFocusLost()))
        .model(pojo(mPin, text("ConfirmPassword")))
        .setTag(R.id.tag_test, "test");

    // POP message from model to View only, do that on model state change
    Binders.numeric(bm) // <CharSequence - to - Integer>
        .view(textView(getView(), R.id.tv_feedback))
        .model(pojo(mPin, integer("Message")))
        .onModel(onObservable())
        .format(onlyPop(new ToView<CharSequence, Integer>() {
          @Override
          public CharSequence toView(final Integer value) {
            return getString(value);
          }
        }));
  }
}
``` 

# Extended lifecycle

![Android Activity/Fragments lifecycle][1]

AEB adding a new step into lifecycle ```onCreateBinding()``` it executed after the ```onAttachedToWindow()``` and before ```onCreateOptionsMenu()```.
 
# Generic Concept Overview

## Binding Concept

![High Level Data Flow](_documentation/images/binding-overview-data-flow.png)

## Abstractions

[Abstractions and Responsibilities](_documentation/abstractions.md)

# License

    The MIT License (MIT)
    Copyright (c) 2014-2015 Oleksandr Kucherenko, ArtfulBits Inc.
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


[1]: https://raw.githubusercontent.com/xxv/android-lifecycle/master/complete_android_fragment_lifecycle.png
[2]: http://evendanan.net/robolectric/unit-test/2015/04/09/migrating-to-robolectric-v3/
[3]: https://secure.travis-ci.org/OleksandrKucherenko/binding-tc.png?branch=master
[4]: https://travis-ci.org/OleksandrKucherenko/binding-tc
[5]: https://coveralls.io/repos/OleksandrKucherenko/binding-tc/badge.svg?branch=master&service=github
[6]: https://coveralls.io/github/OleksandrKucherenko/binding-tc?branch=master
[7]: https://badges.gitter.im/Join%20Chat.svg
[8]: https://gitter.im/OleksandrKucherenko/binding-tc?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge