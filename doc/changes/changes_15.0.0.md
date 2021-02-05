# Virtual Schema Common Java 15.0.0, released 2021-02-05

Code name: 

In release 15.0.0, we optimized the deployment so that each dialect gets its own jar. With this change, also the `SQL_DIALECT` property used for when executing a `CREATE VIRTUAL SCHEMA` command from Exasol database is obsolete.

## Information for Developers

Since we limited the number of `Virtual Schema Adapter` to be loaded to one, please note the following breaking changes in
design and interface:

The following classes where removed:
* `src/main/java/com/exasol/adapter/AdapterFactory.java`
* `src/main/java/com/exasol/adapter/AdapterRegistry.java`

The `src/main/resources/META-INF/services/com.exasol.adapter.AdapterFactory` file used to specify the `AdapterFactory` to be loaded is no longer required. Instead a the `src/main/resources/META-INF/services/com.exasol.adapter.VirtualSchemaAdapter` file that specifies the `VirtualSchemaAdapter` to be loaded is required.

## Bug Fixes

* #209: Fixed wrong values format of literal_exactnumeric and literal_double.

## Documentation

* #208: Fixed API Documentation for literal_exactnumeric and literal_double.

## Refactoring

* #189: Unified `parseInvolvedTables` method in the `RequestParser`.
* #195: Added a builder for unified error handling.
* #205: Removed code duplication in the `AdapterCallExecutor`.
* #206: Refactored building phase of the application.
* #211: Removed `AdapterName` property from `AdapterRequest`.

## Dependency updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.1.0`