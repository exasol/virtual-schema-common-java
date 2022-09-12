# Common module of Exasol Virtual Schemas Adapters 16.1.0, released 2022-??-??

Code name: Fixed data type properties

## Summary

This release uses correct json encoding of some properties for data tytes.

| Datat Type, Property | Old implementation | Fix |
| --- | --- | --- |
| HASHTYPE, byteSize | `byteSize` | json key `bytesize` with lower case "s" |
| GEOMETRY, srid | json key `scale` | json key `srid` |
| INTERVAL  | | Evaluate value of json key `fromTo` and use either `DataType.createIntervalDaySecond()` or `DataType.createIntervalYearMonth()` |

## Bug fixes

* 253: Fixed data type properties

