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

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.7.0` to `2.8.0`
