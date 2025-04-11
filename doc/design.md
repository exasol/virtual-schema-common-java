## Building Block View

### Property Validator Building Blocks

The UML diagram below shows the relationship between the factory for the validators, the abstract base class and a concrete validator.

```plantuml
hide empty members

interface PropertyValidator {
   * validate() : PropertyValidationResult
   * getPropertyName() : String
   * getErrorCode() : String
}

class AbstractPropertyValidator <<abstract>> {
    ~ AbstractPropertyValidator(context PropertyValidationContext context, propertyName : String, errorCode: String)
    # createError() : void
    * validate() : PropertyValidationResult
    # performSpecificValidation : PropertyValidationResult <<abstract>>
}

AbstractPropertyValidator -u-|> PropertyValidator
ValidatorFactory -d-> ValidationContext
ValidatorFactory -u-> BooleanValidator
ValidatorFactory -u-> IntegerValidator
ValidatorFactory -u-> StringValidator
ValidationContext --> AdapterProperties
ValidationContext --> PropertyValidationLog
BooleanValidator -u-|> AbstractPropertyValidator
IntegerValidator -u-|> AbstractPropertyValidator
StringValidator -u-|> AbstractPropertyValidator
```

Please note that the Validator constructors are all **package-private**. User can intentionally only create validators through the `PropertyValidatorFactory`.

## Runtime View

### Property Validation

We want to avoid that users are plagued with subtle error caused by typos in virtual schema property names or values.

Dedicated validators check everything from the correctness of the name spelling to only using allowed values.

This also serves as a first line of defense against injecting malicious parameters through user input.

There are a couple of rules about how virtual schemas handle properties:

1. The core database hands properties to the virtual schema adapter at the beginning of the request.
2. Property names and values are strings when passed by the database.
3. They are passed "as-is", just like the users entered them.
4. The core database does **not validate** virtual schema properties. That is the job of the adapter.
5. During a request the virtual schema properties don't change. That means we treat them as immutable in the context of a request.
6. Properties can only change in `CREATE` and `REFRESH` requests.

#### Validating Boolean Properties
`dsn~validating-boolean-properties~1`

The `BooleanValidator` checks whether the value or a property is either `true` or `false`, ignoring the case.

Covers:

* `req~validating-boolean-properties~1`

Needs: impl, utest

#### Validating Integer Properties
`dsn~validating-integer-properties~1`

The `IntegerValidator` checks whether the value of a property is a valid integer number.

Covers:

* `req~validating-integer-properties~1`

Needs: impl, utest

#### Integer Interval Validation
`dsn~integer-interval-validation~1`

The `IntegerValidator` allows adding optional upper and lower boundary between which the checked number must be.

Covers:

* `req~validating-integer-properties~1`

Needs: impl, utest

#### Validating Exasol Object ID Properties
`dsn~validating-exasol-object-id-properties~1`

The `ExasolObjectIdValidator` checks whether a given String is a valid Exasol Database Object ID.

The rule of the object IDs follow the [official Exasol documentation](https://docs.exasol.com/db/latest/sql_references/basiclanguageelements.htm#SQLidentifier) (as of 2025-04-08).

We use the library [`db-fundamentals-java`](https://github.com/exasol/db-fundamentals-java) for the validation.

Covers:

* `req~validating-properties-containing-database-objects-ids~1`

Needs: impl, utest

#### Validating Enumeration Properties
`dsn~validating-enumeration-properties~1`

The `EnumerationValiator` checks that a given value is one of the values of an enumeration.

Covers:

* `req~validating-enumeration-properties~1`

Needs: impl, utest

#### Validating the Existence of Mandatory Properties
`dsn~validating-the-existence-of-mandatory-properties~1`

The `RequiredValidator` validates that a mandatory property

1. Exists
2. Is not null
3. Is not empty

Covers:

* `req~validating-the-existence-of-mandatory-properties~1`

Needs: impl, utest

#### Validating a String Against a Regular Expression
`dsn~validating-a-string-against-a-regular-expression~1`

The `StringValidator` checks whether a given string value matches a regular expression.

Covers:

* `req~validating-a-string-against-a-regular-expression~1`

Needs: impl, utest

#### Reporting Format Violations in Properties
`dsn~reporting-format-violations-in-properties~1`

If the user specifies a format description, the `StringValdiator` uses that in an error message instead of the regular expression.

Rationale:

For the end user it is much easier to understand what the validator expects when it requires something like "<host>:<port>" instead of a lengthy regular expression. 

Covers:

* `req~validating-a-string-against-a-regular-expression~1`

Needs: impl, utest

#### Short-circuiting "And" Validation
`dsn~short-circuiting-and-validation~1`

The `AndValidator` executes sub-validators in a pre-defined order. It stops the validation on the first failed sub-validation, returning that result.

Covers:

* `req~validating-combinations-of-properties~1`

Needs: impl, utest

#### "All-of" Validation
`dsn~all-of-validation~1`

The `AllOfValidator` check executes all sub-validators in a pre-defined order. It collects all failures and returns the combined result.

Covers:

* `req~validating-combinations-of-properties~1`

Needs: impl, utest

#### Validation Completeness Check
`dsn~validation-completeness-check~1`

The `CoverageValidator` ensures that all configurable properties, whether mandatory or optional, are properly validated.

Covers:

*`req~property-validation-completeness~1`
*`req~validating-the-existence-of-mandatory-properties~1`
*`req~validating-that-an-optional-property-is-allowed~1`

Needs: impl, utest

### Validating the Absence of Unwanted Properties
`dsn~validating-the-absence-of-unwanted-properties~1`

The `CoverageValidator` ensures that no unwanted property is set.

Covers:

* `req~validating-the-absence-of-unwanted-properties~1`

Needs: impl, utest

## Design Decisions

### How do we Define Virtual Schema Property Validations?

There are a couple of options on how to define property validations. Ranging from cascades of simple conditionals to an elaborate DSL.

The decision is architecture relevant since it impacts:

* system complexity
* reliability
* maintainability

This project is a base library for multiple virtual schemas. Once the decision had been made, the effects propagate into the dependent VS. 

Things would be easy if each property only needed to be validated in isolation. Unfortunately however, this is not the case. Some properties depend on each other, so some logic is required.

Additionally, some properties are shared by multiple VS. Take the log settings for example. They are relevant for all VS and that is why you find them in this base library here.

That means that a base library should support validating the properties it manages and higher level modules should add their own support.

The worst possible situation would be where a higher level module needs to change the validation of a property that is managed by a library below.

We considered the following alternatives:

* Internal DSL

    In this scenario developers can define validations and add to the list of validations in dependent modules.

    This is an elegant solution, that produces nice looking code, but adds considerable complexity. It allows for preventing code duplication

* Validator chaining

    A simpler variant than the DSL, but less flexible, this allows running through a chain of validators. While this also prevents duplication, it does not handle dependencies between the validated properties out of the box.

* Validator functions combined with regular condition cascades

    This is a very flexible solution, that also produces very readable code. The main downside is that it would offload the responsibility for calling all necessary validations to the VS dialect. That also means there will be code duplication, not only on the implementation side (which would not be a lot), but also in unit tests.

### Validator Composition
`dsn~validator-composition~1`

The `PropertyValdiatorFactory` allows creating compositions of base validators for virtual schema properties.

Rationale:

While being simple enough the composition approach allows for enough flexibility.

Rationale:

This allows providing and testing base property validation in the lower layer libraries and only adding what is needed in the dialects. The main benefit is that coupling is kept to a minimum, so that if a validation needs to be improved in a base library, the improvement will propagate to the dialects without code changes there. 

Covers:

* `req~validating-combinations-of-properties~1`

Needs: impl, utest

## Test Strategy

### Property Validation Tests

The constructors of the property validators are intentionally package-private. Users can only generate validators through the `PropertyValidationFactory`. This also means that the unit test of each validator must use the factory. An added benefit of this approach is that the test coverage for the factory is automatically given when a validator is tested.