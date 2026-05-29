# Common Module of Exasol Virtual Schemas Adapters 18.0.2, released 2026-??-??

Code name:

## Summary

## Breaking Changes

* Constructor `ColumnMetadata.Builder()` is now deprecated for removal. Use `ColumnMetadata.builder()` to create a new instance (#306).

## Bugfixes

* #306: Fixed `TablesMetadataParser` so omitted `isIdentity` values default to `false` while omitted `isNullable` values still default to `true`.

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.5.2` to `5.6.2`
