# Common Module of Exasol Virtual Schemas Adapters 18.0.3, released 2026-06-08

Code name: Fix locale-dependent request serialization

## Summary

This release consistently uses `Locale.ROOT` for converting strings to upper/lower case. This avoids locale dependent bugs.

## Bugfixes

* #335: Fixed incomplete `Locale.ROOT` hardening in request parsing and rendering. Locale-sensitive casing is now avoided when parsing table metadata and `IS JSON` constraints, and when rendering pushdown JSON for node types, data types, character sets, and join types.
