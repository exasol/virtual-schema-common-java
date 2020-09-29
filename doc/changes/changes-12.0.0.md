# Virtual Schema Common Java 12.0.0, released 2020-09-29

Code name: New capabilities, refactored API

## Summary

In this release we added the following capabilities: 
`FN_BIT_LROTATE`, `FN_BIT_RROTATE`, `FN_BIT_LSHIFT`, `FN_BIT_RSHIFT,` `FN_FROM_POSIX_TIME`, `FN_HOUR`, `FN_INITCAP`, `FN_AGG_EVERY`, `FN_AGG_SOME`, `FN_AGG_MUL_DISTINCT`.

We have removed the capabilities that are not used in the API: `FN_HASH_SHA`
                                                     
## Features / Enhancements

* #147: Added new capabilities.

## Documentation

* #149: Moved the API documentation to this repository.

## Refactoring

* #152: Refactored the module according to the changes in the API: 
 - removed usage of `numArgs` field in scalar function definitions;
 - removed usage of `variableInputArgs` field in scalar function definitions;
 - removed usage of prefix and infix fields in scalar function definitions;
* #154: Removed `toSimpleSql()` method and its implementation because it was not used.

## Dependency updates

* Updated org.junit.jupiter:junit-jupiter:jar:5.6.2 to version 5.7.0
* Updated org.mockito:mockito-junit-jupiter:jar:3.4.4 to version 3.5.13
* Updated nl.jqno.equalsverifier:equalsverifier:jar:3.4.1 to version 3.4.3