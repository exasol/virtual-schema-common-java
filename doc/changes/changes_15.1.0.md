# Common module of Exasol Virtual Schemas Adapters 15.1.0, released 2021-08-06

Code name: SqlNode serialization

## Summary

In this release we added a renderer for the `SqlNode` structure.

## Features

* #230: Added serializer for SqlNode

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.2.2` to `0.4.0`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.4.3` to `3.7`
* Updated `org.itsallcode:junit5-system-extensions:1.1.0` to `1.2.0`
* Updated `org.junit.jupiter:junit-jupiter:5.7.0` to `5.7.2`
* Updated `org.mockito:mockito-junit-jupiter:3.5.13` to `3.11.2`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.0` to `0.5.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.13`
