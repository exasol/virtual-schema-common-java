# Developer Guide — Virtual Schema Common Java

## Adapter Property Validation

Virtual schema adapter properties are user input and must be validated before being used. On the one hand to protect users from typos and misunderstandings and on the other to secure the adapter and prevent injection of malicious parameters.

### Validators

This library offers several validators that make checking the adapter properties easy.

In a typical scenario you have some base properties that all adapters know. This library for example understands the `LOG_LEVEL` property that lets users change the log level.

Virtual Schema dialects (like the one for MariaDB) offer additional properties. That is why the validation is controlled by higher level modules in the dialect.

Let's look at the simplest possible Validator first, a validator for boolean values.

```java
import com.exasol.adapter.properties.AdapterProperties;
import com.exasol.adapter.properties.PropertyValidator;
import com.exasol.adapter.properties.ValidationResult;
import com.exasol.adapter.properties.ValidatorFactory;

import java.util.Map;

final AdapterProperties adapterProperties = new AdapterProperties(Map.of("THE_PARAMETER", true));

final ValidatorFactory factory = ValidatorFactory.create(adapterProperties);
final PropertyValidator validator = factory.bool("THE_PARAMETER");
final ValidationResult result = validator.validate();
```

In a real adapter the properties would of course not be constructed by hand but rather be passed as part of handling an adapter request. For the sake of simplicity however, this example takes a shortcut.

The `ValidatorFactory` is responsible for creating all validators. It also keeps track of which parameters are covered by validators, but we will get to that later.

Each validator implements the interface `PropertyValidator`, where the most important method is `validate`. This method returns a validation result consisting of an indicator whether the validation succeeded and an error message if it did not.

| Validator Class           | Factory Method                                    | Purpose                                                                        |
|---------------------------|---------------------------------------------------|--------------------------------------------------------------------------------|
| `AndValidator`            | `and(validator1, validator2, ...)`                | Short-circuits validation on first failure                                     |
| `BooleanValidator`        | `bool(propertyName)`                   | Validates that a property value is a boolean                                   |
| `AllOfValidator`          | `allOf(validator1, validator2, ...)`              | Runs all validators without short-circuiting                                   |
| `CoverageValidator`       | `allCovered()`                                    | Validates that all properties have been checked by validators                  |
| `EnumerationValidator`    | `enumeration(propertyName, enumClass)` | Validates that a property value is one of the values defined in an enumeration |
| `ExasolObjectIdValidator` | `exasolObjectId(propertyName)`         | Validates that a property value follows Exasol's object ID format rules        |
| `IntegerValidator`        | `integer(propertyName)`                | Validates that a property value is a valid integer                             |
| `IntegerValidator`        | `integer(propertyName, min, max)`      | Validates that a property value is an integer within specified boundaries      |
| `RequiredValidator`       | `required(propertyName)`               | Validates that a property exists                                               |
| `StringValidator`         | `matches(propertyName, pattern)`       | Validates property against regex pattern                                       |

### Validator Composition

To allow complex validation the factory has a couple of methods that allow creating compositions of validators.

```java
factory.and(
        factory.required("REQUIRED_PARAMETER"),
        factory.bool("REQUIRED_PARAMETER")
)
```

This allows defining a parameter as required and a boolean. The `AndValidator` short-circuits on the first failure. That means, that if the parameter is not set, the boolean check will be skipped.

For this particular combination, there is also a shorthand form:

```java
factory.required(factory.bool("REQUIRED_PARAMETER"))
```

This is much more convenient.

If short-circuiting is not an option — for example because you want to present all validation failures at once instead of forcing users in to a trial-and-error loop — use the `AllOfValidator`.

```java
factory.allOf(
        factory.matches("HOST", "\\w+"),
        factory.integer("PORT")
)
```

We recommend also checking that no unwanted properties are set with the `CoverageValidator`. Put that one at the very end.

```java
factory.allOf(
        // … all your other validators
        factory.allCovered()
)
```