# Virtual Schema Common Java 14.0.0, released 2020-12-23

Code name: Replaces SELECT * with an explicit columns list.

## Feature

* #188: Create a select columns list from the involved tables list when the select list is missing.

## Documentation

* #190: Improved `SELECTLIST_EXPRESSIONS` documentation
* #197: Improved AGGREGATE_GROUP_BY_COLUMN, AGGREGATE_GROUP_BY_EXPRESSION and AGGREGATE_GROUP_BY_TUPLE capabilities documentation.

## Refactoring

* #196: Validated group concat argument is not null.