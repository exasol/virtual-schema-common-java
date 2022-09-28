# Common module of Exasol Virtual Schemas Adapters 16.1.3, released 2022-09-??

Code name: Fixed Geo Aggregation capabilities

## Summary

We deprecated the `FN_AGG_GEO_*` capabilities that were renamed in 7.1.alpha1. Please only use their new `FN_AGG_ST_*`
counterparts.

Additionally, we added an explanation of [dual-use functions and their capabilities](https://github.com/exasol/virtual-schema-common-java/blob/main/doc/development/api/capabilities_list.md#capabilities-for-dual-use-functions) to the developer guide.

We also updated dependencies.

## Bugfixes

* #262: Clarified geometry capabilities.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `jakarta.json:jakarta.json-api:2.1.0` to `2.1.1`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.10` to `3.10.1`
* Updated `org.junit.jupiter:junit-jupiter:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.8.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.7.0` to `2.8.0`
