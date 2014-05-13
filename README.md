binding-tc
==========

Android View properties binding to the Business Objects (POJO). 

Goals
======
* High performance, 
* easy syntax, 
* minimalistic approach, 
* highly customizable,
* hamcrest validation syntax,
* 100% covered by unit tests
 
Concept Overview
================

![High Level Data Flow](_documentation/images/binding-overview-data-flow.png)

Binding In Details
==================

![Data Flow inside the Binding Library](_documentation/images/binding-detailed-data-flow.png)

# Entities, Responsibilities
| Binder | Formatter |
|--------|-----------|
| extract getter and setter by reflection; | convert storage data type to view data type; |
| push and pop value into/from view; | apply formatting during convert operation; |
| attach/detach listeners; | extract value from view data type and \'reverse\' it to storage data type; |
| Trigger value push on change capture by listener. | |

| Validation | Storage |
|------------|---------|
| pre-process data before storing it; | store value in specific format; |
| validate data limits; | hide storage specifics; |
| attach custom listeners that needs binding results; |

| Listeners | Extractor |
|-----------|-----------|
| attach specific listeners to the view or storage, for runtime event based binding triggering; | reflect properties by name. Recognizing names: has\*, is\*, get\*, set\*, exceeds\*; |

| Manager |
|---------|
| find bindings by view reference; |
| find bindings by storage reference;  |
| force binding push; |
| force validation; |
| force binding pop; |
| global listeners; |

# Example of Usage
```java
import com.truecaller.ui.binding.*;

import static com.truecaller.ui.binding.Validations.anything; 
import static com.truecaller.ui.binding.Formatting.default;
import static com.truecaller.ui.binding.Storage.property;
import static com.truecaller.ui.binding.Storage.pojo;
import static com.truecaller.ui.binding.Listeners.onTextChange;

public class LoginFragment extends Fragment implements BindingManager.Callback {

  private final BindingManager mBinder = BindingManager.getInstance(this);
  private final User mUser = new User();

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    // read line:
    // bind property "get/set Text" of view with id R.id.txtLogin to property of Pojo storage object
    // validation pass anything, convert all to String, and listen for text changes
    mBinder.view(withId(R.id.tv_Login)) /* Note: reference on fragment we got during instance creation. */
      .bind(property("text"))
      .storage(pojo(mUser).property("login"))
      .validate(anything())     /* optional: applied automatically */
      .formatting(default())    /* optional: applied automatically */
      .listen(onTextChange());  /* optional: by default we listen 'focus loss' */
      
    // normal way of usage, binded 'Text'-to-'Password'
    mBinder.textview(withId(R.id.tv_Password)).storage(pojo(mUser).property("password")));

    // initialization done, force Views update
    mBinding.updateViews();     /* optional: we listen on visibility change */
  }
  
  public static class User{
    private String mLogin;
    private String mPassword;
  
    public String getLogin(){ return mLogin; }
    public String getPassword(){ return mPassword; }
  }
}
``` 
