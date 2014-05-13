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
import static com.truecaller.ui.binding.Validations.Anything; 
``` 
