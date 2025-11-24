# Common Module of Exasol Virtual Schemas Adapters 17.1.2, released 2025-11-24

Code name: Fix for wrong COUNT and HAVING handling in empty group by.

## Summary

This release fixes inconsistent result of COUNT and HAVING statements in empty group by.

## Features

* #292: [L3-3596] COUNT(*) returns NULL from an empty group by
* #297: Wrong handling of aggregate functions in HAVING statement

