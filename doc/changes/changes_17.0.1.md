# Common Module of Exasol Virtual Schemas Adapters 17.0.1, released 2023-11-17

Code name: Improve logging

## Summary

This release logs responses for adapter calls at level `FINER` to simplify debugging and makes `LoggingConfiguration` serializable to allow configuring other components that require serializing.

## Features

* #276: Improved logging

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:udf-api-java:1.0.2` to `1.0.3`
* Updated `jakarta.json:jakarta.json-api:2.1.2` to `2.1.3`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15.3`
* Updated `org.junit.jupiter:junit-jupiter:5.9.3` to `5.10.1`
* Updated `org.mockito:mockito-junit-jupiter:5.4.0` to `5.7.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.7` to `2.9.16`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.2.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
