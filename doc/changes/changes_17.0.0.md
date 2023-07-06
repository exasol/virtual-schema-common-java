# Common Module of Exasol Virtual Schemas Adapters 17.0.0, released 2023-07-06

Code name: Removed Adapter Property BINARY_COLUMN_HANDLING

## Summary

This release updates dependencies and removes adapter property `BINARY_COLUMN_HANDLING` since it was never implemented by Exasol database up to now. As the property also was never used by any virtual schema in GitHub org `exasol` the removal is expected to go without any consequences.

Still this release updates the major version of VSCOMJAVA to signal potentially breaking changes in its public API.

## Features

* #268: Removed adapter property `BINARY_COLUMN_HANDLING`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `com.exasol:udf-api-java:1.0.1` to `1.0.2`
* Updated `jakarta.json:jakarta.json-api:2.1.1` to `2.1.2`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.12` to `3.14.3`
* Updated `org.junit.jupiter:junit-jupiter:5.9.1` to `5.9.3`
* Updated `org.mockito:mockito-junit-jupiter:4.9.0` to `5.4.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.3`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.1` to `2.9.7`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.10.1` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.3.0` to `1.4.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.15.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.8` to `0.8.9`
