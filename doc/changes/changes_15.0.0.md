# Virtual Schema Common Java 15.0.0, released 2021-02-??

Code name: 

In release 15.0.0, we optimized the deployment so that each dialect gets its own jar. With this change, also the `SQL_DIALECT` property used for when executing a `CREATE VIRTUAL SCHEMA` command from Exasol database is obsolete.

## Information for Developers

Since we limited the number of `Virtual Schema Adapter` to be loaded to one, please note the following breaking changes in
design and interface:

* `com.exasol.adapter.AdapterRegistry` class was removed.

## Bug Fixes

* #209: Fixed wrong values format of literal_exactnumeric and literal_double.

## Documentation

* #208: Fixed API Documentation for literal_exactnumeric and literal_double.
* #186: Updated select list API description.

## Refactoring

* #189: Unified `parseInvolvedTables` method in the `RequestParser`.
* #195: Added a builder for unified error handling.
* #205: Removed code duplication in the `AdapterCallExecutor`.
* #206: Refactored building phase of the application.
* #211: Removed `AdapterName` property from `AdapterRequest`.

## Dependency updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.1.0`