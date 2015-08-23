# Known Problems

## CharSequence and String

Android Views used CharSequence interface for representing texts on screens. Biggest
problem is that instance that used in TextView (called: SpannedString) is a high level
abstraction that wraps 'char sequence' in 'formatting'. During data exchange View and Model
will share the same instance of CharSequence implementation. And as a result binding will
become broken.

**Follow the simple rule:**
Model should stores data on more low-level abstractions - primitives. That is why they called
POJOs. Model should store 'copy' of data, not a reference. If you follow this rule - less hours
you will spent on debugging.

**Ridges** - this is a special abstraction designed for keeping isolation of Model and View.

Implementors of ```Binder.Ridge<T>``` interface should keep in mind two things:
1) Ridges are responsible for value caching and change detection;
2) Second thing - clone the value, executed only if change detected. Ridge should create a
deep copy of provided value (or guaranty somehow that view and model do not share the same
instance).

**Why Ridges? Why Not Formatter?**
Main reasons:
- they are executed on different levels of PUSH/POP operations;
- additional abstraction allows to be more flexible and do not mix
different logical aspects in one method implementation.

**Should I implement own Ridges?**
Better to use already designed in Java 'deep copy' things: ```Serializable``` and
```Cloneable```. Implement on of those interfaces (or both) and default implementation
of ```Ridge<T>``` will do the job for you.

## Generics and T type defining

Library use a reflection trick for detecting the ```<T>``` data type. But trick does not
work for all cases. Possible situation when type will not be resolved. All is left - is
to use verbose Java syntax for defining the data type. No other solutions.

## Core- and Toolbox-

Its not a problem. Its a concept of project structuring. Please follow it.

**Toolbox** - is a set of classes that implements concrete typed versions of generics, or do
pre-configuration for common cases. Please keep it so. Toolbox should keep minimal static
memory usage. All instances are created in runtime on first call.

**Core** - pure abstractions and algorithms. If you see some generic things, than CORE is a good
place for keeping them.

## Logs and how to trace things

Not implemented yet.

The main cases when you will need tracing - is a detection of infinite loops. Library designed in
way you will not get a 'stack overflow', but will start to run data-exchange login in infinite loop
and will drain battery quickly. How that happens?!

```
onViewChanged --> PUSH [ View -- Format -- Validate -- Ridge -- Model ] --> onModelChanged

onModelChanged --> POP [ Model -- Ridge -- Validate -- Format -- View ] --> onViewChanged
```

This is the full lifecycle loop, in it ```Ridge``` is responsible for breaking the infinite loops.
```Ridge``` should detect real data change and if its NOT CHANGED - than break the loop.

In addition ```Molds``` (formatting) can be used for reducing the pain. Use ```onlyPop``` or
```onlyPush``` methods.