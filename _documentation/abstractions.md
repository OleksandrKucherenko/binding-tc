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

| Binders |
|-----------------------------------------|
| Pre-configured typed versions of Binder |

| Formatter |
|----------------------------------------------------------------------------|
| convert storage data type to view data type; |
| apply formatting during convert operation; |
| one-way binding |
| extract value from view data type and \'reverse\' it to storage data type; |

| Molds |
|----------------------------------------------------------------------|
| Typical formatting cases: one-way formatting, string-to-number, etc. |
| Typed versions of formatting and helper methods for manipulations |

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

| Models |
|-------------------------------------------------------------------|
| Typed versions of Property and Selectors for primitive data types |

| Views |
|------------------------------------------------------------------|
| Typed versions of Property and Selectors for Android UI elements |

| Ridge |
|-------------------------------|
| isolate model and view from each other by creating a clone instead of sharing the reference on same instance |

| Ridges |
|----------------------------------------------------|
| Typical implementation of ridge, simplest approach |

| Adapters |
|----------------------------------------------------------|
| Helpers for implementing binding inside Android Adapters |

| BindingManager |
|----------------------------------------|
| find bindings by view reference; |
| find bindings by tag id; |
| find bindings by storage reference;  |
| force binding push; |
| force validation; |
| force binding pop; |
| freeze/unfreeze binding operations; |
| MAIN/background threads connections; |
| maintain additional lifecycle states; |
