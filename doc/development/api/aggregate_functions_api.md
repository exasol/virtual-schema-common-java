# Aggregate Functions

This page describes how Exasol aggregate functions map to the Virtual Schemas push-down request API.

## Exasol Aggregate Functions List

### Functions With a Common API

* [Functions with a single argument](#functions-with-a-single-argument)
* [Functions with multiple arguments](#functions-with-multiple-arguments)

### Functions Supporting an Optional `distinct` Field

The aggregate functions from the table below support an optional [`distinct` field](#functions-with-distinct-field). For this field they require an additional capability `*_DISTINCT`.

| Function Name | Required Set-Function Capabilities         |
|---------------|--------------------------------------------|
| AVG           | `AVG` and `AVG_DISTINCT`                   |
| COUNT         | `COUNT` and `COUNT_DISTINCT`               |
| GROUP_CONCAT  | `GROUP_CONCAT` and `GROUP_CONCAT_DISTINCT` |
| LISTAGG       | `LISTAGG` and `LISTAGG_DISTINCT`           |
| MUL           | `MUL` and `MUL_DISTINCT`                   |
| STDDEV        | `STDDEV` and `STDDEV_DISTINCT`             |
| STDDEV_POP    | `STDDEV_POP` and `STDDEV_POP_DISTINCT`     |
| STDDEV_SAMP   | `STDDEV_SAMP` and `STDDEV_SAMP_DISTINCT`   |
| SUM           | `SUM` and `SUM_DISTINCT`                   |
| VARIANCE      | `VARIANCE` and `VARIANCE_DISTINCT`         |
| VAR_POP       | `VAR_POP` and `VAR_POP_DISTINCT`           |
| VAR_SAMP      | `VAR_SAMP` and `VAR_SAMP_DISTINCT`         |

### Special Cases of Aggregate Functions

This section contains functions that have a special API mapping.

| Function Name       | API Mapping Link                                   |
|---------------------|----------------------------------------------------|
| COUNT               | [COUNT function](#count)                           |
| GROUP_CONCAT        | [GROUP_CONCAT function](#group_concat)             |
| LISTAGG             | [LISTAGG function](#listagg)                       |

### Aggregate Functions Not Included in the API

| Function Name       | Comment                                 |
|---------------------|-----------------------------------------|
| ANY                 | The API uses the SOME function.         |
| CORR                | Not included in the API.                |
| COVAR_POP           | Not included in the API.                |
| COVAR_SAMP          | Not included in the API.                |
| GROUPING            | Not included in the API.                |
| PERCENTILE_CONT     | Not included in the API.                |
| PERCENTILE_DISC     | Not included in the API.                |
| REGR_*              | Not included in the API.                |

## Aggregate Functions API

### Functions With a Single Argument

An aggregate function with a single argument (consistent with multiple argument version):

```json
{
    "type": "function_aggregate",
    "name": "<function name>",
    "arguments": [
    {
        ...
    }
    ]
}
```

### Functions With Multiple Arguments

An aggregate function with multiple arguments:

```json
{
    "type": "function_aggregate",
    "name": "<function name>",
    "arguments": [
    {
        ...
    },
    {
        ...
    }
    ]
}
```

### Functions With `distinct` Field

`distinct` is an optional field.

```json
{
    "type": "function_aggregate",
    "name": "<function name>",
    "distinct": true,
    "arguments": [
        ...
    ],

    ...
}
```

### COUNT

`COUNT(*)`
 (Requires set-function capability `COUNT_STAR`. Please notice, that the set-function capability `COUNT` is not required in this case.)

```json
{
    "type": "function_aggregate",
    "name": "COUNT"
}
```

`COUNT([DISTINCT] exp)`
(requires set-function capability `COUNT`)

`COUNT([DISTINCT] (exp1, exp2, ...))`
(requires set-function capabilities `COUNT` and `COUNT_TUPLE`)

```json
{
    "type": "function_aggregate",
    "name": "COUNT",
    "distinct": true,
    "arguments": [
    {
        ...
    },
    {
        ...
    }
    ]
}
```

Notes:
* `distinct`: Optional. Requires set-function capability `COUNT_DISTINCT.`

### GROUP_CONCAT

`GROUP_CONCAT([DISTINCT] arg [orderBy] [SEPARATOR 'separator'])`
 (requires set-function capability `GROUP_CONCAT`)

```json
{
    "type": "function_aggregate_group_concat",
    "name": "GROUP_CONCAT",
    "distinct": true,
    "arguments": [
    {
        ...
    }
    ],
    "orderBy" : [
        ...
    ],
    "separator":
    {
        "type": "literal_string",
        "value": "..."
    }
}
```

Notes:
* `distinct`: Optional. Requires set-function capability `GROUP_CONCAT_DISTINCT.`
* `orderBy`: Optional. The requested order-by clause, a list of `order_by_element` elements. Requires the set-function capability `GROUP_CONCAT_ORDER_BY`.
* `separator`: Optional. Requires set-function capability `GROUP_CONCAT_SEPARATOR`.

### LISTAGG

`LISTAGG([DISTINCT] arg[, 'separator'] ON OVERFLOW {ERROR | TRUNCATE ['truncationFiller'] {WITH | WITHOUT} COUNT}) [WITHIN GROUP (orderBy)]`
 (requires set-function capability `LISTAGG`)

```json
{
    "type": "function_aggregate_listagg",
    "name": "LISTAGG",
    "distinct": true,
    "arguments": [
    {
        ...
    }
    ],
    "separator":
    {
        "type": "literal_string",
        "value": "..."
    },
    "overflowBehavior":
    {
        "type": "TRUNCATE",
        "truncationType": "WITH COUNT",
        "truncationFiller":
        {
            "type": "literal_string",
            "value": "..."
        }    
    },
    "orderBy": [
        ...
    ]
}
```

Notes:

* `distinct`: Optional. Requires set-function capability `LISTAGG_DISTINCT`.
* `separator`: Optional. Requires set-function capability `LISTAGG_SEPARATOR`.
* `overflowBehavior`: `type` is `"ERROR"` (requires set-function capability `LISTAGG_ON_OVERFLOW_ERROR`) or `"TRUNCATE"` (requires set-function capability `LISTAGG_ON_OVERFLOW_TRUNCATE`). Only for `"TRUNCATE"` the members `truncationType` and optionally `truncationFiller` exist. `truncationType` is `"WITH COUNT"` or `"WITHOUT COUNT"`.
* `orderBy`: Optional. The requested order-by clause, a list of `order_by_element` elements. Requires the set-function capability `LISTAGG_ORDER_BY`.
