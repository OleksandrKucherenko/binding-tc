# Roadmap

## Serialization

1. inflate binding to/from XML
2. inflate binding to/from JSON

## Property v2

1. implement version of Property class that instead of reflection will generate dynamic DEX code
2. Property resolve strategy:
- special abstraction that will on high level define how property will resolve searches of getter/setter

## Selector v2

~~1. Implement selectors from MAPs~~

~~2. Implement selectors from Array~~

~~3. Implement selectors from List~~

~~4. Simplify nested/chained selectors declaration~~

5. clone selector with runtime instance replacement (apply the same configuration on another instance)
6. Expression IF/ELSE in logic, quick switches
7. Implement selector from SparseArray

## ~~Formatting~~ **DONE!** Implemented

~~1. Chained formatting, when we have two-, three- stage convert of data, Examples:~~

~~- CharSequence --> String --> StripString~~

~~- String --> Boolean --> Integer~~

## Validations

1. Master-Details binds
2. Custom EVENT/COMMAND raisers/sending to MAIN thread

## Extended Toolbox

1. implement for all Android standard UI controls binidngs methods
~~2. implement data adapter binding~~
3. implement groupped data adapter binidng

## Obfuscation strategy

Find the way how to prevent obfuscation issue in release builds:
- custom annotations
- generated classes that will help binding manager to resolve obfuscated class names (mapping.txt to constants)
- proguard keep rules generator
