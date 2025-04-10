# Features

## Property Validation
`feat~property-validation~1`

The VSCJ library provides the infrastructure for validating the input coming from virtual schema properties.

Rationale:

The validation on the one hand provides users with better information about properties that have been set incorrectly and on the other hand improves the security by preventing faulty or malicious property values to endanger the VS operation.

Needs: req

# High-level Requirements

### Validating the Existence of Mandatory Properties
`req~validating-the-existence-of-mandatory-properties~1`

VSCJ allows validating that a mandatory virtual schema property is set.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating the Absence of Unwanted Properties
`req~validating-the-absence-of-unwanted-properties~1`

VSCJ allows validating that an unwanted property is not set.

Rationale:

This is for cases where the same dialect can do different things depending on the configuration. In such a scenario we want to make sure that users only provide the parameter that are actually evaluated. This prevents confusion on the user's part. 

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn



### Validating That an Optional Property Is Allowed
`req~validating-that-an-optional-property-is-allowed~1`

VSCJ allows validating that a property the user provides is a valid optional property.

Rationale:

It is a subtle source of errors if users provide a property that is not wanted by the virtual schema. Ignoring it is not a good options, since that has the potential to confuse the users. Imaging a situation where they misspell the property name. An error message would immediately tell them that this is not the property name they intended to provide.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating Boolean Properties
`req~validating-boolean-properties~1`

VSCJ checks whether the value of a boolean property is `true` or `false`.

Rationale:

Boolean properties are often used as feature switches.

Covers:

* [`feat~property-validation~1`](#property-validation)  

Needs: dsn

### Validating Integer Properties
`req~validating-integer-properties~1`

VSCJ checks whether a property is

- a proper integer value
- withing the allowed interval

Rationale:

This is useful for configurations like a port range for example.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating Properties Containing Database Object IDS
`req~validating-properties-containing-database-objects-ids~1`

VSCJ validates that a property referencing a database object contains a valid Exasol database object ID.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating Enumeration Properties
`req~validating-enumeration-properties~1`

VSCJ validates that the value given by the user is one of the values in an enumeration.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating a String Against a Regular Expression
`req~validating-a-string-against-a-regular-expression~1`

VSCJ validates string values against a regular expression.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Validating Combinations of Properties
`req~validating-combinations-of-properties~1`

VSCJ allow validating combinations of properties where property values can depend on others.

Rationale:

Consider a virtual schema dialect for a source like MariaDB. You have shared properties like log settings on the base layer, JDBC settings in the JDBC layer and finally dialect specifics like handling of the identifier case. The dialect will only function properly if all properties are set correctly.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn

### Property Validation Completeness
`req~property-validation-completeness~1`

VSCJ checks that all properties given by the virtual schema user are validated.

Rationale:

This helps to prevent typos, using outdated properties and properties from the wrong dialect. 

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: dsn



