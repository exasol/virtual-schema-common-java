# Common Module of Exasol Virtual Schemas Adapters 18.0.2, released 2026-??-??

Code name: Improve code quality

## Summary

## Breaking Changes

* `Capabilities.subtractCapabilities()` is now deprecated for removal and delegates to the new pure `Capabilities.subtract()` implementation. Code that relied on the previous side effect of mutating the receiver must be adapted (#305).
* Constructor `ColumnMetadata.Builder()` is now deprecated for removal. Use `ColumnMetadata.builder()` to create a new instance (#306).
* Constructor `Capabilities.Builder()` is now deprecated for removal. Use `Capabilities.builder()` to create a new instance (#305).
* Constructor `SqlStatementSelect.Builder()` is now deprecated for removal. Use `SqlStatementSelect.builder()` to create a new instance (#306).

## Bugfixes

* #305: Fixed `Capabilities.subtractCapabilities()` so it no longer mutates the receiver. Added the pure `Capabilities.subtract()` method.
* #306: Fixed `TablesMetadataParser` so omitted `isIdentity` values default to `false` while omitted `isNullable` values still default to `true`.
* #310: Fixed `SqlStatementSelect.Builder.build()` so empty and partial builders no longer fail with a raw `NullPointerException`.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.8` to `1.0.9`

### Test Dependency Updates

* Updated `org.itsallcode:junit5-system-extensions:1.2.2` to `1.2.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.3` to `5.14.4`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.5.2` to `5.6.2`
