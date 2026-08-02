# Common Module of Exasol Virtual Schemas Adapters 18.0.4, released 2026-08-03

Code name: Support legacy nullable metadata

## Summary

This release restores compatibility with virtual-schema metadata persisted before Exasol 2025.1.12.
Fixed `TablesMetadataParser` so an explicit JSON `null` for optional boolean metadata, including `isNullable`, uses the field's established default value.

## Bugfixes

* #337: Handle null isNullable metadata after DB upgrade to 2025.1.12

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.4`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.4`
