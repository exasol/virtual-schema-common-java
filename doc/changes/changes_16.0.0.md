# Common module of Exasol Virtual Schemas Adapters 16.0.0, released 2022-09-09

Code name: Evaluate expected resultset datatypes

## Summary

Starting with major version 8 Exasol database uses the capabilities reported by each virtual schema to provide select list data types for each push down request. Based on this information the JDBC virtual schemas no longer need to infer the data types of the result set by inspecting its values. Instead the JDBC virtual schemas can now use the information provided by the database.

This provides the following benefits:
* Improved performance of queries to virtual schema by avoiding one query for each push down
* Enhanced accuracy of data type mapping
* Simplified data type mapping which is easier to extend
* Support for additional use cases

## Features

* #249: Evaluate expected resultset datatypes

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.5.0` to `2.7.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
