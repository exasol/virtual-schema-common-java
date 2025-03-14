# System Requirement Specification (SRS) Virtual Schema Common Java (VSCJ) 

This document contains the system requirements for the Java bottom-layer base library for all Exasol Virtual Schema adapters.

## Terms and Abbreviations

###### Database Object ID

A unique identifier used to reference specific objects in a database.

###### Dialect

A specific implementation or variation of features, behaviors, or rules for interacting with a given data source or database type in the context of Virtual Schemas.

###### Virtual Schema

A database schema that only exists as a projection of an external data source. The data in the virtual schema is not permanently stored on the Exasol database.

###### Virtual Schema Property

A configuration option that controls the structure or behavior of a [virtual schema](#virtual-schema).

###### VS

See [Virtual Schema](#virtual-schema)

###### VSCJ

The abbreviation for "Virtual Schema Common Java", the base library designed for all Java Exasol Virtual Schema adapters.

## Features

### Property Validation
`feat~property-validation~1`

The VSCJ library provides the infrastructure for validating the input coming from [virtual schema properties](#virtual-schema-property).

Rationale:

The validation on the one hand provides users with better information about properties that have been set incorrectly and on the other hand improves the security by preventing faulty or malicious property values to endanger the VS operation.

Needs: req

## High-level Requirements

### Property Validation

Users provide properties with virtual schema definitions that serve as configuration.

Here is a non-exhaustive list of typical properties that should give an idea of what they are used for:

1. Name under which the virtual schema can be found in the Exasol database
2. ID of the connection object that stores the access data to the data source
3. Log level
4. Target host for logs
5. Connection type selector
6. Feature switch

Please refer to [Appendix A — Known Virtual Schema Property Types](#appendix-a--known-virtual-schema-property-types) for a more complete list.

Note that some of these properties have relationships that require to validate them together. In the Exasol Virtual Schema for instance, the selected connection type decides which other connection properties are required.

Also, some of the validations are so [dialect-specific](#dialect), that covering them in the VSCJ base library is not reasonable.

#### Validating the Existence of Mandatory Properties
`req~validating-the-existence-of-mandatory-properties~1`

If a virtual schema property is mandatory, the VSCJ allows validating that it is set.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating That an Optional Property Is Allowed
`req~validating-that-an-optional-property-is-allowed~1`

If a virtual schema property is optional, VSCJ checks that the given property is known.

Rationale:

It is a subtle source of errors if users provide a property that is not wanted by the virtual schema. Ignoring it is not a good option, since that has the potential to confuse the users. Imagine a situation where they misspell the property name. An error message would immediately tell them that this is not the property name they intended to provide.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Boolean Properties
`req~validating-boolean-properties~1`

VSCJ checks whether the value of a boolean property is `true` or `false`.

Rationale:

Boolean properties are often used as feature switches.

Covers:

* [`feat~property-validation~1`](#property-validation)  

Needs: req

#### Validating Integer Properties
`req~validating-integer-properties~1`

VSCJ checks whether a property is

- a proper integer value
- withing the allowed interval

Rationale:

This is useful for configurations like a port range for example.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Properties Containing Database Object IDS
`req~validating-properties-containing-database-objects-ids~1`

VSCJ validates that a property referencing a database object contains a valid [Exasol database object ID](https://docs.exasol.com/saas/sql_references/basiclanguageelements.htm#SQLidentifier).

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating String Property Values
`req~validating-string-property-values~1`

VSCJ validates a string property against a given pattern.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Properties Referencing a Host
`req~validating-properties-referencing-a-host~1`

VSCJ validates that a property contains a valid IP address or host name.

Rationale:

Almost all virtual schemas will point to a network service on a remote host, for example databases, cloud storage or general web services.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Properties with Enumerations
`req~validating-properties-with-enumerations~1`

VSCJ validates that a property contains a value from a predefined set of valid enumeration values.

Rationale:

Certain properties must only accept specific predefined values to avoid configuration errors and ensure consistent behavior across schemas. For instance, a property representing a datatype mapping option.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Properties with Multi-Select Enumerations
`req~validating-properties-with-multi-select-enumerations~1`

VSCJ validates that a property contains a comma-separated list of values, each belonging to the predefined set of valid enumeration values.

Rationale:

Some properties may allow multiple selections from a set of predefined options. For example, `EXCLUDED_CAPABILITIES` might specify features to disable, such as `ABS`, `REPEAT`, or `UNICODE`. Validating that all values in the list are part of the allowed enumeration prevents misconfiguration and ensures proper behavior.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validating Properties With Unix Paths
`req~validating-properties-with-unix-paths~1`

VSCJ validates that a property contains a valid Unix path.

Rationale:

Document-based virtual schemas need a mapping file that is located in BucketFS.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

#### Validation Dependencies
`req~validation-dependencies~1`

VSCJ allows an adapter for an SQL [dialect](#dialect) to enforce dependent validations.

Rationale:

Not all properties exist in isolation. In the Exasol virtual schema for example, the required connection properties depend on the selected connection options.
Note that the relationships can be complicated and highly dialect-specific, so any attempt to cover everything in VSCJ is bound to fail. Instead, we must only make sure in VSCJ that dialects can combine validations in a flexible way. Details and constraints are up to the [software design](design.md).

Covers:

* [`feat~property-validation~1`](#property-validation)


Needs: req



### Known Limitations

* For now, we limit the property validation to the value syntax and ranges.
* During value validation, we do not check if the referenced database objects actually exist.

## Appendix A — Known Virtual Schema Property Types

To make sure we don't forget requirements, here is a list of all known Virtual Schema properties as of 2025-03-13 and their types.

Please note that the list will be outdated at some point. Its main purpose is to get a good sample of all property variants.

| Property Name                                           | Virtual Schema Dialect       | Type                                  | O/M |
|---------------------------------------------------------|------------------------------|---------------------------------------|-----|
| CATALOG_NAME                                            | Multiple dialects            | Database object ID (dialect-specific) | O   |
| CONNECTION_NAME                                         | All                          | Exasol database object ID             | M   |
| DEBUG_ADDRESS                                           | All                          | &lt;host&gt;:&lt;port&gt;             | O   |
| DEBUG_LEVEL                                             | All                          | Enumeration                           | O   |
| EXA_CONNECTION_NAME                                     | Exasol                       | Exasol database object ID             | O   |
| EXCLUDED_CAPABILITIES                                   | All                          | Multi-select enum, comma-separated    | O   |
| GENERATE_JDBC_DATATYPE_MAPPING_FOR_EXA                  | Exasol                       | Boolean                               | O   |
| GENERATE_JDBC_DATATYPE_MAPPING_FOR_OCI                  | Oracle                       | Boolean                               | O   |
| IGNORE_ERRORS                                           | All (for debugging purposes) | Boolean                               | O   |
| IMPORT_FROM_EXA                                         | Exasol                       | Boolean                               | O   |
| IMPORT_FROM_ORA                                         | Oracle                       | Boolean                               | O   |
| IS_LOCAL                                                | Exasol                       | Boolean                               | O   |
| MAPPING                                                 | Azure Blob Storage           | Unix file path                        | M   |
| MAX_PARALLEL_UDFS                                       | Azure Blob Storage           | Integer                               | O   |
| ORA_CONNECTION_NAME                                     | Oracle                       | Exasol database object ID             | O   |
| ORACLE_CAST_NUMBER_TO_DECIMAL_WITH_PRECISION_AND_SCALE  | Oracle                       | &lt;Integer&gt;,&lt;Integer&gt;       | O   |
| POSTGRESQL_IDENTIFIER_MAPPING                           | PostgreSQL                   | Enum                                  | O   |
| TABLE_FILTER                                            | All (optional filtering)     | Comma-separated list of object IDs    | O   |
| SCHEMA_NAME                                             | Multiple dialects            | Database object ID (dialect-specific) | M   |

You can see from the list that some property types are universal and others are highly dialect specific. And Exasol database object ID is only superficially similar to one of MySQL or PostgreSQL.

Outdated properties, i.e. properties that don't play any role anymore or are already removed:

* `DIALECT_NAME` — obsolete since now each dialect has its own JAR package