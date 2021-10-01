# Common module of Exasol Virtual Schemas Adapters 15.2.1, released 2021-10-01

Code name: AggregationType Support

## Summary

In this release we fixed the support for the `aggregationType` field in requests. In addition, we replaced `javax.json` by `jakarta.json`.

## Refactoring

* #237: Replaced `javax.json` by `jakarta.json`

## Bugs

* #229: Added missing parsing for the `aggregationType` field of the request.

## Dependency Updates
