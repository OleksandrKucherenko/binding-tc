# Android Binding vs Binding-TC

|                           | Android Binding | Binding-TC |    Bindroid   | AndroidFormEnhancer |
|---------------------------|-----------------|------------|:-------------:|---------------------|
| VERSION                   |     1.0-rc1     |  1.0-beta  | 19 Sept, 2014 |        v1.1.0       |
|                           |                 |            |               |                     |
| Reflection                |        No       |     Yes    |               |                     |
| Obfuscation Supported     |       Yes       |     No     |               |                     |
| Declarative               |       Yes       |     No     |               |                     |
|                           |                 |            |               |                     |
| POJO-to-POJO              |        No       |     Yes    |               |                     |
| View-to-POJO              |       Yes       |     Yes    |               |                     |
|                           |                 |            |               |                     |
| Runtime                   |                 |            |               |                     |
|   Variables               |       Yes       |     No  (1)|               |                     |
|   Custom Binding Class    |       Yes       |     Yes    |               |                     |
|   Expressions             |       Yes       |     No  (2)|               |                     |
|   Includes                |       Yes       |     No     |               |                     |
|   List                    |       Yes       |     Yes    |               |                     |
|   Sparse                  |       Yes       |     No  (3)|               |                     |
|   Map                     |       Yes       |     Yes    |               |                     |
|   Resources               |       Yes       |     No  (4)|               |                     |
|   ViewStub Support        |       Yes       |     No     |               |                     |
|                           |                 |            |               |                     |
| Data Objects              |                 |            |               |                     |
|   Observable POJO         |       Yes       |     Yes    |               |                     |
|   Observable Collections  |       Yes       |     No     |               |                     |
|   Custom Handlers         |       Yes       |     Yes    |               |                     |
|                           |                 |            |               |                     |
| Integration with IDE      |                 |            |               |                     |
|   Design Time support     |                 |            |               |                     |
|   Gradle build step       |                 |            |               |                     |
|                           |                 |            |               |                     |
|                           |                 |            |               |                     |

(1) - Selector abstraction easily can replace this and provide more flexible way;

(2) - No custom-syntax expression, I realign on Java code and business logic;

(3) - No specific implementation available, Selector can easily cover any type of interface for accessing the data;

(4) - Should be implemented over custom Formatting which converts int-to-resource;



[1]: https://developer.android.com/intl/ru/tools/data-binding/guide.html
[2]: https://github.com/depoll/bindroid
[3]: https://github.com/ksoichiro/AndroidFormEnhancer

[99]: http://www.tablesgenerator.com/markdown_tables
