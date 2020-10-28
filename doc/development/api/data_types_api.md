# Exasol Data Types API Documentation

The following Exasol data types are supported:

## Decimal

```json
{
    "type": "DECIMAL",
    "precision": 18,
    "scale": 2
}
```

## Double

```json
{
    "type": "DOUBLE"
}
```

## Varchar

```json
{
    "type": "VARCHAR",
    "size": 10000,
    "characterSet": "UTF8"
}
```

Notes:

* `characterSet`: Optional. The value is `UTF8` or `ASCII`.

## Char

```json
{
    "type": "CHAR",
    "size": 3,
    "characterSet": "ASCII"
}
```

Notes:

* `characterSet`: Optional. The value is `UTF8` or `ASCII`.

## Date

```json
{
    "type": "DATE"
}
```

## Timestamp

```json
{
    "type": "TIMESTAMP",
    "withLocalTimeZone": true
}
```

Notes:

* `withLocalTimeZone`: Optional. The value is `true` or `false`.

## Boolean

```json
{
    "type": "BOOLEAN"
}
```

## Geometry

```json
{
    "type": "GEOMETRY",
    "srid": 1
}
```

## Interval

```json
{
    "type": "INTERVAL",
    "fromTo": "DAY TO SECONDS",
    "precision": 3,
    "fraction": 4
}
```

```json
{
    "type": "INTERVAL",
    "fromTo": "YEAR TO MONTH",
    "precision": 3
}
```

Notes:

* `fromTo`: The value is `DAY TO SECONDS` or `YEAR TO MONTH`.
* `precision`: Optional.
* `fraction`: Optional. Only for `DAY TO SECONDS` value of 

## Hashtype

```json
{
    "type" : "HASHTYPE",
    "bytesize" : 16
}
```