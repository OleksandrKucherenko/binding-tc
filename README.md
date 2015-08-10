# binding-tc

*Second Name:* Android Easy Binding (AEB)

Android View properties binding to the Business Objects (POJO). 

# State [![Build Status](https://secure.travis-ci.org/OleksandrKucherenko/binding-tc.png?branch=master)](https://travis-ci.org/OleksandrKucherenko/binding-tc) [![Coverage Status](https://coveralls.io/repos/OleksandrKucherenko/binding-tc/badge.svg?branch=master&service=github)](https://coveralls.io/github/OleksandrKucherenko/binding-tc?branch=master)

Active development, started at: 2014-05-15
Changes: ![Changes Log](_documentation/changes.md)

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
Typical Business Object declared in POJO way:

```java
  public static class User{
    private String mLogin;
    private String mPassword;
  
    public String getLogin(){ return mLogin; }
    public String getPassword(){ return mPassword; }
  }
```
Fragment binding:

```java
import com.artfulbits.ui.binding.*;

import static com.artfulbits.ui.binding.toolbox.Formatter.*;
import static com.artfulbits.ui.binding.toolbox.Listeners.*;
import static com.artfulbits.ui.binding.toolbox.Models.*;
import static com.artfulbits.ui.binding.toolbox.Views.*;
import static org.hamcrest.core.IsAnything.anything;

/** Login fragment with simplest UI. */
public class PlaceholderFragment extends Fragment implements BindingManager.LifecycleCallback {

  private final BindingManager mBm   = new BindingManager(this).register(this);
  private final User           mUser = new User();

  public PlaceholderFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_main, container, false);

    // custom Binding --> can be done at any point of the lifecycle
    Binder bindLogin = mBm
      .bind(view(matches(root(view), withId(R.id.et_login)), property("text")))
      .model(pojo(mUser, property("login")));

    // update view by model values
    mBm.pop(bindLogin);

    // update model by views values (can be executed more than one rule!)
    mBm.pushByInstance(mUser);

    return view;
  }

  @Override
  public void onCreateBinding(final BindingManager bm) {
    // custom LIFECYCLE step --> BindingManager.LifecycleCallback 
    
    // most robust syntax with all possible configuration chain calls
    final Binder bindPassword = bm
            .bind(view(withId(R.id.et_password), property("text")))
            .model(pojo(mUser, property("pin")))
            .formatter(direct())
            .validator(anything())
            .listenOnModel(none())
            .listenOnView(none());
  }
}
``` 

# Extended lifecycle

![Android Activity/Fragments lifecycle][1]

AEB adding a new step into lifecycle ```onCreateBinding()``` it executed after the ```onAttachedToWindow()``` and before ```onCreateOptionsMenu()```.
 
# Generic Concept Overview

![High Level Data Flow](_documentation/images/binding-overview-data-flow.png)

# Entities, Responsibilities

| Storage |
|---------------------------------|
| store value in specific format; |
| hide storage specifics; |

| Binder |
|---------------------------------------------------|
| define connection between model and view. |
| push and pop value into/from view; |
| attach/detach listeners; |
| Trigger value push on change capture by listener. |

| Formatter |
|----------------------------------------------------------------------------|
| convert storage data type to view data type; |
| apply formatting during convert operation; |
| one-way binding |
| extract value from view data type and \'reverse\' it to storage data type; |

| Validation |
|--------------------------------------------------------------------|
| pre-process data before storing it; |
| validate data limits; |
| attach custom listeners that needs binding results; |
| easy attachable custom logic: master-details, data processing etc. |

| Listeners |
|-----------------------------------------------------------------------------------------------|
| attach specific listeners to the view or storage, for runtime event based binding triggering; |
| ask binding manager for exchange operation |
| time or 'changed' state listeners |

| Property |
|----------------------------------------------------------|
| reflect properties by name. |
| Recognizing names: has\*, is\*, get\*, set\*, exceeds\*; |

| BindingManager |
|----------------------------------------|
| find bindings by view reference; |
| find bindings by storage reference;  |
| force binding push; |
| force validation; |
| force binding pop; |
| freeze/unfreeze binding operations; |
| MAIN/background threads connections; |
| maintain additional lifecycle states; |

# License

    The MIT License (MIT)
    Copyright (c) 2014 Oleksandr Kucherenko
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
