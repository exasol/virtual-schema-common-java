# Common Module of Exasol Virtual Schemas Adapters 18.0.0, released 2025-03-??

Code name: Property Validation Rework

## Summary

In this release we reworked the Virtual Schema property validation infrastructure. Many parts were in VS Common JDBC, although they are relevant for virtual schemas.

* Moved all virtual schema property related classes to new package `com.exasol.adpater.properties` to make locating them easier.

## Features

* #284: New property validation framework

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.0` to `4.5.0`
