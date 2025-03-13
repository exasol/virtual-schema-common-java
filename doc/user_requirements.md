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

Needs: req

### Validating That an Optional Property Is Allowed
`req~validating-that-an-optional-property-is-allowed~1`

VSCJ allows validating that a property the user provides is a valid optional property.

Rationale:

It is a subtle source of errors if users provide a property that is not wanted by the virtual schema. Ignoring it is not a good options, since that has the potential to confuse the users. Imaging a situation where they misspell the property name. An error message would immediately tell them that this is not the property name they intended to provide.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

### Validating Boolean Properties
`req~validating-boolean-properties~1`

VSCJ checks whether the value of a boolean property is `true` or `false`.

Rationale:

Boolean properties are often used as feature switches.

Covers:

* [`feat~property-validation~1`](#property-validation)  

Needs: req

### Validating Integer Properties
`req~validating-integer-properties~1`

VSCJ checks whether a property is

- a proper integer value
- withing the allowed interval

Rationale:

This is useful for configurations like a port range for example.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

### Validating Properties Containing Database Object IDS
`req~validating-properties-containing-database-objects-ids~1`

VSCJ validates that a property referencing a database object contains a valid Exasol database object ID.

Covers:

* [`feat~property-validation~1`](#property-validation)

Needs: req

