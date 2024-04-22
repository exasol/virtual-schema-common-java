# Common Module of Exasol Virtual Schemas Adapters 17.1.0, released 2024-04-23

Code name: Add `WIDTH_BUCKET`

## Summary

This release adds support for scalar function [`WIDTH_BUCKET`](https://docs.exasol.com/db/latest/sql_references/functions/alphabeticallistfunctions/width_bucket.htm) and capability `FN_WIDTH_BUCKET` by adding the following enum values:
* `ScalarFunctionCapability.WIDTH_BUCKET`
* `ScalarFunction.WIDTH_BUCKET`

The release also adds support for `TIMESTAMP` precision.

**Breaking Changes:** This release removes the following deprecated fields/methods from class `com.exasol.adapter.AdapterProperties`:
* Constant `EXCEPTION_HANDLING_PROPERTY`
* Method `getExceptionHandling()`
* Method `hasExceptionHandling()`

## Breaking Changes

* #269: Removed deprecated exception handling fields/methods from `AdapterProperties`

## Features

* #279: Added support for `TIMESTAMP` precision field `fractionalSecondsPrecision` with default 3.
* #278: Added support for scalar function `WIDTH_BUCKET`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.3` to `1.0.5`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.3` to `3.16.1`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.11.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.1` to `2.0.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `4.3.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.11.0` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.5`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.5.0` to `1.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`
