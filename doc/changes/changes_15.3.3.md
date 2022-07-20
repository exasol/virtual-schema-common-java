# Common module of Exasol Virtual Schemas Adapters 15.3.3, released 2022-07-20

Code name: Fixed "set properties" API documentation

## Summary

Version 15.3.3 fixes an error in the documentation of the Virtual Schema API that was located in the "set properties" request. The section with the updated property values was missing. Instead, the documentation claimed that `/schemaMetaData/properties` would contain the new values, which is not correct. You find the current values there.

## Documentation

* 247: Fixed API documentation of "set properties" request

## Dependency Updates

### Test Dependency Updates

* Updated `org.skyscreamer:jsonassert:1.5.0` to `1.5.1`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.5.0`
