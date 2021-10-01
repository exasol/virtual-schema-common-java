# Common module of Exasol Virtual Schemas Adapters 15.2.1, released 2021-10-01

Code name: AggregationType Support

## Summary

In this release we fixed the support for the `aggregationType` field in requests. In addition, we replaced `javax.json` by `jakarta.json`.

## Refactoring

* #237: Replaced `javax.json` by `jakarta.json`

## Bugs

* #229: Added missing parsing for the `aggregationType` field of the request.

## Dependency Updates

### Compile Dependency Updates

* Added `jakarta.json:jakarta.json-api:2.0.1`
* Removed `org.glassfish:javax.json:1.1.4`

### Runtime Dependency Updates

* Added `org.glassfish:jakarta.json:2.0.1`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.6` to `3.7.1`
* Updated `org.junit.jupiter:junit-jupiter:5.7.2` to `5.8.1`
* Updated `org.mockito:mockito-junit-jupiter:3.10.0` to `3.12.4`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.0` to `0.6.0`
* Updated `com.exasol:project-keeper-maven-plugin:0.6.1` to `1.2.0`
