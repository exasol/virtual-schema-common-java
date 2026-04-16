# Common Module of Exasol Virtual Schemas Adapters 18.0.0, released 2026-??-??

Code name: Add feature tracking

## Summary

This release adds anonymous feature tracking using the [telemetry-java](https://github.com/exasol/telemetry-java) library. When you integrate this new version into another product, please observe the [required user documentation](https://github.com/exasol/telemetry-java/blob/main/doc/integration-guide.md#required-documentation).

## Features

* #300: Add anonymous feature tracking

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:udf-api-java:1.0.5` to `1.0.8`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.19.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.itsallcode:junit5-system-extensions:1.2.0` to `1.2.2`
* Added `org.junit.jupiter:junit-jupiter-api:5.14.3`
* Removed `org.junit.jupiter:junit-jupiter:5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.11.0` to `5.23.0`
* Updated `org.skyscreamer:jsonassert:1.5.1` to `1.5.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.5` to `2.0.6`
* Updated `com.exasol:project-keeper-maven-plugin:5.4.3` to `5.4.6`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.1` to `3.15.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.21.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.9.0` to `0.10.0`
